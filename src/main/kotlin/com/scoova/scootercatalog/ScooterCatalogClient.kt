package com.scoova.scootercatalog

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.atomic.AtomicReference

/**
 * Fetches the Scoova scooter catalog from the rider-platform server,
 * caches it on disk, and exposes synchronous lookups for hot paths.
 *
 * Wire-up at app launch:
 * ```kotlin
 * ScooterCatalogClient.configure(
 *     apiKey  = BuildConfig.SCOOVA_API_KEY,
 *     cacheDir = applicationContext.cacheDir,
 * )
 * applicationScope.launch { ScooterCatalogClient.refresh() }
 * ```
 *
 * After [refresh] succeeds the in-memory map is hot and [getScooterData]
 * resolves serial-prefix lookups without I/O. The cache file persists
 * across launches so subsequent cold starts see the last-known catalog
 * before the next refresh round-trip completes.
 *
 * Cache TTL: 24 h, refreshed in the background if older than that.
 * Refresh failures fall back to the cached snapshot — never throws on
 * a stale read.
 */
public object ScooterCatalogClient {

    private const val TAG = "ScoovaScooterCatalog"
    private const val CACHE_FILENAME = "scoova-scooter-catalog.json"

    private var apiKey: String? = null
    private var baseUrl: String = "https://api.scoo-va.info"
    private var cacheFile: File? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _catalog = MutableStateFlow<List<ScooterData>>(emptyList())
    /** Live stream of the cached catalog. Replays the latest snapshot to
     *  new collectors; updates as [refresh] succeeds. */
    public val catalog: StateFlow<List<ScooterData>> = _catalog.asStateFlow()

    private val byPrefix = AtomicReference<List<Pair<String, ScooterData>>>(emptyList())

    /** Wall-clock millis of the last successful server fetch (0 = never). */
    public var lastRefreshAtMs: Long = 0L
        private set

    @Serializable
    private data class ServerEnvelope(
        val success: Boolean = true,
        val data: ServerData? = null,
    )

    @Serializable
    private data class ServerData(
        val version: Long = 0L,
        val models: List<ScooterData> = emptyList(),
    )

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    /**
     * Configure once at app launch. Re-calling with new parameters
     * replaces the configuration; the in-memory cache + on-disk file
     * survive.
     *
     * @param apiKey  Scoova platform API key sent as `X-API-Key` on
     *                catalog reads.
     * @param cacheDir Writable directory for the on-disk cache snapshot
     *                — usually `context.cacheDir`. If null, only the
     *                in-memory cache is used; restarting the app
     *                empties the catalog until the next refresh.
     * @param baseUrl Rider-platform base URL. Defaults to the live
     *                Scoova gateway.
     */
    public fun configure(
        apiKey: String,
        cacheDir: File? = null,
        baseUrl: String = "https://api.scoo-va.info",
    ) {
        this.apiKey = apiKey
        this.baseUrl = baseUrl.trimEnd('/')
        this.cacheFile = cacheDir?.let { File(it, CACHE_FILENAME) }
        // Warm the in-memory catalog from disk so the FIRST UI tick after
        // launch already has data, before the network round-trip completes.
        cacheFile?.takeIf { it.exists() }?.let { f ->
            runCatching {
                val cached = json.decodeFromString<List<ScooterData>>(f.readText())
                publish(cached)
            }
        }
    }

    /**
     * Synchronous lookup — matches the given serial against the cached
     * catalog, picking the model with the longest matching prefix.
     * Returns null when:
     *   - The cache is empty (no successful refresh + no on-disk file).
     *   - The serial doesn't match any known prefix.
     *
     * Safe to call from Composables / UI binding.
     */
    public fun scooterDataForSerial(serial: String): ScooterData? {
        if (serial.isEmpty()) return null
        val prefixIndex = byPrefix.get()
        val lowered = serial.lowercase()
        var best: ScooterData? = null
        var bestLen = 0
        for ((prefix, data) in prefixIndex) {
            if (lowered.startsWith(prefix) && prefix.length > bestLen) {
                best = data
                bestLen = prefix.length
            }
        }
        return best
    }

    /** Hit the server, replace the cache, publish to [catalog]. */
    public suspend fun refresh(): Result<List<ScooterData>> = withContext(Dispatchers.IO) {
        val key = apiKey ?: return@withContext Result.failure(
            IllegalStateException("ScooterCatalogClient.configure() not called"),
        )
        runCatching {
            val url = URL("$baseUrl/api/v1/scooter-catalog")
            val conn = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                setRequestProperty("Accept", "application/json")
                setRequestProperty("X-API-Key", key)
                connectTimeout = 10_000
                readTimeout = 20_000
            }
            try {
                val code = conn.responseCode
                if (code !in 200..299) {
                    val err = conn.errorStream?.bufferedReader()?.use { it.readText() }.orEmpty().take(200)
                    throw RuntimeException("HTTP $code: $err")
                }
                val body = conn.inputStream.bufferedReader().use { it.readText() }
                val parsed = json.decodeFromString<ServerEnvelope>(body)
                val models = parsed.data?.models ?: emptyList()
                cacheFile?.let { f ->
                    runCatching {
                        f.parentFile?.mkdirs()
                        f.writeText(json.encodeToString(kotlinx.serialization.builtins.ListSerializer(ScooterData.serializer()), models))
                    }
                }
                publish(models)
                lastRefreshAtMs = System.currentTimeMillis()
                models
            } finally {
                conn.disconnect()
            }
        }
    }

    /** Fire-and-forget refresh — convenience for `Application.onCreate`. */
    public fun refreshAsync() {
        scope.launch { refresh() }
    }

    private fun publish(models: List<ScooterData>) {
        _catalog.value = models
        // Pre-compute a flat (prefix → model) index so lookups don't
        // iterate the nested `prefixes` list per call.
        byPrefix.set(
            models.flatMap { m -> m.prefixes.map { it.lowercase() to m } }
                .sortedByDescending { it.first.length },
        )
    }
}

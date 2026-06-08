package com.scoova.scootercatalog

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Generic shape of one entry in the Scoova scooter catalog.
 *
 * The catalog itself (every model, every spec) lives **server-side** in
 * Postgres at the Scoova rider-platform — see
 * `Scoova_Sever/scripts/migrate_add_scooter_catalog.sql`. This SDK ships
 * with the shape only; data is fetched at app launch via
 * [ScooterCatalogClient.refresh] and cached locally for offline use.
 *
 * The pre-2.0 SDK (1.0.x on Maven Central) bundled the catalog as a
 * hand-edited `scootersDataMap` literal. That data is proprietary —
 * vehicle lineup, internal spec values, BLE serial prefix patterns,
 * feature-flag matrix. Shipping it in a public artifact was a mistake;
 * 2.0 fixes that by moving the data to a Scoova-owned database
 * accessed through an authenticated endpoint.
 *
 * Field names + types match the server JSON response exactly.
 */
@Serializable
public data class ScooterData(
    public val id: String,
    public val code: String,
    public val prefixes: List<String>,
    @SerialName("modelType")
    public val type: String,
    @SerialName("modelYear")
    public val year: Int,
    public val stemLight: Boolean,
    public val manualTurnSignalsSupported: Boolean,
    public val manualCruiseSupported: Boolean,
    @SerialName("maxSpeedKph")
    public val maxSpeed: Int,
    public val watt: Double,
    public val minVoltage: Double,
    public val maxVoltage: Double,
    @SerialName("brandNewBatteryCapacityMah")
    public val brandNewBatteryCapacity: Double,
    public val hasLudo: Boolean,
    public val appCanChangeLudo: Boolean,
    public val autoPark: Boolean,
    @SerialName("rangeMinKm")
    public val rangeMin: Int,
    @SerialName("rangeMaxKm")
    public val rangeMax: Int,
    @SerialName("sellingMaxSpeedKph")
    public val sellingMaxSpeed: Int,
    public val hasAppleFindMy: Boolean,
    @SerialName("hasNfc")
    public val hasNFC: Boolean,
    /** Asset name for UI image lookup. May be null when the catalog
     *  hasn't recorded a dedicated image for this model. */
    public val imageAssetName: String? = null,
)

/**
 * Type label kept for source-compatibility with consumers that used to
 * import `com.scoova.scootercatalog.ScooterType`. It's now just a thin
 * wrapper around the catalog `id` string — the server is the source of
 * truth for which ids exist.
 *
 * Pre-2.0 consumers that wrote `ScooterType.APOLLO_PHANTOM_2024` should
 * migrate to `ScooterType("Phantom_2024")` or just use the raw String
 * id. The constant-style enum is gone because the catalog is no longer
 * fixed at SDK build time.
 */
@JvmInline
public value class ScooterType(public val rawValue: String) {
    public companion object {
        /** Fallback used when no other model matches a serial. */
        public val UNKNOWN: ScooterType = ScooterType("Unknown")
    }
}

/**
 * Get the [ScooterData] for a given BLE serial number from the
 * in-memory cache populated by [ScooterCatalogClient]. Returns null if
 * the cache is empty (no successful [ScooterCatalogClient.refresh]
 * since launch) or if the serial doesn't match any known prefix.
 *
 * Synchronous: safe to call from a hot path (Composables, UI binding).
 * Use [ScooterCatalogClient.scooterData] for a flow that reacts to
 * catalog refreshes.
 */
public fun getScooterData(serial: String): ScooterData? =
    ScooterCatalogClient.scooterDataForSerial(serial)

/** Mirror of [getScooterData] returning just the id. Pre-2.0 callers
 *  that used `getScooterType(serial)` get the same behaviour — null
 *  on cache miss, otherwise the matched id wrapped in [ScooterType]. */
public fun getScooterType(serial: String): ScooterType? =
    getScooterData(serial)?.let { ScooterType(it.id) }

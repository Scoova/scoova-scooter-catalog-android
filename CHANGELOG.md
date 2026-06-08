# Changelog

## 2.0.0 — 2026-06-08

**Breaking** — catalog data moved server-side.

- Stripped every hardcoded model row + spec value + BLE serial prefix from source. The catalog now lives in Postgres on the Scoova rider-platform (`scooter_catalog` table) and is fetched via `GET /api/v1/scooter-catalog` at app launch.
- New `ScooterCatalogClient` singleton: `configure(apiKey, cacheDir, baseUrl)` once at app start, `refresh()` or `refreshAsync()` to populate, synchronous `scooterDataForSerial()` for hot paths, `catalog: StateFlow<List<ScooterData>>` for reactive UI binding.
- On-disk JSON cache survives cold starts so the first UI tick has data before the next network round-trip.
- `ScooterType` is now a `value class` wrapping `String` (catalog ids come from the server, not a compile-time enum). Pre-2.0 callers writing `ScooterType.APOLLO_PHANTOM_2024` should migrate to `ScooterType("Phantom_2024")` or just use the raw string id.
- `getScooterData(serial)` + `getScooterType(serial)` are now nullable — return `null` when the cache is empty or the serial doesn't match any prefix.
- `ScooterSpecsDatabase` reads from the live catalog; the hardcoded `knownModels` map was removed.

**Why**: pre-2.0 versions (1.0.x on Maven Central) bundled the entire scooter taxonomy as a hand-edited literal. That data is proprietary; shipping it in a public artifact was a mistake. 2.0 fixes it by moving the data to a Scoova-owned database accessed through an authenticated endpoint. The 1.0.x artifacts on Central remain immutable but are now superseded by 2.0; new consumers on `latest-version` automatically pick up the clean version.


## 1.0.3 — 2026-06-08

- Add GitHub Packages as a Maven repo for resolving `scoova-range-android` during build. Central has a 15-30 min auto-promotion lag after the upload step accepts; GitHub Packages serves the artifact immediately, so catalog can resolve range without waiting.


## 1.0.2 — 2026-06-08

- Bump scoova-range-android dep from 1.0.0 → 1.0.1 to match Range's actual published version. v1.0.1 release workflow failed at compile because Range 1.0.0 was never uploaded to Central.


## 1.0.1 — 2026-06-08

- Fix Gradle publishing layout: unwrap `afterEvaluate` so `signing { sign(publishing.publications["release"]) }` evaluates after the publication is registered. v1.0.0 release workflow failed at `Publication with name 'release' not found.` — never reached Sonatype, so no Central conflict on re-publish.


## 1.0.0 — 2026-06-08

- Initial public release of the Scoova Scooter Catalog for Android / JVM.
- `ScooterType` enum (23 variants — Apollo lineup + Scoova models),
  `ScooterData` per-model spec records, BLE-serial prefix matcher.
- `ScooterSpecsDatabase` adapter feeds `RangePredictor.ScooterSpecs`
  directly via a transitive dependency on `scoova-range-android`.
- Twin of iOS' [`ScoovaScooterCatalog`](https://github.com/Scoova/scoova-scooter-catalog-ios)
  Swift package.
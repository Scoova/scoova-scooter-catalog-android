# Changelog

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
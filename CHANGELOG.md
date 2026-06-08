# Changelog

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
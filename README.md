# Scoova Scooter Catalog — Android

Vehicle taxonomy + per-model specifications for the Scoova fleet. The
stable `ScooterType` enum, BLE-serial prefix lookup, and ready-to-use
[`RangePredictor.ScooterSpecs`](https://github.com/Scoova/scoova-range-android)
adapters. Twin of iOS' [`ScoovaScooterCatalog`](https://github.com/Scoova/scoova-scooter-catalog-ios)
Swift package.

Pure Kotlin — no Android dependencies.

## Install

```kotlin
dependencies {
    implementation("info.scoo-va:scoova-scooter-catalog-android:1.0.0")
    // ScooterSpecsDatabase returns RangePredictor.ScooterSpecs directly,
    // so the range SDK is pulled in transitively as an api() dep.
}
```

## Usage

```kotlin
// Look up by BLE serial:
val data = getScooterData("APH-2024-…")
println("${data.type} ${data.year} — top speed ${data.maxSpeed} km/h")

// Or feed the range engine directly:
val specs = ScooterSpecsDatabase.getSpecsBySerial("APH-2024-…")!!
val predictor = RangePredictor()
val prediction = predictor.predictRange(telemetry, specs)
```

## License

Apache 2.0. See [LICENSE](LICENSE).

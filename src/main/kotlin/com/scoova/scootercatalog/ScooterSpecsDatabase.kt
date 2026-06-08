package com.scoova.scootercatalog

import com.scoova.range.RangePredictor

/**
 * Lookup table over the Scoova vehicle catalog. Maps a BLE serial number
 * (or a model name) to a [RangePredictor.ScooterSpecs] so the range
 * engine doesn't have to know anything about scooter taxonomy.
 *
 * Twin of iOS' `RangePredictor+Scoova.swift` extension — same conversion
 * math (battery Wh = mAh × nominalV / 1000, average range = mid of
 * rangeMin/rangeMax) and the same fallback defaults for unknown
 * serials.
 *
 * Pure-data. Add a model here, rebuild, both platforms agree.
 */
public object ScooterSpecsDatabase {

    /** Get specs from a BLE serial number. Returns the default profile
     *  (40 km typical range, 600 Wh battery) when the serial doesn't
     *  match any known prefix. */
    public fun getSpecsBySerial(serialNumber: String): RangePredictor.ScooterSpecs? {
        if (serialNumber.isEmpty()) return null
        val scooterData = getScooterData(serialNumber)
        val batteryWh = scooterData.brandNewBatteryCapacity * scooterData.maxVoltage / 1000.0
        val avgRangeKm = (scooterData.rangeMin + scooterData.rangeMax) / 2.0
        return RangePredictor.ScooterSpecs(
            batteryCapacityWh = batteryWh,
            nominalVoltage = (scooterData.minVoltage + scooterData.maxVoltage) / 2.0,
            minVoltage = scooterData.minVoltage,
            maxVoltage = scooterData.maxVoltage,
            typicalRangeKm = avgRangeKm,
            motorPowerW = scooterData.watt,
            // Default rider-plus-deck estimate when the catalog hasn't
            // recorded a specific scooter weight. Kept as a constant so
            // the range engine's outputs are deterministic regardless of
            // catalog version.
            weightKg = 25.0,
        )
    }

    /** Legacy path — prefer [getSpecsBySerial]. Returns null on unknown
     *  model names. */
    public fun getSpecs(modelName: String): RangePredictor.ScooterSpecs? =
        knownModels[modelName.lowercase()]

    /** Specs to use when nothing else matches. Conservative
     *  middle-of-fleet numbers. */
    public fun getDefaultSpecs(): RangePredictor.ScooterSpecs = defaultSpecs

    public fun getAllModelNames(): List<String> = knownModels.keys.toList()

    public fun isKnownModel(modelName: String): Boolean =
        knownModels.containsKey(modelName.lowercase())

    private val defaultSpecs = RangePredictor.ScooterSpecs(
        batteryCapacityWh = 600.0,
        nominalVoltage = 48.0,
        minVoltage = 40.0,
        maxVoltage = 54.6,
        typicalRangeKm = 40.0,
        motorPowerW = 500.0,
        weightKg = 20.0,
    )

    private val knownModels = mapOf(
        // Scoova
        "scoova pro" to RangePredictor.ScooterSpecs(
            batteryCapacityWh = 748.0, nominalVoltage = 52.0,
            minVoltage = 42.0, maxVoltage = 58.8,
            typicalRangeKm = 50.0, motorPowerW = 1000.0, weightKg = 23.0,
        ),
        "scoova lite" to RangePredictor.ScooterSpecs(
            batteryCapacityWh = 468.0, nominalVoltage = 48.0,
            minVoltage = 40.0, maxVoltage = 54.6,
            typicalRangeKm = 35.0, motorPowerW = 500.0, weightKg = 18.0,
        ),
        "scoova max" to RangePredictor.ScooterSpecs(
            batteryCapacityWh = 1120.0, nominalVoltage = 60.0,
            minVoltage = 50.0, maxVoltage = 67.2,
            typicalRangeKm = 70.0, motorPowerW = 1500.0, weightKg = 28.0,
        ),
        // Apollo
        "apollo city" to RangePredictor.ScooterSpecs(
            batteryCapacityWh = 561.0, nominalVoltage = 52.0,
            minVoltage = 42.0, maxVoltage = 58.8,
            typicalRangeKm = 45.0, motorPowerW = 500.0, weightKg = 19.0,
        ),
        "apollo city pro" to RangePredictor.ScooterSpecs(
            batteryCapacityWh = 748.0, nominalVoltage = 52.0,
            minVoltage = 42.0, maxVoltage = 58.8,
            typicalRangeKm = 55.0, motorPowerW = 1000.0, weightKg = 23.0,
        ),
        "apollo ghost" to RangePredictor.ScooterSpecs(
            batteryCapacityWh = 1008.0, nominalVoltage = 52.0,
            minVoltage = 42.0, maxVoltage = 58.8,
            typicalRangeKm = 60.0, motorPowerW = 2000.0, weightKg = 30.0,
        ),
        "apollo phantom" to RangePredictor.ScooterSpecs(
            batteryCapacityWh = 1200.0, nominalVoltage = 52.0,
            minVoltage = 42.0, maxVoltage = 58.8,
            typicalRangeKm = 65.0, motorPowerW = 2400.0, weightKg = 34.0,
        ),
        "apollo pro" to RangePredictor.ScooterSpecs(
            batteryCapacityWh = 1440.0, nominalVoltage = 60.0,
            minVoltage = 50.0, maxVoltage = 67.2,
            typicalRangeKm = 80.0, motorPowerW = 3200.0, weightKg = 38.0,
        ),
        // Terra
        "terra s1" to RangePredictor.ScooterSpecs(
            batteryCapacityWh = 600.0, nominalVoltage = 48.0,
            minVoltage = 40.0, maxVoltage = 54.6,
            typicalRangeKm = 40.0, motorPowerW = 600.0, weightKg = 20.0,
        ),
        "terra x1" to RangePredictor.ScooterSpecs(
            batteryCapacityWh = 960.0, nominalVoltage = 52.0,
            minVoltage = 42.0, maxVoltage = 58.8,
            typicalRangeKm = 55.0, motorPowerW = 1200.0, weightKg = 26.0,
        ),
        // Volt
        "volt pro" to RangePredictor.ScooterSpecs(
            batteryCapacityWh = 720.0, nominalVoltage = 48.0,
            minVoltage = 40.0, maxVoltage = 54.6,
            typicalRangeKm = 48.0, motorPowerW = 800.0, weightKg = 22.0,
        ),
        // Fallback
        "generic" to defaultSpecs,
    )
}

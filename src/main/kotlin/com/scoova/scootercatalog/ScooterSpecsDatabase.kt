package com.scoova.scootercatalog

import com.scoova.range.RangePredictor

/**
 * Adapter that converts a catalog [ScooterData] entry into the shape
 * `scoova-range-android`'s [RangePredictor.predictRange] consumes.
 *
 * Source of data: [ScooterCatalogClient]'s in-memory cache, populated
 * from `GET /api/v1/scooter-catalog` on the rider-platform. No
 * hardcoded models in this SDK — the pre-2.0 hardcoded `knownModels`
 * map was removed when the catalog moved server-side.
 */
public object ScooterSpecsDatabase {

    /** Get specs from a BLE serial number. Returns null if the catalog
     *  cache is empty (no successful [ScooterCatalogClient.refresh]
     *  since launch) or the serial doesn't match any model's prefix. */
    public fun getSpecsBySerial(serialNumber: String): RangePredictor.ScooterSpecs? {
        val data = ScooterCatalogClient.scooterDataForSerial(serialNumber) ?: return null
        return data.toRangeSpecs()
    }

    /** Get specs by catalog id (e.g. `"Phantom_2024"`). Returns null on
     *  cache miss / unknown id. */
    public fun getSpecsById(id: String): RangePredictor.ScooterSpecs? =
        ScooterCatalogClient.catalog.value
            .firstOrNull { it.id == id }
            ?.toRangeSpecs()

    /** Conservative default specs for unknown vehicles. Pure-Scoova
     *  constants — never derived from a specific model's data. */
    public fun getDefaultSpecs(): RangePredictor.ScooterSpecs = DEFAULT_SPECS

    public fun getAllIds(): List<String> = ScooterCatalogClient.catalog.value.map { it.id }

    public fun isKnownId(id: String): Boolean =
        ScooterCatalogClient.catalog.value.any { it.id == id }

    private val DEFAULT_SPECS = RangePredictor.ScooterSpecs(
        batteryCapacityWh = 600.0,
        nominalVoltage    = 48.0,
        minVoltage        = 40.0,
        maxVoltage        = 54.6,
        typicalRangeKm    = 40.0,
        motorPowerW       = 500.0,
        weightKg          = 20.0,
    )
}

/** Convert a catalog row into the range engine's input struct. The
 *  conversion math (battery Wh ≈ mAh × nominalV / 1000, typical range
 *  midpoint of advertised min/max) matches iOS' historical
 *  `RangePredictor+Scoova.swift`. */
public fun ScooterData.toRangeSpecs(): RangePredictor.ScooterSpecs {
    val batteryWh = brandNewBatteryCapacity * maxVoltage / 1000.0
    val avgRangeKm = (rangeMin + rangeMax) / 2.0
    return RangePredictor.ScooterSpecs(
        batteryCapacityWh = batteryWh,
        nominalVoltage    = (minVoltage + maxVoltage) / 2.0,
        minVoltage        = minVoltage,
        maxVoltage        = maxVoltage,
        typicalRangeKm    = avgRangeKm,
        motorPowerW       = watt,
        // Default rider-plus-deck estimate. Catalog rows don't carry
        // per-model rider weights so this stays constant.
        weightKg          = 25.0,
    )
}

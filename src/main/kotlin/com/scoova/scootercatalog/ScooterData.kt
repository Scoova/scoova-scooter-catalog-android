package com.scoova.scootercatalog

/**
 * ScooterData - Complete 1:1 port from iOS ScooterModels.swift
 * Contains scooter model specifications and capability flags
 * 
 * IMPORTANT: This file must stay in sync with iOS/Scoova/Models/ScooterModels.swift
 */

// MARK: - Scooter Type Enum
enum class ScooterType(val rawValue: String) {
    APOLLO_DEFAULT("Default"),
    APOLLO_GHOST("Ghost"),
    APOLLO_MINI("Mini"),
    APOLLO_AIR_2021("Air_2021"),
    APOLLO_AIR_PRO_2021("Air_Pro_2021"),
    APOLLO_AIR_PRO_2022("Air_Pro_2022"),
    APOLLO_AIR_PRO_2023("Air_Pro_2023"),
    APOLLO_AIR_PRO_2024("Air_Pro_2024"),
    APOLLO_CITY_2022("City_2022"),
    APOLLO_CITY_2023("City_2023"),
    APOLLO_CITY_PRO_2022("City_Pro_2022"),
    APOLLO_CITY_PRO_2023("City_Pro_2023"),
    APOLLO_CITY_PRO_2024("City_Pro_2024"),
    APOLLO_PRO_2023("Pro_2023"),
    APOLLO_PHANTOM_2022("Phantom_2022"),
    APOLLO_PHANTOM_2023("Phantom_2023"),
    APOLLO_PHANTOM_2024("Phantom_2024"),
    APOLLO_GO_2024("Go_2024"),
    APOLLO_PHANTOM_2025("Phantom_2025"),
    APOLLO_PHANTOM_STELLAR_2025("Phantom_Stellar_2025"),
    APOLLO_CITY_PRO_2025("City_Pro_2025"),
    APOLLO_GO_2025("Go_2025"),
    UNKNOWN("Unknown");

    // Matches iOS ScooterType.imageAssetName (lines 13-29)
    val imageAssetName: String?
        get() = when (this) {
            APOLLO_PRO_2023 -> "apollo_pro"
            APOLLO_PHANTOM_2022, APOLLO_PHANTOM_2023, APOLLO_PHANTOM_2024,
            APOLLO_PHANTOM_2025, APOLLO_PHANTOM_STELLAR_2025 -> "apollo_phantom"
            APOLLO_GO_2024, APOLLO_GO_2025 -> "apollo_go"
            APOLLO_CITY_2022, APOLLO_CITY_2023,
            APOLLO_CITY_PRO_2022, APOLLO_CITY_PRO_2023, APOLLO_CITY_PRO_2024, APOLLO_CITY_PRO_2025 -> "apollo_city"
            APOLLO_AIR_PRO_2021, APOLLO_AIR_PRO_2022, APOLLO_AIR_PRO_2023, APOLLO_AIR_PRO_2024 -> "apollo_air_pro"
            else -> null
        }
}

// MARK: - Scooter Data
data class ScooterData(
    val id: ScooterType,
    val code: String,
    val prefixes: List<String>,
    val type: String,
    val year: Int,
    val stemLight: Boolean,
    val manualTurnSignalsSupported: Boolean,
    val manualCruiseSupported: Boolean,
    val maxSpeed: Int,
    val watt: Double,
    val minVoltage: Double,
    val maxVoltage: Double,
    val brandNewBatteryCapacity: Double,
    val hasLudo: Boolean,
    val appCanChangeLudo: Boolean,
    val autoPark: Boolean,
    val rangeMin: Int,
    val rangeMax: Int,
    val sellingMaxSpeed: Int,
    val hasAppleFindMy: Boolean,
    val hasNFC: Boolean
)

// MARK: - Scooters Data Map - EXACT 1:1 port from iOS lines 82-244
val scootersDataMap: Map<ScooterType, ScooterData> = mapOf(
    ScooterType.APOLLO_DEFAULT to ScooterData(
        id = ScooterType.APOLLO_DEFAULT, code = "Default", prefixes = listOf("default"), 
        type = "Scooter", year = 2050,
        stemLight = true, manualTurnSignalsSupported = true, manualCruiseSupported = true,
        maxSpeed = 100, watt = 504.0, minVoltage = 31.0, maxVoltage = 42.0, 
        brandNewBatteryCapacity = 15000.0,
        hasLudo = false, appCanChangeLudo = false, autoPark = false,
        rangeMin = 32, rangeMax = 48, sellingMaxSpeed = 45, 
        hasAppleFindMy = false, hasNFC = false
    ),
    ScooterType.APOLLO_GHOST to ScooterData(
        id = ScooterType.APOLLO_GHOST, code = "Ghost", prefixes = listOf("ghost"), 
        type = "Ghost", year = 2023,
        stemLight = false, manualTurnSignalsSupported = false, manualCruiseSupported = false,
        maxSpeed = 80, watt = 1196.0, minVoltage = 43.0, maxVoltage = 58.8, 
        brandNewBatteryCapacity = 18000.0,
        hasLudo = true, appCanChangeLudo = false, autoPark = false,
        rangeMin = 32, rangeMax = 48, sellingMaxSpeed = 52, 
        hasAppleFindMy = false, hasNFC = false
    ),
    ScooterType.APOLLO_MINI to ScooterData(
        id = ScooterType.APOLLO_MINI, code = "Mini", prefixes = listOf("36781", "3781"), 
        type = "Mini", year = 2022,
        stemLight = false, manualTurnSignalsSupported = false, manualCruiseSupported = false,
        maxSpeed = 40, watt = 280.0, minVoltage = 31.0, maxVoltage = 42.0, 
        brandNewBatteryCapacity = 10400.0,
        hasLudo = false, appCanChangeLudo = false, autoPark = false,
        rangeMin = 30, rangeMax = 55, sellingMaxSpeed = 34, 
        hasAppleFindMy = false, hasNFC = false
    ),
    ScooterType.APOLLO_AIR_2021 to ScooterData(
        id = ScooterType.APOLLO_AIR_2021, code = "L9_2021", 
        prefixes = listOf("air", "36751", "3751"), 
        type = "Air", year = 2021,
        stemLight = false, manualTurnSignalsSupported = false, manualCruiseSupported = false,
        maxSpeed = 45, watt = 273.0, minVoltage = 31.0, maxVoltage = 42.0, 
        brandNewBatteryCapacity = 7600.0,
        hasLudo = false, appCanChangeLudo = false, autoPark = false,
        rangeMin = 30, rangeMax = 55, sellingMaxSpeed = 34, 
        hasAppleFindMy = false, hasNFC = false
    ),
    ScooterType.APOLLO_AIR_PRO_2021 to ScooterData(
        id = ScooterType.APOLLO_AIR_PRO_2021, code = "L9_Pro_2021", 
        prefixes = listOf("airpro", "36101"), 
        type = "Air Pro", year = 2021,
        stemLight = false, manualTurnSignalsSupported = false, manualCruiseSupported = false,
        maxSpeed = 45, watt = 360.0, minVoltage = 31.0, maxVoltage = 42.0, 
        brandNewBatteryCapacity = 10400.0,
        hasLudo = false, appCanChangeLudo = false, autoPark = false,
        rangeMin = 30, rangeMax = 55, sellingMaxSpeed = 34, 
        hasAppleFindMy = false, hasNFC = false
    ),
    ScooterType.APOLLO_AIR_PRO_2022 to ScooterData(
        id = ScooterType.APOLLO_AIR_PRO_2022, code = "L9_Pro_2022", 
        prefixes = listOf("315122", "3101"), 
        type = "Air Pro", year = 2022,
        stemLight = false, manualTurnSignalsSupported = false, manualCruiseSupported = false,
        maxSpeed = 45, watt = 504.0, minVoltage = 31.0, maxVoltage = 42.0, 
        brandNewBatteryCapacity = 15000.0,
        hasLudo = false, appCanChangeLudo = false, autoPark = false,
        rangeMin = 30, rangeMax = 55, sellingMaxSpeed = 34, 
        hasAppleFindMy = false, hasNFC = false
    ),
    ScooterType.APOLLO_AIR_PRO_2023 to ScooterData(
        id = ScooterType.APOLLO_AIR_PRO_2023, code = "L9_Pro_2023", 
        prefixes = listOf("315123"), 
        type = "Air Pro", year = 2023,
        stemLight = false, manualTurnSignalsSupported = false, manualCruiseSupported = false,
        maxSpeed = 60, watt = 504.0, minVoltage = 31.0, maxVoltage = 42.0, 
        brandNewBatteryCapacity = 15000.0,
        hasLudo = false, appCanChangeLudo = false, autoPark = false,
        rangeMin = 30, rangeMax = 55, sellingMaxSpeed = 34, 
        hasAppleFindMy = false, hasNFC = false
    ),
    ScooterType.APOLLO_AIR_PRO_2024 to ScooterData(
        id = ScooterType.APOLLO_AIR_PRO_2024, code = "L9_Pro_2024", 
        prefixes = listOf("315124"), 
        type = "Air Pro", year = 2024,
        stemLight = false, manualTurnSignalsSupported = true, manualCruiseSupported = true,
        maxSpeed = 60, watt = 504.0, minVoltage = 31.0, maxVoltage = 42.0, 
        brandNewBatteryCapacity = 15000.0,
        hasLudo = false, appCanChangeLudo = false, autoPark = false,
        rangeMin = 30, rangeMax = 55, sellingMaxSpeed = 34, 
        hasAppleFindMy = false, hasNFC = false
    ),
    ScooterType.APOLLO_CITY_2022 to ScooterData(
        id = ScooterType.APOLLO_CITY_2022, code = "L9C_2022", 
        prefixes = listOf("48141", "4141"), 
        type = "City", year = 2022,
        stemLight = false, manualTurnSignalsSupported = false, manualCruiseSupported = false,
        maxSpeed = 60, watt = 672.0, minVoltage = 41.0, maxVoltage = 54.6, 
        brandNewBatteryCapacity = 13500.0,
        hasLudo = false, appCanChangeLudo = false, autoPark = false,
        rangeMin = 38, rangeMax = 70, sellingMaxSpeed = 52, 
        hasAppleFindMy = false, hasNFC = false
    ),
    ScooterType.APOLLO_CITY_2023 to ScooterData(
        id = ScooterType.APOLLO_CITY_2023, code = "L9C_2023", 
        prefixes = listOf("4201"), 
        type = "City", year = 2023,
        stemLight = false, manualTurnSignalsSupported = false, manualCruiseSupported = false,
        maxSpeed = 60, watt = 864.0, minVoltage = 41.0, maxVoltage = 54.6, 
        brandNewBatteryCapacity = 13500.0,
        hasLudo = false, appCanChangeLudo = false, autoPark = false,
        rangeMin = 38, rangeMax = 70, sellingMaxSpeed = 52, 
        hasAppleFindMy = false, hasNFC = false
    ),
    ScooterType.APOLLO_CITY_PRO_2022 to ScooterData(
        id = ScooterType.APOLLO_CITY_PRO_2022, code = "L9C_pro_2022", 
        prefixes = listOf("48142", "4142"), 
        type = "City Pro", year = 2022,
        stemLight = false, manualTurnSignalsSupported = false, manualCruiseSupported = false,
        maxSpeed = 60, watt = 864.0, minVoltage = 41.0, maxVoltage = 54.6, 
        brandNewBatteryCapacity = 18000.0,
        hasLudo = false, appCanChangeLudo = false, autoPark = false,
        rangeMin = 38, rangeMax = 70, sellingMaxSpeed = 52, 
        hasAppleFindMy = false, hasNFC = false
    ),
    ScooterType.APOLLO_CITY_PRO_2023 to ScooterData(
        id = ScooterType.APOLLO_CITY_PRO_2023, code = "L9C_pro_2023", 
        prefixes = listOf("420223"), 
        type = "City Pro", year = 2023,
        stemLight = false, manualTurnSignalsSupported = false, manualCruiseSupported = false,
        maxSpeed = 60, watt = 864.0, minVoltage = 41.0, maxVoltage = 54.6, 
        brandNewBatteryCapacity = 20000.0,
        hasLudo = false, appCanChangeLudo = false, autoPark = false,
        rangeMin = 38, rangeMax = 70, sellingMaxSpeed = 52, 
        hasAppleFindMy = false, hasNFC = false
    ),
    ScooterType.APOLLO_CITY_PRO_2024 to ScooterData(
        id = ScooterType.APOLLO_CITY_PRO_2024, code = "L9C_pro_2024", 
        prefixes = listOf("420224", "4202BDX"), 
        type = "City Pro", year = 2024,
        stemLight = false, manualTurnSignalsSupported = true, manualCruiseSupported = true,
        maxSpeed = 60, watt = 864.0, minVoltage = 41.0, maxVoltage = 54.6, 
        brandNewBatteryCapacity = 20000.0,
        hasLudo = false, appCanChangeLudo = false, autoPark = false,
        rangeMin = 38, rangeMax = 70, sellingMaxSpeed = 52, 
        hasAppleFindMy = false, hasNFC = false
    ),
    ScooterType.APOLLO_PRO_2023 to ScooterData(
        id = ScooterType.APOLLO_PRO_2023, code = "A12_2024", 
        prefixes = listOf("5302", "52301", "5301"), 
        type = "Pro", year = 2023,
        stemLight = false, manualTurnSignalsSupported = true, manualCruiseSupported = true,
        maxSpeed = 100, watt = 1560.0, minVoltage = 43.5, maxVoltage = 67.2, 
        brandNewBatteryCapacity = 30000.0,
        hasLudo = true, appCanChangeLudo = false, autoPark = false,
        rangeMin = 50, rangeMax = 100, sellingMaxSpeed = 70, 
        hasAppleFindMy = false, hasNFC = false
    ),
    ScooterType.APOLLO_PHANTOM_2022 to ScooterData(
        id = ScooterType.APOLLO_PHANTOM_2022, code = "Phantom_2022", 
        prefixes = listOf("pha", "phah60v", "5223", "5322"), 
        type = "Phantom", year = 2022,
        stemLight = false, manualTurnSignalsSupported = false, manualCruiseSupported = false,
        maxSpeed = 80, watt = 1196.0, minVoltage = 43.0, maxVoltage = 58.8, 
        brandNewBatteryCapacity = 23400.0,
        hasLudo = true, appCanChangeLudo = false, autoPark = false,
        rangeMin = 45, rangeMax = 90, sellingMaxSpeed = 85, 
        hasAppleFindMy = false, hasNFC = false
    ),
    ScooterType.APOLLO_PHANTOM_2023 to ScooterData(
        id = ScooterType.APOLLO_PHANTOM_2023, code = "Phantom_2023", 
        prefixes = listOf("523223"), 
        type = "Phantom", year = 2023,
        stemLight = false, manualTurnSignalsSupported = false, manualCruiseSupported = false,
        maxSpeed = 80, watt = 1196.0, minVoltage = 43.0, maxVoltage = 58.8, 
        brandNewBatteryCapacity = 23400.0,
        hasLudo = true, appCanChangeLudo = false, autoPark = false,
        rangeMin = 45, rangeMax = 90, sellingMaxSpeed = 85, 
        hasAppleFindMy = false, hasNFC = false
    ),
    ScooterType.APOLLO_PHANTOM_2024 to ScooterData(
        id = ScooterType.APOLLO_PHANTOM_2024, code = "Phantom_2024", 
        prefixes = listOf("523224", "5232BDXF"), 
        type = "Phantom", year = 2024,
        stemLight = false, manualTurnSignalsSupported = false, manualCruiseSupported = false,
        maxSpeed = 80, watt = 1196.0, minVoltage = 43.0, maxVoltage = 58.8, 
        brandNewBatteryCapacity = 23400.0,
        hasLudo = true, appCanChangeLudo = false, autoPark = false,
        rangeMin = 45, rangeMax = 90, sellingMaxSpeed = 85, 
        hasAppleFindMy = false, hasNFC = false
    ),
    ScooterType.APOLLO_GO_2024 to ScooterData(
        id = ScooterType.APOLLO_GO_2024, code = "A9", 
        prefixes = listOf("3152", "3152BDX", "3152BDAX"), 
        type = "Go", year = 2024,
        stemLight = true, manualTurnSignalsSupported = true, manualCruiseSupported = true,
        maxSpeed = 60, watt = 504.0, minVoltage = 31.0, maxVoltage = 42.0, 
        brandNewBatteryCapacity = 15000.0,
        hasLudo = false, appCanChangeLudo = false, autoPark = false,
        rangeMin = 32, rangeMax = 48, sellingMaxSpeed = 45, 
        hasAppleFindMy = false, hasNFC = false
    ),
    ScooterType.APOLLO_PHANTOM_2025 to ScooterData(
        id = ScooterType.APOLLO_PHANTOM_2025, code = "A11_52V", 
        prefixes = listOf("5272B"), 
        type = "Phantom 2.0", year = 2025,
        stemLight = true, manualTurnSignalsSupported = true, manualCruiseSupported = true,
        maxSpeed = 100, watt = 1260.0, minVoltage = 43.0, maxVoltage = 58.8, 
        brandNewBatteryCapacity = 27000.0,
        hasLudo = true, appCanChangeLudo = true, autoPark = true,
        rangeMin = 45, rangeMax = 90, sellingMaxSpeed = 85, 
        hasAppleFindMy = true, hasNFC = true
    ),
    ScooterType.APOLLO_PHANTOM_STELLAR_2025 to ScooterData(
        id = ScooterType.APOLLO_PHANTOM_STELLAR_2025, code = "A11_60V", 
        prefixes = listOf("6302B"), 
        type = "Phantom 2.0 Stellar", year = 2025,
        stemLight = true, manualTurnSignalsSupported = true, manualCruiseSupported = true,
        maxSpeed = 100, watt = 1260.0, minVoltage = 48.0, maxVoltage = 67.2, 
        brandNewBatteryCapacity = 30000.0,
        hasLudo = true, appCanChangeLudo = true, autoPark = true,
        rangeMin = 45, rangeMax = 90, sellingMaxSpeed = 100, 
        hasAppleFindMy = true, hasNFC = true
    ),
    ScooterType.APOLLO_CITY_PRO_2025 to ScooterData(
        id = ScooterType.APOLLO_CITY_PRO_2025, code = "L9G_2025", 
        prefixes = listOf("4202B", "4203B"), 
        type = "City Pro", year = 2025,
        stemLight = true, manualTurnSignalsSupported = true, manualCruiseSupported = true,
        maxSpeed = 65, watt = 864.0, minVoltage = 41.0, maxVoltage = 54.6, 
        brandNewBatteryCapacity = 20000.0,
        hasLudo = false, appCanChangeLudo = false, autoPark = true,
        rangeMin = 38, rangeMax = 70, sellingMaxSpeed = 52, 
        hasAppleFindMy = true, hasNFC = true
    ),
    ScooterType.APOLLO_GO_2025 to ScooterData(
        id = ScooterType.APOLLO_GO_2025, code = "A9_2025", 
        prefixes = listOf("3152B"), 
        type = "Go", year = 2025,
        stemLight = true, manualTurnSignalsSupported = true, manualCruiseSupported = true,
        maxSpeed = 60, watt = 504.0, minVoltage = 31.0, maxVoltage = 42.0, 
        brandNewBatteryCapacity = 15000.0,
        hasLudo = true, appCanChangeLudo = true, autoPark = true,
        rangeMin = 32, rangeMax = 48, sellingMaxSpeed = 45, 
        hasAppleFindMy = false, hasNFC = false
    ),
    ScooterType.UNKNOWN to ScooterData(
        id = ScooterType.UNKNOWN, code = "Unknown", prefixes = emptyList(), 
        type = "Unknown", year = 0,
        stemLight = true, manualTurnSignalsSupported = true, manualCruiseSupported = true,
        maxSpeed = 100, watt = 504.0, minVoltage = 31.0, maxVoltage = 42.0, 
        brandNewBatteryCapacity = 15000.0,
        hasLudo = false, appCanChangeLudo = false, autoPark = false,
        rangeMin = 32, rangeMax = 48, sellingMaxSpeed = 45, 
        hasAppleFindMy = false, hasNFC = false
    )
)

// MARK: - Get Scooter Data by Serial Number (iOS lines 246-266)
/**
 * Returns the ScooterData for a given serial number by matching the longest prefix.
 * If no match is found, returns the default scooter data.
 */
fun getScooterData(serial: String): ScooterData {
    var bestMatch = scootersDataMap[ScooterType.APOLLO_DEFAULT]!!
    var longestPrefixLength = 0
    for (scooter in scootersDataMap.values) {
        for (prefix in scooter.prefixes) {
            if (serial.lowercase().startsWith(prefix.lowercase())) {
                val prefixLength = prefix.length
                if (prefixLength > longestPrefixLength) {
                    longestPrefixLength = prefixLength
                    bestMatch = scooter
                }
            }
        }
    }
    return bestMatch
}

/**
 * Returns the ScooterType for a given serial number
 */
fun getScooterType(serial: String): ScooterType {
    return getScooterData(serial).id
}

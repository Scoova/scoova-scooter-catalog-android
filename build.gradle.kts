// Pure-data Scoova vehicle taxonomy + per-model specifications. Works
// in Android apps, KMP modules, or server-side Kotlin — no Android
// dependencies. Same shape as `scoova-range-android` /
// `scoova-analysis-android`.
plugins {
    // Pinned to 2.0.21 — match the Scoova consumer app's Kotlin runtime so
    // `.kotlin_module` metadata stays decodable by Compose-2.0 consumers.
    kotlin("jvm") version "2.0.21"
    `maven-publish`
    signing
    `java-library`
}

group = "info.scoo-va"
version = "1.0.2"

repositories {
    // mavenLocal() first so the locally-staged Scoova SDKs (e.g.
    // `scoova-range-android`) resolve before the Maven Central
    // versions ship.
    mavenLocal()
    mavenCentral()
}

dependencies {
    // Reads `RangePredictor.ScooterSpecs` to expose a typed lookup that
    // feeds the range engine directly — no per-app marshalling.
    api("info.scoo-va:scoova-range-android:1.0.1")
    testImplementation(kotlin("test"))
}

kotlin { jvmToolchain(17) }
tasks.test { useJUnitPlatform() }

java {
    withSourcesJar()
    withJavadocJar()
}

// Pure-JVM library — `components["java"]` is created during plugin
// application, so the publishing block evaluates top-level (no
// `afterEvaluate` wrap). That ordering matters: the `signing { ... }`
// block below references `publishing.publications["release"]`, and
// an `afterEvaluate`-wrapped publication isn't visible to it yet.
publishing {
    publications {
        create<MavenPublication>("release") {
            from(components["java"])
            groupId = "info.scoo-va"
            artifactId = "scoova-scooter-catalog-android"
            version = project.version.toString()
            pom {
                name.set("Scoova Scooter Catalog — Android")
                description.set(
                    "Vehicle taxonomy + per-model specifications for the Scoova fleet. " +
                        "Stable scooter-type enum, serial-prefix lookup, and ready-to-use " +
                        "RangePredictor.ScooterSpecs adapters. Twin of iOS' " +
                        "ScooterModels.swift. Zero Android dependencies."
                )
                url.set("https://scoo-va.info")
                inceptionYear.set("2026")
                licenses {
                    license {
                        name.set("Apache-2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                        distribution.set("repo")
                    }
                }
                developers {
                    developer {
                        id.set("scoova")
                        name.set("Scoova")
                        email.set("info@scoo-va.info")
                        organization.set("Scoova")
                        organizationUrl.set("https://scoo-va.info")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/Scoova/scoova-scooter-catalog-android.git")
                    developerConnection.set("scm:git:ssh://github.com:Scoova/scoova-scooter-catalog-android.git")
                    url.set("https://github.com/Scoova/scoova-scooter-catalog-android")
                }
            }
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Scoova/scoova-scooter-catalog-android")
            credentials {
                username = System.getenv("GITHUB_ACTOR") ?: project.findProperty("gpr.user") as? String ?: ""
                password = System.getenv("GITHUB_TOKEN") ?: project.findProperty("gpr.key") as? String ?: ""
            }
        }
        maven {
            name = "LocalStaging"
            url = uri(layout.buildDirectory.dir("staging-deploy"))
        }
    }
}


signing {
    val signingKey: String? = System.getenv("SIGNING_KEY")
    val signingPassword: String = System.getenv("SIGNING_PASSWORD") ?: ""
    isRequired = signingKey != null
    if (signingKey != null) {
        useInMemoryPgpKeys(signingKey, signingPassword)
        sign(publishing.publications["release"])
    }
}

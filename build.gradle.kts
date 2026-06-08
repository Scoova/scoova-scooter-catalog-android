// Server-backed scooter catalog SDK. The catalog itself (models, specs,
// BLE serial prefixes, feature flags) lives in Postgres on the Scoova
// rider-platform; this SDK is just the wire-shape + an HTTP client that
// fetches and caches the data at app launch.
//
// 2.0.0 — major rewrite. Pre-2.0 versions (1.0.x on Maven Central)
// bundled the catalog as a hardcoded `scootersDataMap` literal that
// exposed proprietary vehicle data to anyone who downloaded the
// artifact. 2.0 removes every model row + every spec value from
// source and routes them through `GET /api/v1/scooter-catalog`
// (auth-gated by `X-API-Key`).
plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.serialization") version "2.0.21"
    `maven-publish`
    signing
    `java-library`
}

group = "info.scoo-va"
version = "2.0.0"

repositories {
    // mavenLocal() first so the locally-staged Scoova SDKs (e.g.
    // `scoova-range-android`) resolve before the Maven Central
    // versions ship.
    mavenLocal()
    mavenCentral()
    // GitHub Packages — covers Maven Central's 15-30 min auto-promotion
    // lag. When Range publishes its release.yml fires both the Central
    // upload AND a GitHub Packages publish; the latter is queryable
    // instantly, so catalog can resolve range immediately after Range's
    // tag-push, without waiting for Central to sync. CI runs already have
    // GITHUB_ACTOR + GITHUB_TOKEN in env (the workflow injects them); the
    // empty fallback is for local builds where this repo's reads aren't
    // needed (mavenLocal serves them).
    maven {
        name = "GitHubPackagesScoovaRange"
        url = uri("https://maven.pkg.github.com/Scoova/scoova-range-android")
        credentials {
            username = System.getenv("GITHUB_ACTOR") ?: project.findProperty("gpr.user") as? String ?: ""
            password = System.getenv("GITHUB_TOKEN") ?: project.findProperty("gpr.key") as? String ?: ""
        }
    }
}

dependencies {
    // Reads `RangePredictor.ScooterSpecs` to expose a typed lookup that
    // feeds the range engine directly — no per-app marshalling.
    api("info.scoo-va:scoova-range-android:1.0.1")
    // Network + JSON for the catalog client.
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
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

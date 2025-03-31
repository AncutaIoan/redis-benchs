plugins {
    kotlin("jvm") version "2.0.21"
}

group = "angrymiaucino"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Kotlin Coroutines for async operations
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")

    // CLIENTS
    // Lettuce (Reactive Redis Client)
    implementation("io.lettuce:lettuce-core:6.5.5.RELEASE")
    // Redisson (Reactive Redis Client)
    implementation("org.redisson:redisson:3.23.5")
    // Jedis (Non-Reactive Redis Client)
    implementation("redis.clients:jedis:5.1.0")

    // SLF4J Simple Logger to suppress warnings
    implementation("org.slf4j:slf4j-simple:2.0.12")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}
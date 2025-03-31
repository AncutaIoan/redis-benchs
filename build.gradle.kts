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
//    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

    // Lettuce (Reactive Redis Client)
    implementation("io.lettuce:lettuce-core:6.5.0.RELEASE")

    // Redisson (Reactive Redis Client)
    implementation("org.redisson:redisson:3.23.5")

    // Spring Data Redis (Optional - If you want to test it)
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive:3.1.0")

    // Jedis (Non-Reactive Redis Client)
    implementation("redis.clients:jedis:5.1.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
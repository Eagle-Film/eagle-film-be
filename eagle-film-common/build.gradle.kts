import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

val bootJar: BootJar by tasks

bootJar.enabled = false

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

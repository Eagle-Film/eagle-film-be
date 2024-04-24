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

val jar: Jar by tasks
val bootJar: BootJar by tasks

bootJar.enabled = true
jar.enabled = true

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

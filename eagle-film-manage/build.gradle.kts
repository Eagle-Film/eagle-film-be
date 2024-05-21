plugins {
    kotlin("jvm")
}

val jdaVersion = "5.0.0-beta.12"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":eagle-film-common"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("net.dv8tion:JDA:$jdaVersion")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

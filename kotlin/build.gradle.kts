plugins {
    kotlin("jvm") version "1.6.0"
}

group = "com.github.tke"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.withType<Test> { useJUnitPlatform() }

dependencies {
    val kotestVersion = "5.0.0"

    implementation(kotlin("reflect"))

    testImplementation("io.kotest:kotest-property:$kotestVersion")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
}

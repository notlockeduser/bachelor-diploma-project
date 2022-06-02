import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.6.7"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
}

group = "com.holovin"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    // main
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // optional
    implementation("de.jplag:jplag:3.0.0")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("net.lingala.zip4j:zip4j:2.10.0")

    implementation("org.jeasy:easy-random:5.0.0")
    implementation("org.jeasy:easy-random-core:5.0.0")

    implementation("org.springframework.boot:spring-boot-starter-data-mongodb:2.6.7")
    implementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    implementation("org.junit.platform:junit-platform-launcher:1.8.2")
    implementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    implementation("org.apache.maven.surefire:surefire-report-parser:3.0.0-M6")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf:2.7.0")

    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:3.1.3")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

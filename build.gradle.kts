group = "pathfinder"
version = "1.0"

plugins {
    war
    kotlin("jvm") version "2.2.0"
    kotlin("plugin.jpa") version "2.2.0"
    kotlin("plugin.spring") version "2.2.0"
    id("org.springframework.boot") version "3.5.3"
    id("io.spring.dependency-management") version "1.1.7"
}

repositories {
    mavenCentral()
    maven { url = uri("https://m2.chew.pro/releases") }
    maven { url = uri("https://jitpack.io") }
    maven {
        url = uri("https://maven.pkg.github.com/TheMather1/dice-syntax")
        credentials {
            username = "token"
            password = System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation("com.fasterxml.jackson.module", "jackson-module-kotlin", "2.18.4")
    implementation("com.h2database", "h2", "2.3.232")
//    implementation("com.github.Hexworks", "mixite", "2020.1.0")
//    implementation("org.hexworks.cobalt", "cobalt.datatypes-jvm", "2020.0.12-RELEASE")
    implementation("io.micrometer", "micrometer-registry-prometheus", "1.15.1")
    implementation("net.dv8tion", "JDA", "5.6.1")
    implementation("no.mather.ttrpg", "dice-syntax", "0.2.0")
    implementation("org.springframework.boot", "spring-boot-starter-actuator")
    implementation("org.springframework.boot", "spring-boot-starter-data-jpa")
    implementation("org.springframework.boot", "spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot", "spring-boot-starter-web")
    implementation("org.springframework.boot", "spring-boot-starter-thymeleaf")
    implementation("pw.chew", "jda-chewtils", "2.0")
}

tasks {
    test {
        useJUnitPlatform()
    }
}

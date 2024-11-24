import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "pathfinder"
version = "1.0"

plugins {
    war
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.spring") version "1.6.0"
    id("org.springframework.boot") version "2.6.2"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

repositories {
    mavenCentral()
    jcenter()
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
    implementation("javax.servlet", "jstl", "1.2")
    implementation("net.dv8tion", "JDA", "5.0.0-alpha.21")
    implementation("no.mather.ttrpg", "dice-syntax", "0.1.2")
    implementation("org.mapdb", "mapdb", "3.0.8")
    implementation("org.springframework.boot", "spring-boot-starter-actuator")
    implementation("org.springframework.boot", "spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot", "spring-boot-starter-web")
    providedRuntime("org.springframework.boot", "spring-boot-starter-tomcat")
    implementation("org.apache.taglibs", "taglibs-standard-impl", "1.2.5")
    implementation("org.apache.tomcat.embed", "tomcat-embed-jasper")
}

tasks {
    war {
        webAppDirName = "src/main"
    }
    test {
        useJUnitPlatform()
    }
}

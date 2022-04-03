import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.0.0-SNAPSHOT"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.20"
    kotlin("plugin.spring") version "1.6.20"
    kotlin("kapt") version "1.6.20"
    kotlin("plugin.noarg") version "1.6.20"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

val lightDBVersion: String by rootProject
val objectFormatVersion: String by rootProject

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
        extendsFrom(configurations.kapt.get())
    }
}

ext["jedis.version"] = "4.2.0"

noArg {
    annotation("i.server")
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation(platform("com.github.d7z-team.light-db:bom:$lightDBVersion"))
    implementation(platform("com.github.d7z-team.object-format:bom:$objectFormatVersion"))
    implementation(platform("com.github.d7z-team.light-db-session:bom:0.1.0"))
    implementation("com.github.d7z-team.light-db-session:session-core")
    implementation("com.github.d7z-team.object-format:format-core")
    implementation("com.github.d7z-team.object-format:format-spring-boot-starter")
    implementation("com.github.d7z-team.object-format:format-extra-jackson")
    implementation("com.github.d7z-team.logger4k:logger-core:0.2.1")
    implementation("com.github.d7z-team.logger4k:logger-forward:0.2.1")
    implementation("org.springdoc:springdoc-openapi-starter-common:2.0.0-M1")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.0-M1")
    implementation("com.github.d7z-team.light-db:db-api")
    implementation("com.github.d7z-team.light-db:db-jedis")
    implementation("com.github.d7z-team.light-db:db-memory")
    implementation("com.github.d7z-team.light-db:db-spring-boot-starter")
    implementation("org.jetbrains.exposed:exposed-spring-boot-starter:0.37.3")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-freemarker")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
    kapt("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
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
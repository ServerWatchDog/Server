import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.0.0-M2"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.20"
    kotlin("plugin.spring") version "1.6.20"
    kotlin("plugin.noarg") version "1.6.20"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

val lightDBVersion: String by rootProject
val objectFormatVersion: String by rootProject
val lightDBSessionVersion: String by rootProject
val lightDBCacheVersion: String by rootProject

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

ext["jedis.version"] = "4.2.0"

noArg {
    annotation("i.server")
}

repositories {
    mavenCentral()
    maven("https://repo.open-edgn.cn/maven/")
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation(platform("org.jetbrains.exposed:exposed-bom:0.37.3"))
    implementation(platform("com.github.d7z-team.light-db:bom:$lightDBVersion"))
    implementation(platform("com.github.d7z-team.object-format:bom:$objectFormatVersion"))
    implementation(platform("com.github.d7z-team.light-db-session:bom:$lightDBSessionVersion"))
    implementation(platform("com.github.d7z-team.light-db-cache:bom:$lightDBCacheVersion"))
    implementation("com.github.d7z-team.light-db:db-api")
    implementation("com.github.d7z-team.light-db:db-jedis")
    implementation("com.github.d7z-team.light-db:db-memory")
    implementation("com.github.d7z-team.light-db:db-spring-boot-starter")
    implementation("com.github.d7z-team.light-db-session:session-core")
    implementation("com.github.d7z-team:security4k:0.1.0")
    implementation("com.github.d7z-team.light-db-cache:cache-core")
    implementation("com.github.d7z-team.object-format:format-core")
    implementation("com.github.d7z-team.object-format:format-spring-boot-starter")
    implementation("com.github.d7z-team.object-format:format-extra-jackson")
    implementation("com.github.d7z-team.logger4k:logger-core:0.2.1")
    implementation("com.github.d7z-team.logger4k:logger-forward:0.2.1")
    implementation("org.springdoc:springdoc-openapi-starter-common:2.0.0-M1")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.0-M1")
    implementation("com.j256.two-factor-auth:two-factor-auth:1.3")
    implementation("org.jetbrains.exposed:exposed-spring-boot-starter")
    implementation("org.jetbrains.exposed:exposed-java-time")
    implementation("org.jetbrains.exposed:exposed-money")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-freemarker")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
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

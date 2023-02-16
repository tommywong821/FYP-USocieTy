import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.8"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
	id("org.jetbrains.kotlin.plugin.jpa") version "1.6.21"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
}

group = "ngok3.fyp"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	//spring rest controller
	implementation("org.springframework.boot:spring-boot-starter-web")
	//send restful request
	implementation("com.squareup.okhttp3:okhttp")
	//xml convertor
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.14.0")
	//postgresql
	runtimeOnly("org.postgresql:postgresql")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.jetbrains.kotlin:kotlin-maven-noarg:1.6.21")
	//db data validation
	implementation("org.springframework.boot:spring-boot-starter-validation")
	//jwt generator
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
    //openapi
    implementation("org.springdoc:springdoc-openapi-ui:1.6.14")
    //testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    //springboot 2.X max version:3.x
    testImplementation("com.ninja-squad:springmockk:3.1.2")
    testImplementation("com.h2database:h2")
    //spring security role based login
    implementation("org.springframework.boot:spring-boot-starter-security")
    //AWS SDK V2, S3
    implementation(platform("software.amazon.awssdk:bom:2.20.2"))
    implementation("software.amazon.awssdk:s3:2.20.2")
    //File utils
    implementation("commons-io:commons-io:2.11.0")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

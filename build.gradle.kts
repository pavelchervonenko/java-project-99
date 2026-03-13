plugins {
	id("application")
	id("checkstyle")
	id("jacoco")
	id("org.sonarqube") version "7.2.3.7755"
	id("org.springframework.boot") version "4.0.3"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "hexlet.code"
version = "0.0.1-SNAPSHOT"
description = "Task Manager"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-data-jdbc-test")
	testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
	testImplementation("org.springframework.boot:spring-boot-starter-security-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	runtimeOnly("com.h2database:h2")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

checkstyle {
	toolVersion = "13.3.0"
	configFile = file("${rootDir}/config/checkstyle/checkstyle.xml")
	isIgnoreFailures = false
}

sonar {
	properties {
		property("sonar.projectKey", "pavelchervonenko_java-project-99")
		property("sonar.organization", "pavelchervonenko")
		property("sonar.host.url", "https://sonarcloud.io")
		property(
			"sonar.coverage.jacoco.xmlReportPaths",
			layout.buildDirectory.file("reports/jacoco/test/jacocoTestReport.xml").get().asFile.absolutePath
		)
	}
}

jacoco {
	toolVersion = "0.8.14"
}

tasks.jacocoTestReport {
	reports {
		xml.required.set(true)
		html.required.set(true)
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

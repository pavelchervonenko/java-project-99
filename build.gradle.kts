plugins {
	id("application")
	id("checkstyle")
	id("jacoco")
	id("org.sonarqube") version "6.3.1.5724"
	id("org.springframework.boot") version "3.4.3"
	id("io.spring.dependency-management") version "1.1.7"
	id("io.sentry.jvm.gradle") version "4.14.0"

}

group = "hexlet.code"
version = "0.0.1-SNAPSHOT"
description = "Task Manager"

java {
	sourceCompatibility = JavaVersion.VERSION_21
}

application {
	mainClass = "hexlet.code.AppApplication"
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
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	testImplementation("org.springframework.security:spring-security-test")

	implementation("org.openapitools:jackson-databind-nullable:0.2.6")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.8")

	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testCompileOnly("org.projectlombok:lombok")
	testAnnotationProcessor("org.projectlombok:lombok")

	implementation("org.mapstruct:mapstruct:1.5.5.Final")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
	testAnnotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

	annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")
	testAnnotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")

	runtimeOnly("com.h2database:h2")
	runtimeOnly("org.postgresql:postgresql")

	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	implementation("io.sentry:sentry-spring-boot-starter-jakarta:7.19.0")
}

sentry {
	includeSourceContext = System.getenv("SENTRY_AUTH_TOKEN") != null

	org = "my-company-bi0"
	projectName = "java-spring"
	authToken = System.getenv("SENTRY_AUTH_TOKEN")
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

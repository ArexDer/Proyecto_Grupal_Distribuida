plugins {
	java
	id("org.springframework.boot") version "3.5.3"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.app"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
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

dependencyManagement {
	imports {
		mavenBom ("org.springframework.cloud:spring-cloud-dependencies:2025.0.0")
	}
}


dependencies {
	//  JPA → Persistencia con Hibernate/JPA (API para ORM con BD relacional)
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	//  REST y servidor web embebido (Tomcat) + JSON (Jackson)
	implementation("org.springframework.boot:spring-boot-starter-web")

	//  Migraciones de base de datos automáticas (Flyway Core)
	implementation("org.flywaydb:flyway-core")

	//  Driver de Flyway para PostgreSQL (necesario junto con flyway-core)
	implementation("org.flywaydb:flyway-database-postgresql")

	//  Service Discovery usando Consul (registra y descubre servicios en Consul)
	implementation("org.springframework.cloud:spring-cloud-starter-consul-discovery")

	//  Actuator → expone endpoints para métricas, health checks, info, etc.
	implementation("org.springframework.boot:spring-boot-starter-actuator")

	//  Anotaciones de Lombok (solo en compilación)
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	//  Driver de PostgreSQL (para conectarse a la base de datos en tiempo de ejecución)
	runtimeOnly("org.postgresql:postgresql")

	//  Dependencias para pruebas con Spring Boot
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

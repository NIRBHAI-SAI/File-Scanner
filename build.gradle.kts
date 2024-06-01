plugins {
	java
	id("org.springframework.boot") version "3.2.5"
	id("io.spring.dependency-management") version "1.1.4"
}

group = "com.fscan"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {


	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("com.google.guava:guava:33.2.0-jre")
	implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.14")
	implementation("org.json:json:20231013")
	implementation("org.springframework:spring-mock:2.0.8")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa");
	implementation("org.springframework.boot:spring-boot-starter-data-rest");
	runtimeOnly("org.postgresql:postgresql");
	compileOnly("org.projectlombok:lombok:1.18.30")
	implementation("org.modelmapper:modelmapper:3.2.0")



}

tasks.withType<Test> {
	useJUnitPlatform()
}

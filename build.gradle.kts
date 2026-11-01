plugins {
	`java-library`
	`maven-publish`
	id("io.spring.javaformat") version "0.0.48"
	checkstyle
}

group = "dev.onepieceapi"
version = "0.1.0"
description = "One Piece API - reader axis: what the reader knows at chapter or episode N"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
	withSourcesJar()
	withJavadocJar()
}

repositories {
	mavenCentral()
}

// Pure Java on purpose (ADR-0001): no runtime dependency, so both services can use it
// whatever their framework version. Lombok is compile-time only.
dependencies {
	compileOnly("org.projectlombok:lombok:1.18.46")
	annotationProcessor("org.projectlombok:lombok:1.18.46")
	testCompileOnly("org.projectlombok:lombok:1.18.46")
	testAnnotationProcessor("org.projectlombok:lombok:1.18.46")
	testImplementation(platform("org.junit:junit-bom:6.1.3"))
	testImplementation("org.junit.jupiter:junit-jupiter")
	testImplementation("org.assertj:assertj-core:3.27.7")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

checkstyle {
	toolVersion = "14.0.0"
}

publishing {
	publications {
		create<MavenPublication>("maven") {
			from(components["java"])
			pom {
				name = "one-piece-reader-time"
				description = project.description
				url = "https://github.com/one-piece-api/one-piece-reader-time"
			}
		}
	}
	repositories {
		maven {
			name = "GitHubPackages"
			url = uri("https://maven.pkg.github.com/one-piece-api/one-piece-reader-time")
			credentials {
				username = System.getenv("GITHUB_ACTOR")
				password = System.getenv("GITHUB_TOKEN")
			}
		}
	}
}

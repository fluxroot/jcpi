plugins {
	`java-library`
	`maven-publish`
	signing
}

group = "com.fluxchess.jcpi"
version = "2.0.0-SNAPSHOT"

dependencies {
	testImplementation(libs.junit)
	testImplementation(libs.assertj)
	testImplementation(libs.slf4j)
}

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(8))
	}
	withSourcesJar()
	withJavadocJar()
}

tasks.jar {
	manifest {
		attributes("Automatic-Module-Name" to "com.fluxchess.jcpi")
	}
}

tasks.test {
	useJUnitPlatform()
}

publishing {
	publications {
		create<MavenPublication>("mavenJava") {
			from(components["java"])
			pom {
				name.set("Java Chess Protocol Interface")
				description.set("The Java Chess Protocol Interface provides a clean object-oriented interface to the UCI protocol.")
				url.set("https://github.com/fluxroot/jcpi")
				licenses {
					license {
						name.set("Apache License, Version 2.0")
						url.set("http://www.apache.org/licenses/LICENSE-2.0")
					}
				}
				developers {
					developer {
						name.set("Flux Chess Project")
					}
				}
				scm {
					connection.set("scm:git:git@github.com:fluxroot/jcpi.git")
					developerConnection.set("scm:git:git@github.com:fluxroot/jcpi.git")
					url.set("https://github.com/fluxroot/jcpi")
				}
			}
		}
	}
	repositories {
		maven {
			val releasesRepository = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
			val snapshotRepository = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
			url = if (version.toString().endsWith("SNAPSHOT")) snapshotRepository else releasesRepository
			credentials {
				val ossrhUsername: String? by project
				val ossrhPassword: String? by project
				if (ossrhUsername != null && ossrhPassword != null) {
					username = ossrhUsername
					password = ossrhPassword
				}
			}
		}
	}
}

signing {
	val signingKeyId: String? by project
	val signingKey: String? by project
	val signingPassword: String? by project
	if (signingKeyId != null && signingKey != null && signingPassword != null) {
		useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
		sign(publishing.publications["mavenJava"])
	}
}

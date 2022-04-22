plugins {
	`java-library`
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

tasks.test {
	useJUnitPlatform()
}

tasks.jar {
	manifest {
		attributes("Automatic-Module-Name" to "com.fluxchess.jcpi")
	}
}

import com.fluxchess.gladius.build.plugin.artifact.JavaArtifactPlugin
import com.fluxchess.gladius.build.plugin.ci.CiPlugin
import com.fluxchess.gladius.build.plugin.versioning.VersioningExtension
import com.fluxchess.gladius.build.plugin.versioning.VersioningPlugin

description = "Java Chess Protocol Interface"
group = "com.fluxchess.jcpi"
version = "2.0.0"

repositories {
	jcenter()
}

apply<CiPlugin>()
apply<VersioningPlugin>()
apply<JavaArtifactPlugin>()

plugins {
	`build-scan`
	`java-library`
}

buildScan {
	termsOfServiceUrl = "https://gradle.com/terms-of-service"
	termsOfServiceAgree = "yes"
}

dependencies {
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.4.1")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.4.1")
	testImplementation("org.assertj:assertj-core:3.12.2")
	testImplementation("org.slf4j:slf4j-log4j12:1.7.26")
}

java {
	sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.named<Test>("test") {
	useJUnitPlatform()
}

tasks.named<Jar>("jar").configure {
	manifest {
		attributes(
				"Automatic-Module-Name" to "com.fluxchess.jcpi",
				"Implementation-Title" to project.name,
				"Implementation-Version" to project.the<VersioningExtension>().version,
				"Build-Number" to project.the<VersioningExtension>().buildNo,
				"Commit-Id" to project.the<VersioningExtension>().commitId)
	}
}

val distTask = tasks.register<Zip>("dist") {
	into("${project.name}-${project.the<VersioningExtension>().version}") {
		from("README.md")
		from("LICENSE")
		from("NOTICE")

		from(tasks.named<Jar>("jar"))
		from(tasks.named<Jar>("sourcesJar"))
		from(tasks.named<Jar>("javadocJar"))

		from("src/dist/engine-interface.txt")
	}
}

artifacts {
	archives(tasks.named<Zip>("dist"))
}

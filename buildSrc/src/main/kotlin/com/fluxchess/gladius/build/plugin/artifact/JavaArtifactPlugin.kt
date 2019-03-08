package com.fluxchess.gladius.build.plugin.artifact

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getPlugin
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType

class JavaArtifactPlugin : Plugin<Project> {

	override fun apply(project: Project) {
		project.plugins.withType(JavaPlugin::class) {
			createJavadocJarTask(project)
			createSourcesJarTask(project)
			createTestsJarTask(project)
		}
	}

	private fun createJavadocJarTask(project: Project) {
		val javadocJar = project.tasks.register<Jar>("javadocJar") {
			dependsOn(JavaPlugin.JAVADOC_TASK_NAME)
			archiveClassifier.set("javadoc")
			from(project.tasks.named(JavaPlugin.JAVADOC_TASK_NAME))
		}
		project.artifacts.add("archives", javadocJar)
	}

	private fun createSourcesJarTask(project: Project) {
		val sourcesJar = project.tasks.register<Jar>("sourcesJar") {
			dependsOn(JavaPlugin.CLASSES_TASK_NAME)
			archiveClassifier.set("sources")
			val convention = project.convention.getPlugin(JavaPluginConvention::class)
			from(convention.sourceSets["main"].allSource)
		}
		project.artifacts.add("archives", sourcesJar)
	}

	private fun createTestsJarTask(project: Project) {
		val testsJar = project.tasks.register<Jar>("testsJar") {
			dependsOn(JavaPlugin.TEST_CLASSES_TASK_NAME)
			archiveClassifier.set("tests")
			val convention = project.convention.getPlugin(JavaPluginConvention::class)
			from(convention.sourceSets["test"].output)
		}
		project.artifacts.add("archives", testsJar)
	}
}

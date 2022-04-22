/*
 * Copyright 2007-2022 The Java Chess Protocol Interface Project Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fluxchess.gladius.build.plugin.versioning

import com.fluxchess.gladius.build.plugin.ci.CiExtension
import com.fluxchess.gladius.build.plugin.ci.CiPlugin
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType

class VersioningPlugin : Plugin<Project> {

	override fun apply(project: Project) {
		val versioningExtension = createExtension(project)

		project.plugins.withType(CiPlugin::class) {
			val ciExtension = project.extensions.getByType(CiExtension::class)
			if (ciExtension.buildingOnCi) {
				project.version = "${versioningExtension.version}-" +
						"${versioningExtension.buildNo}.${versioningExtension.abbreviatedId}"
			} else {
				project.version = "${versioningExtension.version}-SNAPSHOT"
			}

			if (project == project.rootProject) {
				project.logger.lifecycle("Building ${project.name} ${project.version}")
			}
		}
	}

	private fun createExtension(project: Project): VersioningExtension {
		val fileRepository = FileRepositoryBuilder().readEnvironment().findGitDir(project.projectDir).build()
		fileRepository.use { repository ->
			val buildNo = System.getenv("CIRCLE_BUILD_NUM") ?: ""
			val commitId = getCommitId(repository)
			val abbreviatedCommitId = getAbbreviatedCommitId(repository)
			return project.extensions.create(
					"versioning", VersioningExtension::class, project.version, buildNo, commitId, abbreviatedCommitId)
		}
	}

	private fun getCommitId(repository: Repository): String {
		val head = repository.resolve("HEAD")
		return head.name()
	}

	private fun getAbbreviatedCommitId(repository: Repository): String {
		val head = repository.resolve("HEAD")
		return repository.newObjectReader().use { it.abbreviate(head).name() }
	}
}

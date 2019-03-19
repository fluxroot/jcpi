/*
 * Copyright 2007-2019 The Java Chess Protocol Interface Project Authors
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
package com.fluxchess.gladius.build.plugin.ci

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

class CiPlugin : Plugin<Project> {

	override fun apply(project: Project) {
		val extension = project.extensions.create("ci", CiExtension::class, buildingOnCi())
		if (project == project.rootProject && extension.buildingOnCi) {
			project.logger.lifecycle("Building on CI.")
		}
	}

	private fun buildingOnCi(): Boolean {
		return System.getenv("CI") != null
	}
}

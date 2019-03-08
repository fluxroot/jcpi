/*
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
package com.fluxchess.gladius.build.plugin.buildfile

import org.gradle.api.Plugin
import org.gradle.api.initialization.ProjectDescriptor
import org.gradle.api.initialization.Settings

class BuildFilePlugin : Plugin<Settings> {

	override fun apply(settings: Settings) {
		settings.gradle.settingsEvaluated {
			settings.rootProject.children.forEach { configureProject(it) }
		}
	}

	private fun configureProject(project: ProjectDescriptor) {
		project.buildFileName = "${project.name}.gradle.kts"
		project.children.forEach { configureProject(it) }
	}
}

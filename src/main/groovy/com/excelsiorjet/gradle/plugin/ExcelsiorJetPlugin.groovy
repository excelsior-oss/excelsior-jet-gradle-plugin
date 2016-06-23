/*
 * Copyright (c) 2016 Excelsior LLC.
 *
 *  This file is part of Excelsior JET Gradle Plugin.
 *
 *  Excelsior JET Maven Plugin is free software:
 *  you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Excelsior JET Maven Plugin is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Excelsior JET Gradle Plugin.
 *  If not, see <http://www.gnu.org/licenses/>.
 *
*/
package com.excelsiorjet.gradle.plugin

import com.excelsiorjet.api.tasks.JetProject
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.LogLevel
import org.gradle.jvm.tasks.Jar

/**
 * Excelsior JET Gradle Plugin implementation class.
 *
 * Provides Gradle users with an easy way to compile their applications down to optimized native Windows, OS X,
 * or Linux executables with Excelsior JET.
 *
 * Tasks provided by the plugin:
 * <ul>
 *     <li>jetBuild - builds Java (JVM) applications with Excelsior JET.</li>
 * </ul>
 *
 * @author Aleksey Zhidkov
 */
class ExcelsiorJetPlugin implements Plugin<Project> {


    @Override
    void apply(Project target) {
        target.getLogging().captureStandardError(LogLevel.INFO)
        def log = new GradleLog(target.getLogger())
        JetProject.configureEnvironment(log, ResourceBundle.getBundle("GradleStrings", Locale.ENGLISH))

        target.getExtensions().create(ExcelsiorJetExtension.EXTENSION_NAME, ExcelsiorJetExtension)

        def jetBuild = target.tasks.create("jetBuild", JetBuildTask)
        jetBuild.dependsOn(":jar", ":test")

        addJetBuildConventions(target)
    }

    private static void addJetBuildConventions(Project project) {
        ExcelsiorJetExtension extension = project.extensions.findByName(ExcelsiorJetExtension.EXTENSION_NAME) as ExcelsiorJetExtension
        extension.conventionMapping.version = { project.version.toString() }
        extension.conventionMapping.excelsiorJetPackaging = { JetProject.ZIP }
        extension.conventionMapping.artifactName = { getArchiveName(project.tasks.getByPath(':jar')) }
        extension.conventionMapping.mainJar = {
            new File(project.tasks.getByPath(':jar').archivePath as String)
        }
        extension.conventionMapping.jetHome = { System.getProperty("jet.home") }
        extension.conventionMapping.jetResourcesDir = {
            new File("${project.projectDir}/src/main/jetresources" as String)
        }
        extension.conventionMapping.groupId = { project.group }
    }

    private static String getArchiveName(Jar jarTask) {
        if (jarTask.extension != null && !jarTask.extension.isEmpty()) {
            jarTask.archiveName.substring(0, jarTask.archiveName.lastIndexOf('.'))
        } else {
            jarTask.archiveName
        }
    }
}

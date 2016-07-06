/*
 * Copyright (c) 2016 Excelsior LLC.
 *
 *  This file is part of Excelsior JET Gradle Plugin.
 *
 *  Excelsior JET Gradle Plugin is free software:
 *  you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Excelsior JET Gradle Plugin is distributed in the hope that it will be useful,
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

import com.excelsiorjet.api.tasks.ApplicationType
import com.excelsiorjet.api.tasks.JetProject
import com.excelsiorjet.api.util.Txt
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.ProjectConfigurationException
import org.gradle.api.Task
import org.gradle.api.logging.LogLevel

/**
 * Excelsior JET Gradle Plugin implementation class.
 *
 * Provides Gradle users with an easy way to compile their applications down to optimized native Windows, OS X,
 * or Linux executables with Excelsior JET.
 *
 * Tasks provided by the plugin:
 * <ul>
 *     <li>jetBuild - builds Java (JVM) applications with Excelsior JET.</li>
 *     <li>jetTestRun - executes test run of Java (JVM) applications with Excelsior JET.</li>
 * </ul>
 *
 * @author Aleksey Zhidkov
 */
class ExcelsiorJetPlugin implements Plugin<Project> {

    private ApplicationType appType
    private ExcelsiorJetExtension extension

    @Override
    void apply(Project target) {
        target.getLogging().captureStandardError(LogLevel.INFO)
        def log = new GradleLog(target.getLogger())
        JetProject.configureEnvironment(log, ResourceBundle.getBundle("com.excelsiorjet.gradle.plugin.GradleStrings"))

        extension = target.getExtensions().create(ExcelsiorJetExtension.EXTENSION_NAME, ExcelsiorJetExtension)

        def archiveTaskName
        if (target.getPlugins().hasPlugin('war')) {
            appType = ApplicationType.TOMCAT
            archiveTaskName = 'war'
        } else if (target.getPlugins().hasPlugin('java')) {
            appType = ApplicationType.PLAIN
            archiveTaskName = 'jar'
        } else {
            throw new ProjectConfigurationException(Txt.s("ExcelsiorJetGradlePlugin.ProjectGroupIsNotSet"), null)
        }

        def jetTestRun = target.tasks.create("jetTestRun", JetTestRunTask)
        jetTestRun.dependsOn(taskPath(target, archiveTaskName), taskPath(target, "test"))

        def jetBuild = target.tasks.create("jetBuild", JetBuildTask)
        jetBuild.dependsOn(taskPath(target, archiveTaskName), taskPath(target, "test"))

        addJetBuildConventions(target)
    }

    private void addJetBuildConventions(Project project) {
        extension.conventionMapping.version = {
            if (project.version.toString() == "unspecified") {
                project.logger.warn(Txt.s("ExcelsiorJetGradlePlugin.ProjectVersionIsNotSet"))
                extension.version = "1.0"
                return extension.version
            } else {
                return project.version.toString()
            }
        }
        extension.conventionMapping.packaging = { JetProject.ZIP }
        extension.conventionMapping.artifactName = { getArchiveName(project.tasks.getByPath(taskPath(project, "jar"))) }
        extension.conventionMapping.mainJar = {
            new File(project.tasks.getByPath(taskPath(project, "jar")).archivePath as String)
        }
        extension.conventionMapping.mainWar = {
            new File(project.tasks.getByPath(taskPath(project, "war")).archivePath as String)
        }
        extension.conventionMapping.jetHome = { System.getProperty("jet.home") }
        extension.conventionMapping.jetResourcesDir = {
            new File("${project.projectDir}/src/main/jetresources" as String)
        }
        extension.conventionMapping.groupId = {
            if (project.group.toString().isEmpty() && (extension.vendor ?: "").isEmpty()) {
                throw new ProjectConfigurationException(Txt.s("ExcelsiorJetGradlePlugin.ProjectGroupIsNotSet"), null)
            }

            if (project.group.toString().isEmpty()) {
                return extension.vendor
            } else {
                return project.group.toString()
            }
        }

        extension.appType = appType

    }

    private static String getArchiveName(Task jarTask) {
        if (jarTask.extension != null && !jarTask.extension.isEmpty()) {
            jarTask.archiveName.substring(0, jarTask.archiveName.lastIndexOf('.'))
        } else {
            jarTask.archiveName
        }
    }

    /**
     * Resolves task name in the given project into valid full task name string (with project prefix, for subprojects).
     * Tasks in root project should not be referenced with root project name prefix (e.g ":jar"),
     * while task in subprojects should be referenced with project name prefix (e.g. ":subproject:jar").
     */
    private static String taskPath(Project target, String taskName) {
        if (target.rootProject == target) {
            return ":$taskName"
        } else {
            return ":${target.name}:$taskName"
        }
    }
}

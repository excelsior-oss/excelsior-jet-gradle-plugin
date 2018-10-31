/*
 * Copyright (c) 2016-2017 Excelsior LLC.
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

import com.excelsiorjet.api.tasks.config.ApplicationType
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
 *     <li>jetProfile - collects execution profile.</li>
 *     <li>jetRun - executes compiled application.</li>
 *     <li>jetClean - cleans up Project database.</li>
 * </ul>
 *
 * @author Aleksey Zhidkov
 * @author Nikita Lipsky
 */
class ExcelsiorJetPlugin implements Plugin<Project> {

    private boolean isWar
    private boolean isSpringBoot
    private ExcelsiorJetExtension extension

    @Override
    void apply(Project target) {
        target.getLogging().captureStandardError(LogLevel.INFO)
        def log = new GradleLog(target.getLogger())
        JetProject.configureEnvironment(log, ResourceBundle.getBundle("com.excelsiorjet.gradle.plugin.GradleStrings"))

        extension = target.getExtensions().create(ExcelsiorJetExtension.EXTENSION_NAME, ExcelsiorJetExtension)

        Task archiveTask
        isSpringBoot = false
        if (target.getPlugins().hasPlugin('war')) {
            isWar = true
            if (!target.getPlugins().hasPlugin('org.springframework.boot')) {
              archiveTask = target.tasks.getByName('war')
            } else {
              isSpringBoot = true;
              archiveTask = target.tasks.getByName('bootWar')
            }
        } else if (target.getPlugins().hasPlugin('org.springframework.boot')) {
            isWar = false
            isSpringBoot = true
            archiveTask = target.tasks.getByName('bootJar')
        } else if (target.getPlugins().hasPlugin('java')) {
            isWar = false
            archiveTask = target.tasks.getByName('jar')
        } else {
            throw new ProjectConfigurationException(Txt.s("ExcelsiorJetGradlePlugin.NoJarOrWarPluginsFound"), null)
        }

        Task testTask = target.tasks.getByName('test')

        def jetTestRun = target.tasks.create("jetTestRun", JetTestRunTask)
        jetTestRun.dependsOn(archiveTask, testTask)

        def jetBuild = target.tasks.create("jetBuild", JetBuildTask)
        jetBuild.dependsOn(archiveTask, testTask)

        def jetProfile = target.tasks.create("jetProfile", JetProfileTask)
        jetProfile.dependsOn(archiveTask, testTask)

        def jetRun = target.tasks.create("jetRun", JetRunTask)
        jetRun.dependsOn(archiveTask, testTask)

        target.tasks.create("jetStop", JetStopTask)

        target.tasks.create("jetClean", JetCleanTask)

        addJetBuildConventions(target, archiveTask)

    }

    private void addJetBuildConventions(Project project, Task archiveTask) {
        extension.conventionMapping.version = {
            if (project.version.toString() == "unspecified") {
                project.logger.warn(Txt.s("ExcelsiorJetGradlePlugin.ProjectVersionIsNotSet"))
                extension.version = "1.0"
                return extension.version
            } else {
                return project.version.toString()
            }
        }
        extension.conventionMapping.artifactName = { getArchiveName(archiveTask) }
        if (!isWar) {
            extension.conventionMapping.mainJar = {
                archiveTask.archivePath
            }
        } else  {
            extension.conventionMapping.mainWar = {
                archiveTask.archivePath
            }
        }
        extension.conventionMapping.jetHome = { System.getProperty("jet.home") }

        extension.jetResourcesDir = project.file("src/main/jetresources")

        extension.conventionMapping.groupId = {
            if (project.group.toString().isEmpty() && (extension.vendor ?: "").isEmpty()) {
                return setGroupId(project)
            }

            if (project.group.toString().isEmpty()) {
                return extension.vendor
            } else {
                return project.group.toString()
            }
        }

        extension.conventionMapping.appType = {
            if (isWar && isSpringBoot) {
                throw new ProjectConfigurationException(Txt.s("JetProject.NoAppType.Failure"), null)
            }
            isWar ? ApplicationType.TOMCAT.toString(): ApplicationType.PLAIN.toString()
        }
    }

    private String setGroupId(Object project) {
        // see
        // https://docs.gradle.org/current/javadoc/org/gradle/api/file/SourceDirectorySet.html
        // https://docs.gradle.org/current/javadoc/org/gradle/api/tasks/SourceSet.html
        // https://docs.gradle.org/current/userguide/java_plugin.html#N1503E
        // https://docs.gradle.org/current/dsl/org.gradle.api.tasks.SourceSet.html

        // Get all Gradle SourceSet objects, that contains source code
        def allCodeSourceSets = project.sourceSets.main.allSource.source.findAll({
            // pattern: "main <<language> source|resources>",
            // eg: "main Java source", "main resources"
            (it.name as String).matches('^main \\w+ source$') || it.name == "java"
        })

        // Map Gradle SourceSet objects to sources root
        List<File> allCodeSrcDirs = allCodeSourceSets.collect({ it.srcDirs }).flatten() as List<File>
        def guessedGroups = allCodeSrcDirs.collect { guessGroupId(it) }
        def guessedGroup = guessedGroups.find { it != null }
        if (guessedGroup != null) {
            project.logger.warn(Txt.s("ExcelsiorJetGradlePlugin.ProjectGroupIsGuessed", guessedGroup))
            extension.groupId = guessedGroup
        } else {
            extension.groupId = ""
        }
        return extension.groupId
    }

    private static String getArchiveName(Task jarTask) {
        if (jarTask.extension != null && !jarTask.extension.isEmpty()) {
            jarTask.archiveName.substring(0, jarTask.archiveName.lastIndexOf('.'))
        } else {
            jarTask.archiveName
        }
    }

    /**
     * Tries to guess groupId by given sources root.
     * If given sources root is not directory or do not contain subdirectories returns {@code null}.
     * Otherwise group id is formed from first subdirectory and, if exists, first subdirectory if that directory names joined with '.'.
     */
    private static String guessGroupId(File sourcesRoot) {
        if (!sourcesRoot.isDirectory()) {
            return null
        }

        def firstLevelDirs = sourcesRoot.listFiles({ File f -> f.isDirectory() } as FileFilter)
        if (firstLevelDirs == null || firstLevelDirs.size() == 0) {
            return null
        }
        def firstDir = firstLevelDirs.first()

        def secondLevelDirs = firstDir.listFiles({ File f -> f.isDirectory() } as FileFilter)
        if (secondLevelDirs == null || secondLevelDirs.size() == 0) {
            return firstDir.name
        }
        def secondDir = secondLevelDirs.first()

        return "${firstDir.name}.${secondDir.name}"
    }
}

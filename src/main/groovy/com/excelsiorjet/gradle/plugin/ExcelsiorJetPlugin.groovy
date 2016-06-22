package com.excelsiorjet.gradle.plugin

import com.excelsiorjet.api.log.Log
import com.excelsiorjet.api.tasks.ApplicationType
import com.excelsiorjet.api.tasks.ClasspathEntry
import com.excelsiorjet.api.tasks.JetProject
import com.excelsiorjet.api.util.Txt
import org.gradle.api.GradleScriptException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.LogLevel

import java.util.stream.Stream

/**
 * Excelsior JET plugin. Allows to compile project into native executable and pack it to native for platform package.
 * Adds tasks:
 * <ul>
 *     <li>jetBuild - compiles project into native executable and packs it into package of specified type</li>
 * </ul>
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
        extension.conventionMapping.jetOutputDir = { new File("${project.buildDir}/jet") }
        extension.conventionMapping.excelsiorJetPackaging = { JetProject.ZIP }
        extension.conventionMapping.finalName = { "${project.name}-${project.version}".toString() }
        extension.conventionMapping.mainJar = {
            new File(project.tasks.getByPath(':jar').archivePath as String)
        }
        extension.conventionMapping.packageFilesDir = {
            project.projectDir.toPath().resolve('rc/main/jetresources/packagefiles').toFile()
        }
        extension.conventionMapping.jetHome = { System.getProperty("jet.home") }
        extension.conventionMapping.jetResourcesDir = {
            new File("${project.projectDir}/src/main/jetresources" as String)
        }
        extension.conventionMapping.groupId = { project.group }
    }
}

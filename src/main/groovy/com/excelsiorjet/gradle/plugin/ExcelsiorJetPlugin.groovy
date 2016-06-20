package com.excelsiorjet.gradle.plugin

import com.excelsiorjet.api.log.Log
import com.excelsiorjet.api.tasks.ApplicationType
import com.excelsiorjet.api.tasks.ClasspathEntry
import com.excelsiorjet.api.tasks.JetProject
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
        Log.logger = new GradleLog(target.getLogger())

        target.getExtensions().create(ExcelsiorJetExtension.EXTENSION_NAME, ExcelsiorJetExtension)

        def jetBuild = target.tasks.create("jetBuild", JetBuildTask)
        jetBuild.dependsOn(":jar", ":test")

        addJetBuildConventions(target)
    }

    private void addJetBuildConventions(Project project) {
        project.tasks.withType(JetBuildTask) {
            ExcelsiorJetExtension extension = project.extensions.findByName(ExcelsiorJetExtension.EXTENSION_NAME) as ExcelsiorJetExtension
            conventionMapping.artifactId = { extension.artifactId ?: project.name }
            conventionMapping.version = { extension.version ?: project.version.toString() }
            conventionMapping.jetOutputDir = { extension.jetOutputDir ?: new File("${project.buildDir}/jet") }
            conventionMapping.dependencies = { getDependencies(project) }
            conventionMapping.buildDir = {
                extension.buildDir ?: project.buildDir.toPath().resolve("jet/build").toFile()
            }
            conventionMapping.excelsiorJetPackaging = { extension.excelsiorJetPackaging ?: JetProject.ZIP }
            conventionMapping.finalName = { extension.finalName ?: "${getArtifactId()}-${getVersion()}".toString() }
            conventionMapping.mainClass = { extension.mainClass }
            conventionMapping.mainJar = {
                extension.mainJar ?: new File(project.tasks.getByPath(':jar').archivePath as String)
            }
            conventionMapping.outputName = { extension.outputName }
            conventionMapping.packageFilesDir = {
                extension.packageFilesDir ?: project.projectDir.toPath().resolve('rc/main/jetresources/packagefiles').toFile()
            }
            conventionMapping.appType = { extension.appType ?: ApplicationType.PLAIN }
            conventionMapping.jetHome = { extension.jetHome ?: System.getProperty("jet.home") }
        }
    }

    static Stream<ClasspathEntry> getDependencies(Project project) {
        def configuration = project.configurations.getByName("compile")
        return configuration.getDependencies().stream().map {
            def depFiles = configuration.files(it)
            if (depFiles.size() > 0) {
                new ClasspathEntry(depFiles.first(), project.group.equals(it.group))
            }
        }
    }
}

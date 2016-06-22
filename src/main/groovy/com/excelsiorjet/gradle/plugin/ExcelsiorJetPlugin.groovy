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
        def log= new GradleLog(target.getLogger())
        JetProject.configureEnvironment(log, ResourceBundle.getBundle("GradleStrings", Locale.ENGLISH))

        target.getExtensions().create(ExcelsiorJetExtension.EXTENSION_NAME, ExcelsiorJetExtension)

        def jetBuild = target.tasks.create("jetBuild", JetBuildTask)
        jetBuild.dependsOn(":jar", ":test")

        addJetBuildConventions(target)
    }

    private void addJetBuildConventions(Project project) {
        project.tasks.withType(JetBuildTask) {
            ExcelsiorJetExtension extension = project.extensions.findByName(ExcelsiorJetExtension.EXTENSION_NAME) as ExcelsiorJetExtension
            conventionMapping.version = { extension.version ?: project.version.toString() }
            conventionMapping.jetOutputDir = { extension.jetOutputDir ?: new File("${project.buildDir}/jet") }
            conventionMapping.dependencies = { getDependencies(project) }
            conventionMapping.excelsiorJetPackaging = { extension.excelsiorJetPackaging ?: JetProject.ZIP }
            conventionMapping.finalName = { extension.finalName ?: "${project.name}-${project.version}".toString() }
            conventionMapping.mainClass = { extension.mainClass }
            conventionMapping.mainJar = {
                extension.mainJar ?: new File(project.tasks.getByPath(':jar').archivePath as String)
            }
            conventionMapping.outputName = { extension.outputName }
            conventionMapping.packageFilesDir = {
                extension.packageFilesDir ?: project.projectDir.toPath().resolve('rc/main/jetresources/packagefiles').toFile()
            }
            conventionMapping.jetHome = { extension.jetHome ?: System.getProperty("jet.home") }
            conventionMapping.jetResourcesDir = { new File("${project.projectDir }/src/main/jetresources" as String) }
            conventionMapping.groupId = { project.group }
        }
    }

    static List<ClasspathEntry> getDependencies(Project project) {
        def configuration = project.configurations.getByName("compile")
        return configuration.getDependencies().collect {
            // due to bug in support of maven relocation in gradle, dependency may be resolved into multiple files
            // https://issues.gradle.org/browse/GRADLE-2812
            // https://issues.gradle.org/browse/GRADLE-3301
            // https://discuss.gradle.org/t/oddity-in-the-output-of-dependencyinsight-task/7553/12
            // https://discuss.gradle.org/t/warning-after-gradle-update-1-9-with-pmd-plugin/1731/3
            def depFiles = configuration.files(it)
            if (depFiles.size() == 0) {
                throw new GradleScriptException(Txt.s("ExcelsiorJetGradlePlugin.NoFilesForDependency", it), null)
            } else if (depFiles.size() > 1) {
                Log.logger.warn(Txt.s("ExcelsiorJetGradlePlugin.MultipleFilesForDependency", it, depFiles, depFiles.first()))
            }
            new ClasspathEntry(depFiles.first(), project.group.equals(it.group))
        }
    }
}

package com.excelsiorjet.gradle.plugin

import com.excelsiorjet.api.tasks.ApplicationType
import com.excelsiorjet.api.tasks.ClasspathEntry
import com.excelsiorjet.api.tasks.JetProject
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import java.util.stream.Stream

/**
 * Task that compiles project into native executable and packs it into package of specified type
 *
 * @see ExcelsiorJetExtension
 */
class JetBuildTask extends DefaultTask {

    List<ClasspathEntry> dependencies
    String excelsiorJetPackaging
    String finalName
    File jetOutputDir
    String mainClass
    File mainJar
    String outputName
    File packageFilesDir
    String version
    String jetHome
    File jetResourcesDir
    String groupId

    @TaskAction
    def jetBuild() {
        def jetProject = new JetProject(project.name, getGroupId(), getVersion(), ApplicationType.PLAIN, project.buildDir, getJetResourcesDir())

        // getters should be used to fallback into convention mapping magic, when field is not set
        jetProject.dependencies(getDependencies())
                .excelsiorJetPackaging(getExcelsiorJetPackaging())
                .artifactName(getFinalName())
                .jetOutputDir(getJetOutputDir())
                .mainClass(getMainClass())
                .mainJar(getMainJar())
                .outputName(getOutputName())
                .packageFilesDir(getPackageFilesDir())
                .version(getVersion())
                .jetHome(getJetHome())

        new com.excelsiorjet.api.tasks.JetBuildTask(jetProject).execute()
    }

}

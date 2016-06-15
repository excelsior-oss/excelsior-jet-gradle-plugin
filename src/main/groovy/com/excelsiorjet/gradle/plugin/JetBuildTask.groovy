package com.excelsiorjet.gradle.plugin

import com.excelsiorjet.api.tasks.JetProject
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class JetBuildTask extends DefaultTask {

    ExcelsiorJetExtension excelsiorJetExtension

    @TaskAction
    def jetBuild() {
        def jetProject = new JetProject()

        jetProject.artifactId(excelsiorJetExtension.artifactId())
        jetProject.dependencies(excelsiorJetExtension.artifacts)
        jetProject.buildDir(excelsiorJetExtension.buildDir())
        jetProject.excelsiorJetPackaging(excelsiorJetExtension.excelsiorJetPackaging())
        jetProject.finalName(excelsiorJetExtension.finalName())
        jetProject.jetOutputDir(excelsiorJetExtension.jetOutputDir())
        jetProject.mainClass(excelsiorJetExtension.mainClass)
        jetProject.mainJar(excelsiorJetExtension.mainJar())
        jetProject.outputName(excelsiorJetExtension.outputName())
        jetProject.packageFilesDir(excelsiorJetExtension.packageFilesDir())
        jetProject.packaging(excelsiorJetExtension.packaging())
        jetProject.version(excelsiorJetExtension.version())

        new com.excelsiorjet.api.tasks.JetBuildTask(jetProject).execute()
    }

}

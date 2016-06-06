package com.excelsiorjet.gradle.plugin

import com.excelsiorjet.api.JetTask
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class JetBuildTask extends DefaultTask {

    ExcelsiorJetExtension excelsiorJetExtension

    @TaskAction
    def jetBuild() {
        new JetTask(excelsiorJetExtension).execute()
    }

}

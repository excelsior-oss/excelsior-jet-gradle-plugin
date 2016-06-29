package com.excelsiorjet.gradle.plugin

import com.excelsiorjet.api.tasks.JetProject
import org.gradle.api.tasks.TaskAction

class TestRunTask extends AbstractJetTask {

    @TaskAction
    def jetBuild() {
        JetProject jetProject = createJetProject()
        new com.excelsiorjet.api.tasks.TestRunTask(jetProject).execute()
    }

}

package com.excelsiorjet.gradle.plugin

import com.excelsiorjet.api.ExcelsiorJet
import com.excelsiorjet.api.tasks.JetProject
import org.gradle.api.tasks.TaskAction

/**
 * Cleans up Excelsior JET Project database (PDB) directory for the current project.
 */
class JetCleanTask extends AbstractBuildTask {

    @TaskAction
    def jetClean() {
        ExcelsiorJet excelsiorJet = new ExcelsiorJet(jetHome)
        JetProject jetProject = createJetProject()
        new com.excelsiorjet.api.tasks.JetCleanTask(jetProject, excelsiorJet).execute()
    }
}

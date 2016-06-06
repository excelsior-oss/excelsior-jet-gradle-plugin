package com.excelsiorjet.gradle.plugin

import com.excelsiorjet.api.log.AbstractLog
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.LogLevel

class ExcelsiorJetPlugin implements Plugin<Project> {

    @Override
    void apply(Project target) {
        def ext = target.getExtensions().create("excelsiorJet", ExcelsiorJetExtension)
        ext.project = target
        def jetBuild = target.tasks.create("jetBuild", JetBuildTask)
        jetBuild.dependsOn(":jar", ":test")
        jetBuild.excelsiorJetExtension = ext

        target.getLogging().captureStandardError(LogLevel.INFO)
        AbstractLog.instance = new GradleLog(target.getLogger())
    }

}

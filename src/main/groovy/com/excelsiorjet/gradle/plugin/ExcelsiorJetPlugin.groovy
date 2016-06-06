package com.excelsiorjet.gradle.plugin

import com.excelsiorjet.api.AbstractLog
import org.gradle.api.Plugin
import org.gradle.api.Project

class ExcelsiorJetPlugin implements Plugin<Project> {

    @Override
    void apply(Project target) {
        def ext = target.getExtensions().create("excelsiorJet", ExcelsiorJetExtension)
        ext.project = target
        def jetBuild = target.tasks.create("jetBuild", JetBuildTask)
        jetBuild.dependsOn(":jar", ":test")
        jetBuild.excelsiorJetExtension = ext

        AbstractLog.instance = new GradleLog()
    }

}

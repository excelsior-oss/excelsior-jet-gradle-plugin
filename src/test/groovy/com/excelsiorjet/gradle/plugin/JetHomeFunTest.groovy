package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.UnexpectedBuildFailure

class JetHomeFunTest extends BaseFunTest implements HelloWorldProject {

    def "jetHome plugin property checking"() {
        when:
        runGradle('jetBuild')

        then:
        UnexpectedBuildFailure ex = thrown()
        ex.getMessage().contains("com.excelsiorjet.api.JetHomeException: The jetHome plugin parameter points to \"jetHome\", which is not an Excelsior JET installation directory.")

    }

    public String testProjectDir() {
        return "39-jethome"
    }

}

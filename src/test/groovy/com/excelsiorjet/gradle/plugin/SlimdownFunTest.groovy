package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome
import spock.lang.IgnoreIf

class SlimdownFunTest extends BaseFunTest implements HelloWorldProject {

    @IgnoreIf({!slimDownSupported})
    def "test java runtime slimdown"() {
        setup:
        File rt0Jar = new File(basedir, "build/jet/app/rt/lib/rt-0.jar")

        when:
        def result = runGradle('clean', 'jetTestRun', 'jetBuild')

        then:
        rt0Jar.exists()

        result.task(":clean").outcome == TaskOutcome.SUCCESS || result.task(":clean").outcome == TaskOutcome.UP_TO_DATE
        result.task(":jetTestRun").outcome == TaskOutcome.SUCCESS
        result.task(":jetBuild").outcome == TaskOutcome.SUCCESS
    }

    @Override
    protected String testProjectDir() {
        return "09-slimdown"
    }

}

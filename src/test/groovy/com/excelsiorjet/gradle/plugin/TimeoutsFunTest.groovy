package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome
import spock.lang.IgnoreIf

class TimeoutsFunTest extends BaseFunTest implements HelloWorldProject {

    @IgnoreIf({crossCompilation})
    def "tests test run, profile timeouts"() {
        setup:

        when:
        def result = runGradle("jetTestRun", "jetProfile")

        then:
        result.task(":jetTestRun").outcome == TaskOutcome.SUCCESS
        result.task(":jetProfile").outcome == TaskOutcome.SUCCESS
        !(new File(basedir, "build/jet/build/failed").exists())
        !(new File(basedir, "build/jet/appToPofile/failed").exists())
    }

    @Override
    protected String testProjectDir() {
        return "47-timeouts"
    }

}

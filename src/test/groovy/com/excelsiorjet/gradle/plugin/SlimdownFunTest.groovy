package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome

class SlimdownFunTest extends BaseFunTest implements HelloWorldProject {

    def "test java runtime slimdown"() {
        setup:
        File jetRtFiles= new File(basedir, "build/jet/app/rt/jetrt")
        File rt0Jar = new File(basedir, "build/jet/app/rt/lib/rt-0.jar")

        when:
        def result = runGradle('clean', 'jetTestRun', 'jetBuild')

        then:
        (jetRtFiles.listFiles().length > 1 || rt0Jar.exists())

        result.task(":clean").outcome == TaskOutcome.SUCCESS || result.task(":clean").outcome == TaskOutcome.UP_TO_DATE
        result.task(":jetTestRun").outcome == TaskOutcome.SUCCESS
        result.task(":jetBuild").outcome == TaskOutcome.SUCCESS
    }

    @Override
    protected String testProjectDir() {
        return "09-slimdown"
    }

}

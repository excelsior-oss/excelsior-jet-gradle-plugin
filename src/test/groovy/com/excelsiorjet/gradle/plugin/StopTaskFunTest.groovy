package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome
import spock.lang.IgnoreIf

class StopTaskFunTest extends BaseFunTest implements HelloWorldProject {

    protected static final String gradleExec = System.getProperty("gradle.exec")

    @IgnoreIf({crossCompilation || (gradleExec == null) || !PGOSupported})
    def "stops test run, profile, run tasks "() {
        setup:

        when:
        def result = runGradle("jetTestRun", "jetProfile", "jetBuild", "jetRun", "-Dgradle.exec=" + gradleExec)

        then:
        result.task(":jetTestRun").outcome == TaskOutcome.SUCCESS
        result.task(":jetProfile").outcome == TaskOutcome.SUCCESS
        result.task(":jetBuild").outcome == TaskOutcome.SUCCESS
        result.task(":jetRun").outcome == TaskOutcome.SUCCESS
        !(new File(basedir, "build/jet/build/failed").exists())
        !(new File(basedir, "build/jet/app/failed").exists())
        !(new File(basedir, "build/jet/appToPofile/failed").exists())
    }

    @Override
    protected String testProjectDir() {
        return "46-stop-task"
    }

}

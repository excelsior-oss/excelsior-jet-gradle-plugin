package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome
import spock.lang.IgnoreIf

class TestRunFunTest extends BaseFunTest implements HelloWorldProject {

    @IgnoreIf({crossCompilation})
    def "executes test run for project"() {
        setup:
        File startupProfile = new File( basedir, "src/main/jetresources/HelloWorld.startup");
        File reorderFile = new File(basedir, "build/jet/build/HelloWorld_jetpdb/reorder.li")

        when:
        def result = runGradle('jetTestRun', 'jetBuild')

        then:
        startupProfile.exists()
        reorderFile.exists();
        new File(basedir, "build/jet/build/custom.file").exists()
        new File(basedir, "build/jet/build/subdir/subdir.file").exists()

        result.task(":jetTestRun").outcome == TaskOutcome.SUCCESS
        result.task(":jetBuild").outcome == TaskOutcome.SUCCESS
    }

    @Override
    protected String testProjectDir() {
        return "07-testrun"
    }

}

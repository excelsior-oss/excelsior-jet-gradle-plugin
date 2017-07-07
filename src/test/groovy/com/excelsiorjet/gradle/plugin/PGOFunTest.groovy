package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome
import spock.lang.IgnoreIf

class PGOFunTest extends BaseFunTest implements HelloWorldProject {

    @IgnoreIf({!PGOSupported || crossCompilation})
    def "pgo test"() {
        when:
        def zipFile = new File(basedir, "build/jet/${projectName()}"  + ".zip")
        def appToProfileExeFile = new File(basedir, "build/jet/appToProfile/${projectName()}$ext")
        def execProfile = new File( basedir, "src/main/jetresources/Test.jprof");
        def result = runGradle('jetProfile', 'jetBuild')

        then:
        buildExeFile.exists()
        appToProfileExeFile.exists()
        execProfile.exists()
        appExeFile.exists()
        zipFile.exists()

        result.task(":jetProfile").outcome == TaskOutcome.SUCCESS
        result.task(":jetBuild").outcome == TaskOutcome.SUCCESS
    }

    @Override
    String projectName() {
        return "Test"
    }

    public String testProjectDir() {
        return "36-pgo"
    }

}

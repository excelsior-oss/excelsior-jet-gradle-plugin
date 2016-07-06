package com.excelsiorjet.gradle.plugin

class TrialVersionFunTest extends BaseFunTest implements HelloWorldProject {

    def "test trial version configuration"() {
        when:
        runGradle("clean", "jetBuild")

        then:
        new File(basedir, "build/jet/build/HelloWorld_jetpdb/HelloWorld.rsp").text.contains("expire")

        appExeFile.exists()
        cmdOutput(appExeFile).contains("App is expired")
    }

    @Override
    protected String testProjectDir() {
        return "11-trialversion"
    }
}

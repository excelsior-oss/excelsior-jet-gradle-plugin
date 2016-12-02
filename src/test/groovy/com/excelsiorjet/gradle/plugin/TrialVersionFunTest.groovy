package com.excelsiorjet.gradle.plugin

import spock.lang.IgnoreIf

class TrialVersionFunTest extends BaseFunTest implements HelloWorldProject {

    @IgnoreIf({!trialSupported})
    def "test trial version configuration"() {
        when:
        runGradle("clean", "jetBuild")

        then:
        new File(basedir, "build/jet/build/HelloWorld_jetpdb/HelloWorld.rsp").text.contains("expire")

        appExeFile.exists()

        crossCompilation || cmdOutput(appExeFile).contains("App is expired")
    }

    @Override
    protected String testProjectDir() {
        return "11-trialversion"
    }
}

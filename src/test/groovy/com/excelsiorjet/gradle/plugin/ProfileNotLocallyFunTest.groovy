package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome
import spock.lang.IgnoreIf

class ProfileNotLocallyFunTest extends BaseFunTest implements HelloWorldProject {

    @IgnoreIf({!PGOSupported})
    def "profile not locally test"() {
        when:
        def appToProfileZipFile = new File(basedir, "build/jet/appToProfile.zip")
        def appToProfileDir = new File(basedir, "build/jet/appToProfile")
        def appToProfileExeFile = new File(appToProfileDir, "${projectName()}$ext")
        def execProfile = new File( appToProfileDir, "Test.jprof");
        def result = runGradle('jetProfile')

        then:
        appToProfileZipFile.exists()
        appToProfileExeFile.exists()

        if (!crossCompilation) {
            appToProfileExeFile.getAbsolutePath().execute(null, appToProfileDir).text.contains("Time:")
            execProfile.exists()
        }

        result.task(":jetProfile").outcome == TaskOutcome.SUCCESS
    }

    @Override
    String projectName() {
        return "Test"
    }

    public String testProjectDir() {
        return "37-profile-not-locally"
    }

}

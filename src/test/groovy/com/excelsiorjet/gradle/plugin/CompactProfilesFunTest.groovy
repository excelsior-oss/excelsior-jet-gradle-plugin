package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome
import spock.lang.IgnoreIf

class CompactProfilesFunTest extends BaseFunTest implements HelloWorldProject {

    @IgnoreIf({!compactProfilesSupported})
    def "jetBuild task builds simple application packaging with compact3 profile"() {
        when:
        def libManagement = new File(basedir, "build/jet/app/rt/lib/management")
        def libFonts = new File(basedir, "build/jet/app/rt/lib/fonts")
        def result = runGradle('jetBuild')

        then:
        buildExeFile.exists()
        appExeFile.exists()
        libManagement.exists()
        !libFonts.exists()

        result.task(":jetBuild").outcome == TaskOutcome.SUCCESS
    }

    public String testProjectDir() {
        return "26-compactprofiles"
    }

}

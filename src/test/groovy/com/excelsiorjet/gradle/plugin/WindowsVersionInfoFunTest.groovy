package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome
import spock.lang.IgnoreIf

@IgnoreIf({!windowsVersionInfoSupported})
class WindowsVersionInfoFunTest extends BaseFunTest implements HelloWorldProject {

    def "jetBuild task builds simple application with windows version info"() {
        when:
        def versionRes = new File(basedir, "build/jet/build/HelloWorld_jetpdb/version.rc");
        def result = runGradle('jetBuild')

        then:
        versionRes.exists()
        String versionResText = versionRes.text;
        versionResText.contains("MyCompany")
        versionResText.contains("MyProduct")
        versionResText.contains("4.3.2.1")
        versionResText.contains("MyCopyright")
        versionResText.contains("My description")

        result.task(":jetBuild").outcome == TaskOutcome.SUCCESS
    }

    String testProjectDir() {
        return "28-windows-version-info"
    }

}

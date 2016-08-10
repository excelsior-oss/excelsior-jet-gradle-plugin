package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome

class FullStackTraceSplashFromManfiestChineseLocaleFunTest extends BaseFunTest implements HelloWorldProject {

    def "jetBuild task builds application with full stack trace, splash from manifest, chinese locale"() {
        when:
        def result = runGradle('jetBuild')

        then:
        buildExeFile.exists()
        appExeFile.exists()

        //check lines in stack trace
        checkStdOutContains(appExeFile, "Hello World")

        //check very-aggressive inline
        new File(basedir, "build/jet/build/HelloWorld.prj").text.contains("-inlinetolimit=2000")

        //check splash in resources
        new File(basedir, "build/jet/build/HelloWorld_jetpdb/tmpres/splash.png").exists()

        //check chinese locale
        new File(basedir, "build/jet/app/rt/jetrt").list().any{it.contains("XLCH")}

        //check no european locale
        !new File(basedir, "build/jet/app/rt/jetrt").list().any{it.contains("XLEU")}

        result.task(":jetBuild").outcome == TaskOutcome.SUCCESS
    }

    public String testProjectDir() {
        return "17-fullstacktrace-splashfrommanifest-chineselocale"
    }

}

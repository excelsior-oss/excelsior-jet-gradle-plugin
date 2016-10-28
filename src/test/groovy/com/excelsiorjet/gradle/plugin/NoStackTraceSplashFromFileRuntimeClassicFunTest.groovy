package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome

class NoStackTraceSplashFromFileRuntimeClassicFunTest extends BaseFunTest implements HelloWorldProject {

    def "jetBuild task builds application with no stack trace, splash from file, -pack=all"() {
        when:
        def result = runGradle('jetBuild')

        then:
        buildExeFile.exists()
        appExeFile.exists()

        //check Unknown in stack trace
        checkStdOutContains("Hello World", appExeFile)

        //check tiny-methods only inline
        new File(basedir, "build/jet/build/HelloWorld.prj").text.contains("-inline-")

        //check splash in .rsp
        new File(basedir, "build/jet/build/HelloWorld_jetpdb/HelloWorld.rsp").text.contains("splash.png")

        //check -pack=all and splash from file (tmpres does not exist as no resources should be prepared)
        new File(basedir, "build/jet/build/HelloWorld_jetpdb/HelloWorld.rsp").text.contains('-config=RuntimeKind:CLASSIC')

        result.task(":jetBuild").outcome == TaskOutcome.SUCCESS
    }

    public String testProjectDir() {
        return "18-nostacktrace-splashfromfile-runtimeclassic"
    }

}

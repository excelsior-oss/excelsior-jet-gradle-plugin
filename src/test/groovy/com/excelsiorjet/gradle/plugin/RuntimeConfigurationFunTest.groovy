package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome
import spock.lang.IgnoreIf

@IgnoreIf({!changeRTLocationAvailable})
class RuntimeConfigurationFunTest extends BaseFunTest implements HelloWorldProject {

    def "runtime kind and location test"() {
        setup:
        def rspFile = new File(basedir, "build/jet/build/HelloWorld_jetpdb/HelloWorld.rsp");
        def relocatedRT = new File( basedir, "build/jet/app/hidden/runtime")

        when:
        def result = runGradle('jetBuild')

        then:

        rspFile.text.contains("RuntimeKind:CLASSIC")
        relocatedRT.exists()

        result.task(":jetBuild").outcome == TaskOutcome.SUCCESS
    }

    public String testProjectDir() {
        return "29-runtime-configuration"
    }

}

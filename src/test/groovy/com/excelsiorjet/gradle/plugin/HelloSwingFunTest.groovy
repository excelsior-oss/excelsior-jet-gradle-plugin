package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome

class HelloSwingFunTest extends BaseFunTest {

    def "jetBuild task builds simple swing application"() {
        setup:
        boolean isWindows = osName.contains("Windows");

        when:
        def result = runGradle('jetBuild')

        then:
        buildExeFile.exists()
        appExeFile.exists()
        zipFile.exists()

        !isWindows || new File(basedir, "build/jet/build/HelloSwing_jetpdb/HelloSwing.rsp").text.contains("icon.ico")
        !isWindows || new File(basedir, "build/jet/build/HelloSwing_jetpdb/HelloSwing.rsp").text.contains("-sys=W")

        result.task(":jetBuild").outcome == TaskOutcome.SUCCESS
    }


    @Override
    protected String testProjectDir() {
        return "03-helloswing"
    }

    @Override
    protected String projectName() {
        return "HelloSwing"
    }

    @Override
    protected String projectVersion() {
        return "1.0-SNAPSHOT"
    }
}

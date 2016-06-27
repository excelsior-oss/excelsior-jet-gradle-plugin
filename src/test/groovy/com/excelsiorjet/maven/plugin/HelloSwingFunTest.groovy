package com.excelsiorjet.maven.plugin

import org.gradle.testkit.runner.TaskOutcome

class HelloSwingFunTest extends BaseFunTest {

    def "jetBuild task builds simple application with dependency"() {
        when:
        def result = runGradle()

        boolean isWindows = System.properties['os.name'].contains("Windows");
        then:
        exeFile.exists()
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
}

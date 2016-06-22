package com.excelsiorjet.maven.plugin

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome

class HelloSwingFunTest extends BaseFunTest {

    def "jetBuild task builds simple application with dependency"() {
        given:
        Utils.copyDirectoryContents(originalProjectDir, basedir.toPath())

        when:
        def result = GradleRunner.create()
                .withProjectDir(basedir)
                .withArguments("-DexcelsiorJetPluginVersion=" + pluginVersion, 'jetBuild')
                .withDebug(true)
                .build()

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

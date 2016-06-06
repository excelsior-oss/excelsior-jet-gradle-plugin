package com.excelsiorjet.maven.plugin

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import java.nio.file.Path

class HelloWorldFunTest extends Specification {

    final Path originalProjectDir = new File(getClass().getClassLoader().getResource("01-helloworld").file).toPath()

/*    @Rule
    final TemporaryFolder testProjectDir = new TemporaryFolder()*/

    def "hello world task prints hello world"() {
        //def basedir = testProjectDir.root
        def basedir = File.createTempDir()
        given:
        Utils.copyDirectoryContents(originalProjectDir, basedir.toPath())

        when:
        def result = GradleRunner.create()
                .withProjectDir(basedir)
                .withArguments('jetBuild')
                .withPluginClasspath()
                .withDebug(true)
                .build()

        String ext = System.properties['os.name'].contains("Windows") ? ".exe" : ""
        File exeFile = new File(basedir, "build/jet/build/HelloWorld" + ext);
        File zipFile = new File(basedir, "build/jet/HelloWorld-1.0-SNAPSHOT.zip")

        then:
        exeFile.exists()
        zipFile.exists()
        exeFile.absolutePath.execute().inputStream.text.contains("Hello World")
        result.task(":jetBuild").outcome == TaskOutcome.SUCCESS
    }

}

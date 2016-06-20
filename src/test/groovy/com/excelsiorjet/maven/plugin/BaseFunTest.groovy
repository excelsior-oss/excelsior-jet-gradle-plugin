package com.excelsiorjet.maven.plugin

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import java.nio.file.Path

abstract class BaseFunTest extends Specification {

    protected static final String pluginVersion = "0.1.0-SNAPSHOT"

    protected static final String ext = System.properties['os.name'].contains("Windows") ? ".exe" : ""

    final Path originalProjectDir = new File(getClass().getClassLoader().getResource(testProjectDir()).file).toPath()

    @Rule
    final TemporaryFolder testProjectDir = new TemporaryFolder()

    File basedir
    File exeFile
    File zipFile

    void setup() {
        basedir = testProjectDir.root
        exeFile = new File(basedir, "build/jet/build/${projectName()}$ext")
        zipFile = new File(basedir, "build/jet/${projectName()}-1.0-SNAPSHOT.zip")
    }

    protected static boolean checkStdOutContains(File exeFile, String str) {
        exeFile.absolutePath.execute().inputStream.text.contains(str)
    }

    protected abstract String testProjectDir()

    protected abstract String projectName()
}

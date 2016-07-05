package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification

abstract class BaseFunTest extends Specification {

    protected static final String pluginVersion = System.getProperty("excelsiorJetPluginVersion")
    protected static final String osName = System.properties['os.name']
    protected static final String ext = osName.contains("Windows") ? ".exe" : ""

    File basedir = new File(getClass().getClassLoader().getResource(testProjectDir()).file)
    File buildExeFile
    File appExeFile
    File zipFile

    void setup() {
        buildExeFile = new File(basedir, "build/jet/build/${projectName()}$ext")
        appExeFile = new File( basedir, "build/jet/app/${projectName()}$ext")
        zipFile = new File(basedir, "build/jet/${projectName()}-" + projectVersion() + ".zip")
    }

    protected def runGradle(String tasksToExecute) {
        return GradleRunner.create()
                .withProjectDir(basedir)
                .withArguments("-DexcelsiorJetPluginVersion=" + pluginVersion, tasksToExecute)
                .withDebug(true)
                .build()
    }

    protected static boolean checkStdOutContains(File exeFile, String str) {
        exeFile.absolutePath.execute().inputStream.text.contains(str)
    }
    protected abstract String testProjectDir()

    protected abstract String projectName()

    protected abstract String projectVersion()
}

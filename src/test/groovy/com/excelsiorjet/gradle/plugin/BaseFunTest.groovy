package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification

abstract class BaseFunTest extends Specification {

    protected static final String pluginVersion = System.getProperty("excelsiorJetPluginVersion")
    protected static final String osName = System.properties['os.name']
    protected static final String ext = osName.contains("Windows") ? ".exe" : ""

    File basedir = new File(getClass().getClassLoader().getResource(testProjectDir()).file)
    File appDir
    File buildExeFile
    File appExeFile
    File zipFile

    void setup() {
        appDir = new File(basedir, "build/jet/app")
        buildExeFile = new File(basedir, "build/jet/build/${projectName()}$ext")
        appExeFile = new File(appDir, "${projectName()}$ext")
        zipFile = new File(basedir, "build/jet/${projectName()}-" + projectVersion() + ".zip")
    }

    protected def runGradle(String... tasksToExecute) {
        def args = (tasksToExecute as List) + ("-DexcelsiorJetPluginVersion=" + pluginVersion)
        return GradleRunner.create()
                .withProjectDir(basedir)
                .withArguments(args)
                .withDebug(true)
                .build()
    }

    protected static boolean checkStdOutContains(File exeFile, String str) {
        cmdOutput(exeFile).contains(str)
    }

    public static String cmdOutput(File exeFile) {
        def process = exeFile.absolutePath.execute()
        process.inputStream.text + process.errorStream.text
    }

    protected abstract String testProjectDir()

    protected abstract String projectName()

    protected abstract String projectVersion()
}

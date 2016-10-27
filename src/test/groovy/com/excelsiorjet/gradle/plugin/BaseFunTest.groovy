package com.excelsiorjet.gradle.plugin

import com.excelsiorjet.api.ExcelsiorJet
import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification

import static org.codehaus.groovy.runtime.ProcessGroovyMethods.*

abstract class BaseFunTest extends Specification {

    protected static final String pluginVersion = System.getProperty("excelsiorJetPluginVersion")
    protected static final ExcelsiorJet excelsiorJet = new ExcelsiorJet(null);
    protected static final boolean isWindows = excelsiorJet.targetOS.isWindows()
    protected static final boolean isOSX = excelsiorJet.targetOS.isOSX()
    protected static final boolean excelsiorInstallerSupported = excelsiorJet.excelsiorInstallerSupported
    protected static final boolean crossCompilation = excelsiorJet.crossCompilation
    protected static final String ext = excelsiorJet.targetOS.exeFileExtension

    File basedir = new File(getClass().getClassLoader().getResource(testProjectDir()).file)
    File appDir
    File jetBuildDir
    File buildExeFile
    File appExeFile
    File zipFile

    void setup() {
        appDir = new File(basedir, "build/jet/app")
        jetBuildDir = new File(basedir, "build/jet/build")
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

    protected static boolean checkStdOutContains(String str, File exeFile, String... args) {
        if (!crossCompilation) {
            cmdOutput(exeFile, args).contains(str)
        } else {
            true
        }
    }

    public static String cmdOutput(File exeFile, String... args) {
        def process = execute([exeFile.absolutePath] + args.toList())
        process.inputStream.text + process.errorStream.text
    }

    public static String toUnixLineSeparators(String text) {
        text.replaceAll("\r\n", "\n")
    }

    protected abstract String testProjectDir()

    protected abstract String projectName()

    protected abstract String projectVersion()
}

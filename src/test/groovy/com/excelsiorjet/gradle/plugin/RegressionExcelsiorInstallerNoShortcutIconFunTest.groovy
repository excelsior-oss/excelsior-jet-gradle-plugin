package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome
import spock.lang.IgnoreIf

class RegressionExcelsiorInstallerNoShortcutIconFunTest extends BaseFunTest {

    @IgnoreIf({!advancedExcelsiorInstallerFeaturesSupported || !isWindows})
    def boolean "regression test for excelsior installer configured with shortcuts without icon set"() {
        setup:
        File installer = new File(basedir, "build/jet/HelloWorld-1.0-SNAPSHOT" + ext)

        when:
        def result = runGradle('jetBuild')

        then:
        buildExeFile.exists()
        appExeFile.exists()
        installer.exists();
        String xpackArgs = toUnixLineSeparators(new File(basedir, "build/jet/build/HelloWorld.EI.xpack").text)
        xpackArgs.contains("""
-shortcut program-folder README.txt "Read me first" "" "" ""
-file-association abc HelloWorld.exe "ABC Files" "Super Duper Program" "" "" checked
""")

        result.task(":jetBuild").outcome == TaskOutcome.SUCCESS
    }

    @Override
    protected String testProjectDir() {
        return "40-regression-excelsior-installer-no-shortcut-icon"
    }

    @Override
    protected String projectName() {
        return "HelloWorld"
    }

    @Override
    protected String projectVersion() {
        return "1.2.3-SNAPSHOT"
    }
}

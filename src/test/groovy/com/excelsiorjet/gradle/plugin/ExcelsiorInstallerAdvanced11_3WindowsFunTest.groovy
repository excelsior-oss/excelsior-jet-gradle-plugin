package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome
import spock.lang.IgnoreIf

class ExcelsiorInstallerAdvanced11_3WindowsFunTest extends BaseFunTest {

    @IgnoreIf({!advancedExcelsiorInstallerFeaturesSupported || !isWindows})
    def boolean "jetBuild task builds excelsior installer using xpack options available for Windows since 11.3"() {
        setup:
        File installer = new File(basedir, "build/jet/HelloWorld-1.0-SNAPSHOT" + ext)

        when:
        def result = runGradle('jetBuild')

        then:
        buildExeFile.exists()
        appExeFile.exists()
        installer.exists();
        String xpackArgs = toUnixLineSeparators(new File(basedir, "build/jet/build/HelloWorld.EI.xpack").text)
        xpackArgs.contains("-registry-key excelsior/maven/tests")
        xpackArgs.contains("""
-shortcut program-folder README.txt "Read me first" /readme.ico "" ""
-no-default-post-install-actions 
-post-install-checkbox-run run.bat workDir arg checked
-post-install-checkbox-open README.txt checked
-post-install-checkbox-restart unchecked
-file-association abc HelloWorld.exe "ABC Files" "Super Duper Program" file.ico "" checked
""")
        xpackArgs.contains("-welcome-image")
        xpackArgs.contains("-installer-image")
        xpackArgs.contains("-uninstaller-image")

        result.task(":jetBuild").outcome == TaskOutcome.SUCCESS
    }

    @Override
    protected String testProjectDir() {
        return "33-excelsior-installer-advanced-11-3-windows"
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

package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome
import spock.lang.IgnoreIf

class ExcelsiorInstallerAdvanced11_3CommonFunTest extends BaseFunTest {

    @IgnoreIf({!excelsiorInstallerSupported || !since11_3})
    def boolean "jetBuild task builds excelsior installer using common (Windows, Linux) xpack options available since 11.3"() {
        setup:
        File installer = new File(basedir, "build/jet/HelloWorld-1.0-SNAPSHOT" + ext)

        when:
        def result = runGradle('jetBuild')

        then:
        buildExeFile.exists()
        appExeFile.exists()
        installer.exists();
        String xpackArgs = new File(basedir, "build/jet/build/HelloWorld.EI.xpack").text
        xpackArgs.contains("-language german")
        xpackArgs.contains("-cleanup-after-uninstall")
        xpackArgs.contains("-compression-level fast")
        xpackArgs.contains("-after-install-runnable HelloWorld")
        xpackArgs.contains("arg with space")
        xpackArgs.contains("-installation-directory /mavenPluginTestEI")
        xpackArgs.contains("-installation-directory-type current-directory")
        xpackArgs.contains("-installation-directory-fixed")
        xpackArgs.contains("-install-callback")
        xpackArgs.contains("-uninstall-callback")

        result.task(":jetBuild").outcome == TaskOutcome.SUCCESS
    }

    @Override
    protected String testProjectDir() {
        return "32-excelsior-installer-advanced-11-3-common"
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

package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome
import spock.lang.IgnoreIf

class WindowsServiceMultiAppWithExcelsiorInstallerFunTest extends BaseFunTest {

    @IgnoreIf({!windowsServicesInExcelsiorInstallerSupported})
    def "jetBuild task builds windows service"() {
        when:
        def result = runGradle('jetBuild')
        def rspFile = new File(appDir, "SampleService.rsp");
        File installer = new File(basedir, "build/jet/${projectName()}-" + projectVersion() + ext)
        File installBat = new File(appDir, "install.bat")
        File uninstallBat = new File(appDir, "uninstall.bat")

        then:
        buildExeFile.exists()
        appExeFile.exists()

        rspFile.exists();
        String rspContents = toUnixLineSeparators(rspFile.text);
        rspContents.equals(
        """-install SampleService.exe
-displayname "Sample Service"
-description "Sample Service created with Excelsior JET"
-manual
-dependence Dhcp
-dependence Dnscache
-args
-args
arg
arg with space
arg3 with space
"""
        )

        installBat.exists()
        installBat.text.contains("-user %name% -password %password")
        uninstallBat.exists()
        installer.exists()

        result.task(":jetBuild").outcome == TaskOutcome.SUCCESS
    }

    public String testProjectDir() {
        return "25-windows-service-multiapp-with-excelsior-installer"
    }

    @Override
    protected String projectName() {
        return "SampleService"
    }

    @Override
    protected String projectVersion() {
        return "1.0-SNAPSHOT"
    }
}

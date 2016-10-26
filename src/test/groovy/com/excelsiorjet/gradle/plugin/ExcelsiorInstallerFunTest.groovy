package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome
import spock.lang.IgnoreIf

class ExcelsiorInstallerFunTest extends BaseFunTest {

    @IgnoreIf({!excelsiorInstallerSupported})
    def "jetBuild task builds simple swing application and packs it with excelsior installer"() {
        setup:
        File installer = new File(basedir, "build/jet/HelloSwing-1.2.3-SNAPSHOT" + ext)
        File versionRes = new File(basedir, "build/jet/build/jetpdb/version.rc");

        when:
        def result = runGradle('jetBuild')

        then:
        buildExeFile.exists()
        appExeFile.exists()
        installer.exists();
        !versionRes.exists() || versionRes.text.contains("1.2.3")

        result.task(":jetBuild").outcome == TaskOutcome.SUCCESS
    }

    @Override
    protected String testProjectDir() {
        return "04-excelsior-installer"
    }

    @Override
    protected String projectName() {
        return "HelloSwing"
    }

    @Override
    protected String projectVersion() {
        return "1.2.3-SNAPSHOT"
    }
}

package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome
import spock.lang.IgnoreIf

class ExcelsiorInstallerAdvancedFunTest extends BaseFunTest {

    @IgnoreIf({!excelsiorInstallerSupported})
    def boolean "jetBuild task builds simple swing application and packs it with excelsior installer with cyrillic eula and splash in custom location"() {
        setup:
        File installer = new File(basedir, "build/jet/HelloSwing-1.2.3-SNAPSHOT" + ext)

        when:
        def result = runGradle('jetBuild')

        then:
        buildExeFile.exists()
        appExeFile.exists()
        installer.exists();

        result.task(":jetBuild").outcome == TaskOutcome.SUCCESS
    }

    @Override
    protected String testProjectDir() {
        return "05-excelsior-installer-advanced"
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

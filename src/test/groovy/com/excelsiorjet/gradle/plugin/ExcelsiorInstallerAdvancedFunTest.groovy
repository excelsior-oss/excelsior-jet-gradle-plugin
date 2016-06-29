package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome

class ExcelsiorInstallerAdvancedFunTest extends BaseFunTest {

    def "jetBuild task builds simple swing application and packs it with excelsior installer with cyrillic eula"() {
        when:
        def result = runGradle('jetBuild')

        boolean isWindows = System.properties['os.name'].contains("Windows");
        boolean isOX = System.properties['os.name'].contains("OS X");
        String ext = isWindows ? ".exe" : ""
        File installer = new File(basedir, "build/jet/HelloSwing-1.2.3-SNAPSHOT" + ext)

        then:
        buildExeFile.exists()
        appExeFile.exists()
        isOX || installer.exists();

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

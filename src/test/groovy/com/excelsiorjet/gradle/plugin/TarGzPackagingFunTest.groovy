package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome
import spock.lang.IgnoreIf

class TarGzPackagingFunTest extends BaseFunTest implements HelloWorldProject {

    @IgnoreIf({!tarGzPackagingSupported})
    def "jetBuild task packs to tar.gz"() {
        when:
        def archiveFile = new File(basedir, "build/jet/${projectName()}"  + ".tar.gz")
        def result = runGradle('jetBuild')

        then:
        buildExeFile.exists()
        appExeFile.exists()
        archiveFile.exists()

        checkStdOutContains("Hello World", appExeFile)

        result.task(":jetBuild").outcome == TaskOutcome.SUCCESS
    }

    public String testProjectDir() {
        return "48-tar-gz-packaging"
    }

}

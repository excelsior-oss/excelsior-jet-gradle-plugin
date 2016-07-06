package com.excelsiorjet.gradle.plugin

class ProtectDataFunTest extends BaseFunTest implements HelloWorldProject {

    def "test protect data configuration"() {
        when:
        runGradle("clean", "jetBuild")

        then:
        appExeFile.exists()
        !appExeFile.text.contains("<mainClass>")
    }

    @Override
    protected String testProjectDir() {
        return "13-protectdata"
    }
}

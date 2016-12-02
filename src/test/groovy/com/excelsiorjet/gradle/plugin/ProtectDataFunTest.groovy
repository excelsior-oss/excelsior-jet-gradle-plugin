package com.excelsiorjet.gradle.plugin

import spock.lang.IgnoreIf

class ProtectDataFunTest extends BaseFunTest implements HelloWorldProject {

    @IgnoreIf({!dataProtectionSupported})
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

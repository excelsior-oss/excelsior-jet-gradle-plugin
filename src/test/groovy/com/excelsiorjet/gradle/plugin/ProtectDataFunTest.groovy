package com.excelsiorjet.gradle.plugin

import spock.lang.IgnoreIf

class ProtectDataFunTest extends BaseFunTest implements HelloWorldProject {

    @IgnoreIf({!dataProtectionSupported})
    def "test protect data configuration"() {
        when:
        def cryptseed = new File(basedir, "build/jet/build/HelloWorld_jetpdb/cryptseed")
        runGradle("clean", "jetBuild")

        then:
        appExeFile.exists()
        !appExeFile.text.contains("<mainClass>")
        cryptseed.exists()
    }

    @Override
    protected String testProjectDir() {
        return "13-protectdata"
    }
}

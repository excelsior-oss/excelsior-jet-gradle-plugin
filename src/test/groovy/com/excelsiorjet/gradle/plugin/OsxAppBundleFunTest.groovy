package com.excelsiorjet.gradle.plugin

import spock.lang.IgnoreIf

class OsxAppBundleFunTest extends BaseFunTest {

    @IgnoreIf({!isOSX})
    def "test osx bundle configuration"() {
        setup:
        File infoPlist = new File(basedir, "build/jet/HelloSwing.app/Contents/Info.plist")

        when:
        runGradle("clean", "jetBuild")

        then:
        appExeFile.exists()
        infoPlist.exists()
        infoPlist.text.contains("CFBundleIconFile")
        infoPlist.text.contains("com.excelsior")
    }

    @Override
    protected String testProjectDir() {
        return "14-osx-app-bundle"
    }

    @Override
    protected String projectName() {
        return "HelloSwing"
    }

    @Override
    protected String projectVersion() {
        return "1.0-SNAPSHOT"
    }
}

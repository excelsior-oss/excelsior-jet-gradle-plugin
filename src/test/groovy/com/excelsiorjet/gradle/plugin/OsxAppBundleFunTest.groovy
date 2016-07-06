package com.excelsiorjet.gradle.plugin

class OsxAppBundleFunTest extends BaseFunTest {

    def "test osx bundle configuration"() {
        setup:
        File infoPlist = new File(basedir, "build/jet/HelloSwing.app/Contents/Info.plist")

        when:
        runGradle("clean", "jetBuild")

        then:
        if (osName.contains("OS X")) {
            appExeFile.exists()
            infoPlist.exists()
            infoPlist.text.contains("CFBundleIconFile")
        }
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

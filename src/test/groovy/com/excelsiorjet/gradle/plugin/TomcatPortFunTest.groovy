package com.excelsiorjet.gradle.plugin

import spock.lang.IgnoreIf

class TomcatPortFunTest extends BaseFunTest {

    @IgnoreIf({!tomcatSupported || !excelsiorInstallerSupported || !since11_3})
    def "test tomcat port"() {
        given:
        File installer = new File(basedir, "build/jet/${projectName()}-" + projectVersion() + ext)
        File xpackArgsFile = new File(basedir, "build/jet/build/HelloTomcat.EI.xpack")

        when:
        runGradle("clean", "war", "jetBuild")

        then:
        new File(appDir, "bin/HelloTomcat$ext").exists()
        installer.exists()
        xpackArgsFile.text.contains("-allow-user-to-change-tomcat-port")
    }

    @Override
    protected String testProjectDir() {
        return "34-tomcat-port"
    }

    @Override
    protected String projectName() {
        return "HelloTomcat"
    }

    @Override
    protected String projectVersion() {
        return "1.0-SNAPSHOT"
    }
}

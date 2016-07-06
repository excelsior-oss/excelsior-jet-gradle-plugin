package com.excelsiorjet.gradle.plugin

class TomcatFunTest extends BaseFunTest {

    def "test tomcat configuration"() {
        when:
        runGradle("clean", "war", "jetBuild")

        then:
        new File(appDir, "bin/HelloTomcat$ext").exists()
        zipFile.exists()
    }

    @Override
    protected String testProjectDir() {
        return "15-hellotomcat"
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

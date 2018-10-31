package com.excelsiorjet.gradle.plugin

import spock.lang.IgnoreIf

class SpringBootWarFunTest extends BaseFunTest {

    @IgnoreIf({!springBootSupported || !PGOSupported})
    def "test spring boot war"() {
        given:
        def exeFile = new File(basedir, "build/jet/app/spring-boot-sample-war" + ext)
        def zipFile = new File(basedir, "build/jet/spring-boot-sample-war-1.0-SNAPSHOT.zip")

        //profiles
        def profileExeFile = new File( basedir, "build/jet/appToProfile/spring-boot-sample-war" + ext)
        def profileFile = new File(basedir, "src/main/jetresources/spring-boot-sample-war.jprof");
        def startupProfile = new File(basedir, "src/main/jetresources/spring-boot-sample-war.startup");
        def usgProfile = new File(basedir, "src/main/jetresources/spring-boot-sample-war.usg");

        def prjFile = new File(jetBuildDir, "spring-boot-sample-war.prj")

        when:
        runGradle('jetTestRun', 'jetProfile', 'jetBuild')

        then:
        exeFile.exists()
        zipFile.exists()

        //check profile
        profileExeFile.exists()
        profileFile.exists()
        startupProfile.exists()
        usgProfile.exists()
        usgProfile.text.contains("springboot%")

        prjFile.exists()
        def prjText = toUnixLineSeparators(prjFile.text)
        prjText.contains("""
!classloaderentry app spring-boot-sample-war-1.0-SNAPSHOT.war
  -pack=all
!end""")
    }

    @Override
    protected String testProjectDir() {
        return "45-spring-boot-war"
    }

    @Override
    protected String projectName() {
        return "spring-boot-sample-war"
    }

    @Override
    protected String projectVersion() {
        return "1.0-SNAPSHOT"
    }
}

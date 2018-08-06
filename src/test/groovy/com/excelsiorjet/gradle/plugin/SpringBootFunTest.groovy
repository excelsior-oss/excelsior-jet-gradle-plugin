package com.excelsiorjet.gradle.plugin

import spock.lang.IgnoreIf

class SpringBootFunTest extends BaseFunTest {

    @IgnoreIf({!springBootSupported || !PGOSupported})
    def "test spring boot"() {
        given:
        def exeFile = new File(basedir, "build/jet/app/spring-boot-sample-tomcat" + ext)
        def jarFile = new File(basedir, "build/jet/app/spring-boot-sample-tomcat-1.0-SNAPSHOT.jar")
        def zipFile = new File(basedir, "build/jet/spring-boot-sample-tomcat-1.0-SNAPSHOT.zip")

        //profiles
        def profileExeFile = new File( basedir, "build/jet/appToProfile/spring-boot-sample-tomcat" + ext)
        def profileFile = new File(basedir, "src/main/jetresources/spring-boot-sample-tomcat.jprof");
        def startupProfile = new File(basedir, "src/main/jetresources/spring-boot-sample-tomcat.startup");
        def usgProfile = new File(basedir, "src/main/jetresources/spring-boot-sample-tomcat.usg");

        def prjFile = new File(jetBuildDir, "spring-boot-sample-tomcat.prj")

        when:
        runGradle('jetTestRun', 'jetProfile', 'jetBuild')

        then:
        exeFile.exists()
        jarFile.exists()
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
!classloaderentry app spring-boot-sample-tomcat-1.0-SNAPSHOT.jar
  -pack=none
!end""")
    }

    @Override
    protected String testProjectDir() {
        return "44-spring-boot"
    }

    @Override
    protected String projectName() {
        return "spring-boot-sample-tomcat"
    }

    @Override
    protected String projectVersion() {
        return "1.0-SNAPSHOT"
    }
}

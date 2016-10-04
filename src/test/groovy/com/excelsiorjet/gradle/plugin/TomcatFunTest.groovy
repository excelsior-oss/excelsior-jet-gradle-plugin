package com.excelsiorjet.gradle.plugin

class TomcatFunTest extends BaseFunTest {

    def "test tomcat configuration"() {
        given:
        def prjFile = new File(jetBuildDir, "HelloTomcat.prj")

        when:
        runGradle("clean", "war", "jetBuild")

        then:
        new File(appDir, "bin/HelloTomcat$ext").exists()
        zipFile.exists()

        new File(basedir, "build/jet/build/HelloTomcat_jetpdb/tmpres/ROOT/WEB-INF/lib/commons-io-1.3.2.jar").exists()

        prjFile.text.contains("""!classloaderentry webapp webapps/ROOT:/WEB-INF/lib/commons-io-1.3.2.jar
  -optimize=autodetect
  -protect=nomatter
  -pack=all
!end""")

        prjFile.text.contains("""!classloaderentry webapp webapps/ROOT:/WEB-INF/classes
  -optimize=all
  -protect=nomatter
!end""")
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

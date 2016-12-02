package com.excelsiorjet.gradle.plugin

import spock.lang.IgnoreIf

class TomcatFunTest extends BaseFunTest {

    @IgnoreIf({!tomcatSupported})
    def "test tomcat configuration"() {
        given:
        def prjFile = new File(jetBuildDir, "HelloTomcat.prj")
        File installer = new File(basedir, "build/jet/${projectName()}-" + projectVersion() + ext)

        when:
        runGradle("clean", "war", "jetBuild")

        then:
        new File(appDir, "bin/HelloTomcat$ext").exists()
        excelsiorInstallerSupported && installer.exists() || zipFile.exists()

        new File(basedir, "build/jet/build/HelloTomcat_jetpdb/tmpres/ROOT/WEB-INF/lib/commons-io-1.3.2.jar").exists()


        //replace line separators to Unix as Groovy """ multiline strings produce Unix line separators
        def prjText = toUnixLineSeparators(prjFile.text)
        prjText.contains("""
!classloaderentry webapp webapps/ROOT:/WEB-INF/lib/commons-io-1.3.2.jar
  -optimize=autodetect
  -protect=nomatter
  -pack=all
!end""")

        prjText.contains("""
!classloaderentry webapp webapps/ROOT:/WEB-INF/classes
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

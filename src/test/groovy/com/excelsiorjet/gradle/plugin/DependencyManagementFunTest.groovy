package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome


class DependencyManagementFunTest extends BaseFunTest {

    def "jetBuild task accounts dependency settings"() {
        setup:
        File dep = new File(basedir, "build/jet/build/lib/commons-io-1.3.2.jar")
        File prj = new File(basedir, "build/jet/build/AppWithDep.prj")
        File junitLib = new File(basedir, "build/jet/build/lib/junit-4.8.2.jar")
        File jacksonDep = new File(basedir, "build/jet/build/lib/jackson-databind-2.8.0.jar")
        File log4jDep = new File(basedir, "build/jet/build/libs/log4j-1.2.17.jar")
        File extDirContent= new File(basedir, "build/jet/build/extDir/test.txt")
        File extDirCpContent = new File(basedir, "build/jet/build/extDirCp/extDir2/test.txt")

        when:
        def result = runGradle('jetBuild')

        then:
        buildExeFile.exists()
        appExeFile.exists()
        zipFile.exists()

        dep.exists()

         //replace line separators to Unix as Groovy """ multiline strings produce Unix line separators
        def prjText = toUnixLineSeparators(prj.text)
        prjText.contains("""
!classpathentry lib/commons-io-1.3.2.jar
  -optimize=autodetect
  -protect=nomatter
  -pack=all
!end""")

        junitLib.exists()
        prjText.contains("""
!classpathentry lib/junit-4.8.2.jar
  -optimize=all
  -protect=all
  -pack=all
!end""")

        jacksonDep.exists()
        prjText.contains("""
!classpathentry lib/jackson-databind-2.8.0.jar
  -optimize=autodetect
  -protect=all
  -pack=none
!end""")
        prjText.contains("""
!classpathentry lib/jackson-annotations-2.8.0.jar
  -optimize=autodetect
  -protect=all
!end""")
        prjText.contains("""
!classpathentry lib/jackson-core-2.8.0.jar
  -optimize=all
  -protect=all
!end""")

        log4jDep.exists()
        prjText.contains("""
!classpathentry libs/log4j-1.2.17.jar
  -optimize=autodetect
  -protect=nomatter
  -pack=none
!end""")

        extDirContent.exists()
        extDirCpContent.exists()

        checkStdOutContains("HelloWorld", appExeFile)

        result.task(":jetBuild").outcome == TaskOutcome.SUCCESS
    }

    @Override
    protected String testProjectDir() {
        return '21-dependencymanagement'
    }

    @Override
    protected String projectName() {
        return 'AppWithDep'
    }

    @Override
    protected String projectVersion() {
        return "1.0-SNAPSHOT"
    }
}

package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome

class WithFileDependenciesTest extends BaseFunTest {

    def "jetBuild task builds simple application with files(..) and fileTree(..) dependencies"() {
        setup:
        File depRepo = new File(basedir, "build/jet/build/AppWithFileDeps_jetpdb/tmpres/commons-io-1.3.2__1.jar")
        File depFiles = new File(basedir, "build/jet/build/AppWithFileDeps_jetpdb/tmpres/single-dep__2.jar")
        File depFileTreeFirst = new File(basedir, "build/jet/build/AppWithFileDeps_jetpdb/tmpres/first-multi-dep__3.jar")
        File depFileTreeSecond = new File(basedir, "build/jet/build/AppWithFileDeps_jetpdb/tmpres/second-multi-dep__4.jar")
        File prj = new File(basedir, "build/jet/build/AppWithFileDeps.prj")

        when:
        def result = runGradle('jetBuild')

        then:
        buildExeFile.exists()
        appExeFile.exists()
        zipFile.exists()
        depRepo.exists()
        depFiles.exists()
        depFileTreeFirst.exists()
        depFileTreeSecond.exists()
        def prjText = toUnixLineSeparators(prj.text)
        prjText.contains("""
!classpathentry lib/single-dep.jar
  -optimize=autodetect
  -protect=nomatter
  -pack=all
!end""")

        checkStdOutContains(appExeFile, "HelloWorld:SingleDep:FirstMultiDep:SecondMultiDep")

        result.task(":jetBuild").outcome == TaskOutcome.SUCCESS
    }

    @Override
    protected String testProjectDir() {
        return "22-with-file-dependencies"
    }

    @Override
    protected String projectName() {
        return "AppWithFileDeps"
    }

    @Override
    protected String projectVersion() {
        return "1.0-SNAPSHOT"
    }
}

package com.excelsiorjet.gradle.plugin

class JvmArgsFunTest extends BaseFunTest implements HelloWorldProject {

    def "test jvm args configuration"() {
        setup:
        def runOutBuildFile = new File(basedir, "build/jet/build/run.out")
        def rtPathBuild = new File(basedir, "build/jet/build/rt").getAbsolutePath()

        def runOutAppFile = new File(appDir, "run.out")
        def rtPathApp = new File(appDir, "rt").getAbsolutePath()

        when:
        runGradle("clean", "jetTestRun", "jetBuild")
        appExeFile.getAbsolutePath().execute([], appDir).waitForOrKill(2000)

        then:
        runOutBuildFile.text.replace("/",File.separator).trim().equals(rtPathBuild)
        runOutAppFile.text.replace("/",File.separator).trim().equals(rtPathApp)

        appExeFile.exists()
    }

    @Override
    protected String testProjectDir() {
        return "12-jvmargs"
    }

    @Override
    String projectName() {
        return "JVMArgs"
    }
}

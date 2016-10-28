package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome

class OptRtFilesFunTest extends BaseFunTest implements HelloWorldProject {

    def "test additional RT-files"() {
        setup:
        File optRtJar = new File(basedir, "build/jet/app/rt/lib/ext/nashorn.jar")

        when:
        def result = runGradle('jetBuild')
        // should be in when, because requires directory created while run

        then:
        buildExeFile.exists()
        appExeFile.exists()
        zipFile.exists()

        ["build/jet/app/rt/bin/jfxwebkit.dll",
         "build/jet/app/rt/lib/libjfxwebkit.dylib",
         "build/jet/app/rt/lib/amd64/libjfxwebkit.so",
         "build/jet/app/rt/lib/i386/libjfxwebkit.so",
         "build/jet/app/rt/lib/arm/libjfxwebkit.so"].any { new File(basedir, it).exists()}

        optRtJar.exists()

        result.task(":jetBuild").outcome == TaskOutcome.SUCCESS
    }

    @Override
    protected String testProjectDir() {
        return "08-optrtfiles"
    }

}

package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome

class OptRtFilesFunTest extends BaseFunTest implements HelloWorldProject {

    def "test additional RT-files"() {
        setup:
        File optRtJar = new File(basedir, "build/jet/app/rt/lib/ext/nashorn.jar")

        when:
        def result = runGradle('jetBuild')
        // should be in when, because requires directory created while run
        File optRtDll = getOptRtDll()

        then:
        buildExeFile.exists()
        appExeFile.exists()
        zipFile.exists()

        optRtDll.exists()
        optRtJar.exists()

        result.task(":jetBuild").outcome == TaskOutcome.SUCCESS
    }

    @Override
    protected String testProjectDir() {
        return "08-optrtfiles"
    }

    private File getOptRtDll() {
        File optRtDll
        if (osName.contains("Windows")) {
            optRtDll = new File(basedir, "build/jet/app/rt/bin/jfxwebkit.dll");
        } else if (osName.contains("OS X")) {
            optRtDll = new File(basedir, "build/jet/app/rt/lib/libjfxwebkit.dylib");
        } else if (osName.contains("Linux")) {
            if (new File(basedir, "build/jet/app/rt/lib/amd64").exists()) {
                optRtDll = new File(basedir, "build/jet/app/rt/lib/amd64/libjfxwebkit.so");
            } else if (new File(basedir, "build/jet/app/rt/lib/i386").exists()) {
                optRtDll = new File(basedir, "build/jet/app/rt/lib/i386/libjfxwebkit.so");
            } else {
                throw new IllegalArgumentException("Could not find arch-specific rt-library directory")
            }
        } else {
            throw new IllegalArgumentException("Unknown OS: $osName")
        }
        return optRtDll
    }

}

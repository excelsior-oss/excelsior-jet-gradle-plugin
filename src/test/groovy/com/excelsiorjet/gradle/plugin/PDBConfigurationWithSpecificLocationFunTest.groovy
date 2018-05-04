package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome
import spock.lang.IgnoreIf

class PDBConfigurationWithSpecificLocationFunTest extends BaseFunTest implements HelloWorldProject {

    @IgnoreIf({!smartSupported})
    def "configure PDB with specificLocation"() {
        when:
        def pdbDir = new File(System.getProperty("user.home"), ".ExcelsiorJET" + File.separator + "TestPDB")
        def result = runGradle('jetBuild')

        then:
        pdbDir.exists()

        result.task(":jetBuild").outcome == TaskOutcome.SUCCESS
    }

    public String testProjectDir() {
        return "42-pdbconfiguration-with-specific-location"
    }

}

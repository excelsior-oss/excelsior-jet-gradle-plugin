package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome
import spock.lang.IgnoreIf

class PDBConfigurationWithBaseDirFunTest extends BaseFunTest implements HelloWorldProject {

    @IgnoreIf({!smartSupported})
    def "configure PDB with baseDir"() {
        when:
        def pdbDir = new File(System.getProperty("user.home"), ".ExcelsiorJET" + File.separator + "TestPDBBaseDir" +
                File.separator + "com.excelsior.it" + File.separator + "HelloWorld")
        def result = runGradle('jetBuild')

        then:
        pdbDir.exists()

        result.task(":jetBuild").outcome == TaskOutcome.SUCCESS
    }

    public String testProjectDir() {
        return "41-pdbconfiguration-with-base-dir"
    }

}

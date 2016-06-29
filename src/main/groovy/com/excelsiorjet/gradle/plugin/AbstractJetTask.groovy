package com.excelsiorjet.gradle.plugin

import com.excelsiorjet.api.tasks.ApplicationType
import com.excelsiorjet.api.tasks.ClasspathEntry
import com.excelsiorjet.api.tasks.JetProject
import org.gradle.api.DefaultTask
import org.gradle.api.Project

class AbstractJetTask extends DefaultTask {

    protected JetProject createJetProject() {
        ExcelsiorJetExtension ext = project.excelsiorJet
        def jetProject = new JetProject(project.name, ext.getGroupId(), ext.getVersion(), ApplicationType.PLAIN, project.buildDir, ext.getJetResourcesDir())

        // getters should be used to fallback into convention mapping magic, when field is not set
        jetProject.dependencies(getDependencies(project))
                .excelsiorJetPackaging(ext.getPackaging())
                .artifactName(ext.getArtifactName())
                .jetOutputDir(ext.getJetOutputDir())
                .mainClass(ext.getMainClass())
                .mainJar(ext.getMainJar())
                .outputName(ext.getOutputName())
                .packageFilesDir(ext.getPackageFilesDir())
                .version(ext.getVersion())
                .jetHome(ext.getJetHome())
                .hideConsole(ext.getHideConsole())
                .icon(ext.getIcon())
                .execProfilesDir(ext.getExecProfilesDir())
                .execProfilesName(ext.getExecProfilesName())
                .jvmArgs(ext.getJvmArgs())
                .addWindowsVersionInfo(ext.getAddWindowsVersionInfo())
                .winVIVersion(ext.getWinVIVersion())
                .winVIDescription(ext.getWinVIDescription())
                .winVICopyright(ext.getWinVICopyright())
                .inceptionYear(ext.getInceptionYear())
                .vendor(ext.getVendor())
                .product(ext.getProduct())
                .excelsiorInstallerConfiguration(ext.getExcelsiorInstaller())
                .globalOptimizer(ext.getGlobalOptimizer())
                .javaRuntimeSlimDown(ext.getJavaRuntimeSlimdown())
                .trialVersion(ext.getTrialVersion())
                .osxBundleConfiguration(ext.getOsxBundle())
                .multiApp(ext.getMultiApp())
                .profileStartup(ext.getProfileStartup())
                .profileStartupTimeout(ext.getProfileStartupTimeout())
                .protectData(ext.getProtectData())
                .cryptSeed(ext.getCryptSeed())
                .optRtFiles(ext.getOptRtFiles())
        jetProject
    }

    static List<ClasspathEntry> getDependencies(Project project) {
        def configuration = project.configurations.getByName("compile")
        return configuration.getResolvedConfiguration().getResolvedArtifacts().collect() {
            new ClasspathEntry(it.file, project.group.equals(it.moduleVersion.id.module.group))
        }
    }
}

package com.excelsiorjet.gradle.plugin

import com.excelsiorjet.api.log.AbstractLog
import com.excelsiorjet.api.tasks.ClasspathEntry
import com.excelsiorjet.api.tasks.ExcelsiorInstallerConfig
import com.excelsiorjet.api.tasks.JetTaskConfig
import com.excelsiorjet.api.tasks.OSXAppBundleConfig
import com.excelsiorjet.api.tasks.SlimDownConfig
import com.excelsiorjet.api.tasks.TomcatConfig
import com.excelsiorjet.api.tasks.TrialVersionConfig
import org.gradle.api.Project

import java.util.stream.Stream

class ExcelsiorJetExtension implements JetTaskConfig {

    def String mainClass = 'HelloWorld'

    def Project project

    @Override
    void setAddWindowsVersionInfo(boolean b) {

    }

    @Override
    boolean isAddWindowsVersionInfo() {
        return false
    }

    @Override
    String excelsiorJetPackaging() {
        return "zip"
    }

    @Override
    void setExcelsiorJetPackaging(String s) {

    }

    @Override
    String vendor() {
        return null
    }

    @Override
    void setVendor(String s) {

    }

    @Override
    String product() {
        return null
    }

    @Override
    String artifactId() {
        return project.name
    }

    @Override
    void setProduct(String s) {

    }

    @Override
    String winVIVersion() {
        return null
    }

    @Override
    void setWinVIVersion(String s) {

    }

    @Override
    String winVICopyright() {
        return null
    }

    @Override
    void setWinVICopyright(String s) {

    }

    @Override
    String inceptionYear() {
        return null
    }

    @Override
    String winVIDescription() {
        return null
    }

    @Override
    void setWinVIDescription(String s) {

    }

    @Override
    boolean globalOptimizer() {
        return false
    }

    @Override
    void setGlobalOptimizer(boolean b) {

    }

    @Override
    SlimDownConfig javaRuntimeSlimDown() {
        return null
    }

    @Override
    void setJavaRuntimeSlimDown(SlimDownConfig slimDownConfig) {

    }

    @Override
    TrialVersionConfig trialVersion() {
        return null
    }

    @Override
    void setTrialVersion(TrialVersionConfig trialVersionConfig) {

    }

    @Override
    ExcelsiorInstallerConfig excelsiorInstallerConfiguration() {
        return null
    }

    @Override
    String version() {
        return project.version.toString()
    }

    @Override
    OSXAppBundleConfig osxBundleConfiguration() {
        return null
    }

    @Override
    String outputName() {
        return "HelloWorld"
    }

    @Override
    void setOutputName(String s) {

    }

    @Override
    boolean multiApp() {
        return false
    }

    @Override
    void setMultiApp(boolean b) {

    }

    @Override
    boolean profileStartup() {
        return false
    }

    @Override
    void setProfileStartup(boolean b) {

    }

    @Override
    boolean protectData() {
        return false
    }

    @Override
    String cryptSeed() {
        return null
    }

    @Override
    void setCryptSeed(String s) {

    }

    @Override
    File icon() {
        return null
    }

    @Override
    boolean hideConsole() {
        return false
    }

    @Override
    int profileStartupTimeout() {
        return 0
    }

    @Override
    String[] optRtFiles() {
        return new String[0]
    }

    @Override
    File jetOutputDir() {
        return new File("${project.buildDir}/jet")
    }

    @Override
    File tomcatHome() {
        return null
    }

    @Override
    File tomcatInBuildDir() {
        return null
    }

    @Override
    String warDeployName() {
        return null
    }

    @Override
    File mainWar() {
        return null
    }

    @Override
    AbstractLog log() {
        return AbstractLog.instance()
    }

    @Override
    String jetHome() {
        return null
    }

    @Override
    String packaging() {
        return "jar"
    }

    @Override
    File mainJar() {
        return project.tasks.getByPath(":jar").archivePath
    }

    @Override
    String mainClass() {
        return mainClass
    }

    @Override
    TomcatConfig tomcatConfiguration() {
        return null
    }

    @Override
    Stream<ClasspathEntry> getArtifacts() {
        return project.configurations.getByName("compile").getDependencies().stream().map {
            new ClasspathEntry(new File(it.toString()), groupId().equals(it.group))
        }
    }

    @Override
    String groupId() {
        return null
    }

    @Override
    File buildDir() {
        return project.buildDir.toPath().resolve("jet/build").toFile()
    }

    @Override
    String finalName() {
        return "${artifactId()}-${version()}"
    }

    @Override
    File basedir() {
        return null
    }

    @Override
    File packageFilesDir() {
        return project.projectDir.toPath().resolve("src/main/jetresources/packagefiles").toFile()
    }

    @Override
    File execProfilesDir() {
        return null
    }

    @Override
    String execProfilesName() {
        return null
    }

    @Override
    String[] jvmArgs() {
        return new String[0]
    }
}

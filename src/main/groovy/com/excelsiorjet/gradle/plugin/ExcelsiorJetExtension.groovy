package com.excelsiorjet.gradle.plugin

import com.excelsiorjet.api.log.AbstractLog
import com.excelsiorjet.api.tasks.ClasspathEntry
import com.excelsiorjet.api.tasks.config.ExcelsiorInstallerConfig
import com.excelsiorjet.api.tasks.config.JetTaskConfig
import com.excelsiorjet.api.tasks.config.OSXAppBundleConfig
import com.excelsiorjet.api.tasks.config.SlimDownConfig
import com.excelsiorjet.api.tasks.config.TomcatConfig
import com.excelsiorjet.api.tasks.config.TrialVersionConfig
import org.gradle.api.Project

import java.util.stream.Stream

class ExcelsiorJetExtension implements JetTaskConfig {

    def String mainClass = 'HelloWorld'

    def Project project

    @Override
    String excelsiorJetPackaging() {
        return "zip"
    }

    @Override
    String artifactId() {
        return project.name
    }

    @Override
    String version() {
        return project.version.toString()
    }

    @Override
    String outputName() {
        return "HelloWorld"
    }

    @Override
    File jetOutputDir() {
        return new File("${project.buildDir}/jet")
    }

    @Override
    AbstractLog log() {
        return AbstractLog.instance()
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
    Stream<ClasspathEntry> getArtifacts() {
        return project.configurations.getByName("compile").getDependencies().stream().map {
            new ClasspathEntry(new File(it.toString()), groupId().equals(it.group))
        }
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
    String mainClass() {
        return mainClass
    }

    @Override
    File packageFilesDir() {
        return project.projectDir.toPath().resolve("src/main/jetresources/packagefiles").toFile()
    }

    @Override
    void setAddWindowsVersionInfo(boolean b) {

    }

    @Override
    boolean isAddWindowsVersionInfo() {
        return false
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
    OSXAppBundleConfig osxBundleConfiguration() {
        return null
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
    File tomcatInBuildDir() {
        return null
    }

    @Override
    File mainWar() {
        return null
    }

    @Override
    String jetHome() {
        return null
    }

    @Override
    TomcatConfig tomcatConfiguration() {
        return null
    }

    @Override
    String groupId() {
        return null
    }

    @Override
    File basedir() {
        return null
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

/*
 * Copyright (c) 2016 Excelsior LLC.
 *
 *  This file is part of Excelsior JET Gradle Plugin.
 *
 *  Excelsior JET Gradle Plugin is free software:
 *  you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Excelsior JET Gradle Plugin is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Excelsior JET Gradle Plugin.
 *  If not, see <http://www.gnu.org/licenses/>.
 *
*/
package com.excelsiorjet.gradle.plugin

import com.excelsiorjet.api.tasks.ApplicationType
import com.excelsiorjet.api.tasks.JetProject
import com.excelsiorjet.api.tasks.JetTaskFailureException
import com.excelsiorjet.api.tasks.config.ProjectDependency
import com.excelsiorjet.api.util.Txt
import org.gradle.api.DefaultTask

import java.security.MessageDigest

/**
 * Base class for ExcelsiorJet plugin tasks.
 *
 * @author Aleksey Zhidkov
 */
class AbstractJetTask extends DefaultTask {

    String jetHome = (project.excelsiorJet as ExcelsiorJetExtension).jetHome

    protected JetProject createJetProject() {
        ExcelsiorJetExtension ext = project.excelsiorJet as ExcelsiorJetExtension
        validateSettings()
        def jetProject = new JetProject(project.name, ext.getGroupId(), ext.getVersion(), ext.appType, project.buildDir, ext.getJetResourcesDir())

        // getters should be used to fallback into convention mapping magic, when field is not set
        jetProject.projectDependencies(getDependencies())
                .excelsiorJetPackaging(ext.getPackaging())
                .artifactName(ext.getArtifactName())
                .jetOutputDir(ext.getJetOutputDir())
                .mainClass(ext.getMainClass())
                .mainJar(ext.getMainJar())
                .mainWar(ext.getMainWar())
                .outputName(ext.getOutputName())
                .packageFilesDir(ext.getPackageFilesDir())
                .version(ext.getVersion())
                .hideConsole(ext.getHideConsole())
                .icon(ext.getIcon())
                .splash(ext.getSplash())
                .inlineExpansion(ext.getInlineExpansion())
                .stackTraceSupport(ext.getStackTraceSupport())
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
                .javaRuntimeSlimDown(ext.getJavaRuntimeSlimDown())
                .trialVersion(ext.getTrialVersion())
                .osxBundleConfiguration(ext.getOsxBundle())
                .multiApp(ext.getMultiApp())
                .profileStartup(ext.getProfileStartup())
                .profileStartupTimeout(ext.getProfileStartupTimeout())
                .protectData(ext.getProtectData())
                .cryptSeed(ext.getCryptSeed())
                .optRtFiles(ext.getOptRtFiles())
                .locales(ext.getLocales())
                .compilerOptions(ext.getCompilerOptions())
                .tomcatConfiguration(ext.getTomcat())
                .runArgs(ext.getRunArgs())
                .dependencies(ext.getDependencies())

        return jetProject
    }

    private List<ProjectDependency> getDependencies() {
        // due to bug in support of maven relocation in gradle, it's not merges the same artifact referenced by different names
        // https://issues.gradle.org/browse/GRADLE-2812
        // https://issues.gradle.org/browse/GRADLE-3301
        // https://discuss.gradle.org/t/oddity-in-the-output-of-dependencyinsight-task/7553/12
        // https://discuss.gradle.org/t/warning-after-gradle-update-1-9-with-pmd-plugin/1731/3
        def configuration = project.configurations.getByName("compile")
        def allDependencies = configuration.getResolvedConfiguration().getResolvedArtifacts().collect() {
            def module = it.moduleVersion.id.module
            return new ProjectDependency(module.group, module.name, it.moduleVersion.id.version, it.file, false)
        }
        def duplicatesByName = allDependencies.groupBy { it.path.name }
        duplicatesByName.values().
                findAll { it.size() > 1 && it.collect({ AbstractJetTask.hash(it.path) }).size() > 1 }.
                each {
                    logger.warn(Txt.s("JetApi.DuplicateFileName", it.collect { it.idStr() }))
                }
        return duplicatesByName.values().collect { it.first() }
    }

    private static String hash(File file) {
        def digest = MessageDigest.getInstance("SHA1")
        file.eachByte(4096) { buffer, length ->
            digest.update(buffer, 0, length)
        }
        return digest.digest().encodeHex() as String
    }

    private void validateSettings() {
        ExcelsiorJetExtension ext = project.excelsiorJet as ExcelsiorJetExtension
        if (ext.getAppType() == ApplicationType.TOMCAT) {
            if (ext.getIgnoreProjectDependencies()) {
                throw new JetTaskFailureException(s("JetApi.IgnoreProjectDependenciesShouldNotBeSetForTomcatApplications"));
            }
        }
    }

}

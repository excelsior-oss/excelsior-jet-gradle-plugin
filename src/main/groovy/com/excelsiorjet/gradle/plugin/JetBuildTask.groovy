/*
 * Copyright (c) 2016 Excelsior LLC.
 *
 *  This file is part of Excelsior JET Gradle Plugin.
 *
 *  Excelsior JET Maven Plugin is free software:
 *  you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Excelsior JET Maven Plugin is distributed in the hope that it will be useful,
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
import com.excelsiorjet.api.tasks.ClasspathEntry
import com.excelsiorjet.api.tasks.JetProject
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction

/**
 * Main task for building Java (JVM) applications with Excelsior JET.
 *
 * @see ExcelsiorJetExtension
 *
 * @author Aleksey Zhidkov
 */
class JetBuildTask extends DefaultTask {

    @TaskAction
    def jetBuild() {
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
                .excelsiorInstallerConfiguration(ext.getExcelsiorInstallerConfig())
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

        new com.excelsiorjet.api.tasks.JetBuildTask(jetProject).execute()
    }

    static List<ClasspathEntry> getDependencies(Project project) {
        def configuration = project.configurations.getByName("compile")
        return configuration.getResolvedConfiguration().getResolvedArtifacts().collect() {
            new ClasspathEntry(it.file, project.group.equals(it.moduleVersion.id.module.group))
        }
    }
}

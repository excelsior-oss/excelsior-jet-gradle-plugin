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

import com.excelsiorjet.api.tasks.JetProject
import com.excelsiorjet.api.util.Utils

import static com.excelsiorjet.api.util.Txt.s

/**
 * Base task for tasks that depend on build parameters of the plugin:
 * {@link JetBuildTask}, {@link JetProfileTask}, {@link JetRunTask), {@link JetCleanTask}.
 *
 * @see ExcelsiorJetExtension
 *
 * @author Aleksey Zhidkov
 * @author Nikita Lipsky
 */
abstract class AbstractBuildTask extends AbstractJetTask {

    @Override
    protected JetProject createJetProject() {
        JetProject result = super.createJetProject()
        checkDeprecated()
        return result
    }

    private void checkDeprecated() {
        ExcelsiorJetExtension ext = project.excelsiorJet as ExcelsiorJetExtension
        if (ext.getWinVIVersion() != null) {
            logger.warn(s("JetBuildTask.WinVIDeprecated.Warning", "winVIVersion", "version"))
            if (ext.windowsVersionInfo.version == null) {
                ext.windowsVersionInfo.version = ext.getWinVIVersion()
            }
        }
        if (ext.getWinVICopyright() != null) {
            logger.warn(s("JetBuildTask.WinVIDeprecated.Warning", "winVICopyright", "copyright"))
            if (ext.windowsVersionInfo.copyright == null) {
                ext.windowsVersionInfo.copyright = ext.getWinVICopyright()
            }
        }
        if (ext.getWinVIDescription() != null) {
            logger.warn(s("JetBuildTask.WinVIDeprecated.Warning", "winVIDescription", "description"))
            if (ext.windowsVersionInfo.description == null) {
                ext.windowsVersionInfo.description = ext.getWinVIDescription()
            }
        }
        if (!Utils.isEmpty(ext.getOptRtFiles())) {
            logger.warn(s("JetBuildTask.RTSettingDeprecated.Warning", "optRtFiles", "components ="))
            if (Utils.isEmpty(ext.runtime.components)) {
                ext.runtime.components = ext.getOptRtFiles()
            }
        }
        if (!Utils.isEmpty(ext.getLocales())) {
            logger.warn(s("JetBuildTask.RTSettingDeprecated.Warning", "locales", "locales ="))
            if (Utils.isEmpty(ext.runtime.locales)) {
                ext.runtime.locales = ext.getLocales()
            }
        }
        if (ext.getJavaRuntimeSlimDown().isDefined()) {
            logger.warn(s("JetBuildTask.RTSettingDeprecated.Warning", "javaRuntimeSlimDown", "slimDown {\n    }"))
            if (!ext.runtime.slimDown.isDefined()) {
                ext.runtime.slimDown = ext.getJavaRuntimeSlimDown()
            }
        }
        if (ext.getProfile() != null) {
            logger.warn(s("JetBuildTask.RTSettingDeprecated.Warning", "profile", "profile ="))
            if (ext.runtime.profile == null) {
                ext.runtime.profile = ext.getProfile()
            }
        }

        if (ext.getExecProfilesDir() != null) {
            logger.warn(s("JetBuildTask.ExecProfilesDeprecated.Warning", "execProfilesDir", "outputDir ="));
            if (ext.execProfiles.outputDir == null) {
                ext.execProfiles.outputDir = ext.getExecProfilesDir();
            }
        }
        if (ext.getExecProfilesName() != null) {
            logger.warn(s("JetBuildTask.ExecProfilesDeprecated.Warning", "execProfilesName", "outputName ="));
            if (ext.execProfiles.outputName == null) {
                ext.execProfiles.outputName = ext.getExecProfilesName();
            }
        }
    }

}

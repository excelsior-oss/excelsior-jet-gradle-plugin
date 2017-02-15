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

import com.excelsiorjet.api.ExcelsiorJet
import com.excelsiorjet.api.tasks.JetProject
import com.excelsiorjet.api.util.Utils
import org.gradle.api.tasks.TaskAction

import static com.excelsiorjet.api.log.Log.logger
import static com.excelsiorjet.api.util.Txt.s

/**
 * Main task for building Java (JVM) applications with Excelsior JET.
 *
 * @see ExcelsiorJetExtension
 *
 * @author Aleksey Zhidkov
 */
class JetBuildTask extends AbstractJetTask {

    @TaskAction
    def jetBuild() {
        ExcelsiorJet excelsiorJet = new ExcelsiorJet(jetHome)
        JetProject jetProject = createJetProject()
        checkDeprecated()
        new com.excelsiorjet.api.tasks.JetBuildTask(excelsiorJet, jetProject).execute()
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
    }

}

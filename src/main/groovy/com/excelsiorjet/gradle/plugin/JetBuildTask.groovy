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
import org.gradle.api.tasks.TaskAction

import static com.excelsiorjet.api.log.Log.logger
import static com.excelsiorjet.api.log.Log.logger
import static com.excelsiorjet.api.log.Log.logger
import static com.excelsiorjet.api.util.Txt.s
import static com.excelsiorjet.api.util.Txt.s
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
            logger.warn(s("JetBuildTask.WinVIDeprecated.Warning", "winVIVersion", "version"));
            ext.windowsVersionInfo.version = ext.getWinVIVersion()
        }
        if (ext.getWinVICopyright() != null) {
            logger.warn(s("JetBuildTask.WinVIDeprecated.Warning", "winVICopyright", "copyright"))
            ext.windowsVersionInfo.copyright = ext.getWinVICopyright()
        }
        if (ext.getWinVIDescription() != null) {
            logger.warn(s("JetBuildTask.WinVIDeprecated.Warning", "winVIDescription", "description"))
            ext.windowsVersionInfo.description = ext.getWinVIDescription()
        }
    }

}

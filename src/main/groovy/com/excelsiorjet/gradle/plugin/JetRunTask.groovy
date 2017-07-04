/*
 * Copyright (c) 2017 Excelsior LLC.
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
import com.excelsiorjet.api.tasks.RunTask
import org.gradle.api.tasks.TaskAction

/**
 * Task for running generated executables with Excelsior JET.
 *
 * @author Nikita Lipsky
 */
class JetRunTask extends ParentJetTask {

    @TaskAction
    def jetBuild() {
        ExcelsiorJet excelsiorJet = new ExcelsiorJet(jetHome)
        JetProject jetProject = createJetProject()
        new RunTask(excelsiorJet, jetProject).execute()
    }

}

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

import com.excelsiorjet.api.tasks.JetProject
import org.gradle.api.tasks.TaskAction

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
        JetProject jetProject = createJetProject()
        new com.excelsiorjet.api.tasks.JetBuildTask(jetProject).execute()
    }

}

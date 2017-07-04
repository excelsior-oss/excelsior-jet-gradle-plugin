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
import com.excelsiorjet.api.tasks.TestRunTask
import org.gradle.api.tasks.TaskAction

/**
 * Task for performing a Test Run before building the application.
 * Running your Java application before optimization helps Excelsior JET:
 * <ul>
 *  <li>
 *      Verify that your application can run on the Excelsior JET JVM flawlessly
 *      (i.e. it has no implicit dependencies on the Oracle JVM implementation
 *      and your Gradle project has no configuration issues specific to Excelsior JET).
 *  </li>
 *  <li>
 *      Collect profile information to optimize your app more effectively.
 *  </li>
 *  <li>
 *      Enable application startup time optimization.
 *      Performing a Test Run can reduce the startup time by a factor of up to two.
 *  </li>
 * </ul>
 * To perform a Test Run, issue the following Gradle command:
 * <p>
 * <code>
 *     gradlew jetTestRun
 * </code>
 * </p>
 * <p>
 * It is recommended to commit the collected profiles (.usg, .startup) to VCS so as to
 * enable the plugin to re-use them during subsequent builds without performing the Test Run.
 * The profiles are placed to {@link ExcelsiorJetExtension#jetResourcesDir} by default.
 * </p>
 *
 *  Note: During a Test Run, the application is executed in a special profiling mode,
 *        so disregard its modest start-up time and performance.
 *
 *
 * @see ExcelsiorJetExtension
 *
 * @author Aleksey Zhidkov
 */
class JetTestRunTask extends ParentJetTask {

    @TaskAction
    def jetBuild() {
        ExcelsiorJet excelsiorJet = new ExcelsiorJet(jetHome)
        JetProject jetProject = createJetProject()
        new TestRunTask(excelsiorJet, jetProject).execute()
    }

}

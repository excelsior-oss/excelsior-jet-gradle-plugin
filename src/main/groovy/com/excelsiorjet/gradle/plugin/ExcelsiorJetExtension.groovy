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

/**
 * Gradle Extension class for Excelsior JET Gradle Plugin.
 * Declares configuration parameters for all plugin tasks.
 *
 * @author Aleksey Zhidkov
 */
class ExcelsiorJetExtension {

    public static final String EXTENSION_NAME = "excelsiorJet"

    /**
     * Excelsior JET installation directory.
     * If unspecified, the plugin uses the following algorithm to set the value of this property:
     * <ul>
     *   <li> If the jet.home system property is set, use its value</li>
     *   <li> Otherwise, if the JET_HOME environment variable is set, use its value</li>
     *   <li> Otherwise scan the PATH environment variable for a suitable Excelsior JET installation</li>
     * </ul>
     */
    String jetHome

    /**
     * The main application class.
     */
    String mainClass

    /**
     * Application packaging mode. Permitted values are:
     * <dl>
     * <dt>zip</dt>
     * <dd>zip archive with a self-contained application package (default)</dd>
     * <dt>excelsior-installer</dt>
     * <dd>self-extracting installer with standard GUI for Windows
     * and command-line interface for Linux</dd>
     * <dt>osx-app-bundle</dt>
     * <dd>OS X application bundle</dd>
     * <dt>native-bundle</dt>
     * <dd>Excelsior Installer setups for Windows and Linux, application bundle for OS X</dd>
     * <dt>none</dt>
     * <dd>skip packaging altogether</dd>
     * </dl>
     */
    String excelsiorJetPackaging

    /**
     * Project group id. Unique identifier that can be shared by multiple projects.
     * Usually reverse domain name is used for group id such as "com.example".
     *
     * Default value is {@code ${project.group}}.
     */
    String groupId

    /**
     * Product version. Required for Excelsior Installer.
     *
     * Default value is {@code ${project.version}}.
     */
    String version

    /**
     * Target executable name. If not set, the main class name is used.
     */
    String outputName

    /**
     * Directory for temporary files generated during the build process
     * and the target directory for the resulting package.
     * <p>
     * The plugin will place the final self-contained package in the "app" subdirectory
     * of {@link #jetOutputDir}. You may deploy it to other systems using a simple copy operation.
     * For convenience, the plugin will also create a ZIP archive {@code ${project.build.finalName}.zip}
     * with the same content, if the {@code packaging} parameter is set to {@code zip}.
     * </p>
     */
    File jetOutputDir

    /**
     * The main application jar.
     * The default is the main project artifact, if it is a jar file.
     */
    File mainJar

    /**
     * Name of the final artifact of the project. Used as the default value for {@link #mainJar},
     * and to derive the default names of final artifacts created by {@link JetBuildTask} such as zip file, installer,
     * and so on.
     */
    String artifactName

    /**
     * Directory containing additional package files - README, license, media, help files, native libraries, and the like.
     * The plugin will copy its contents recursively to the final application package.
     * <p>
     * By default, the plugin assumes that those files reside in the {@code packagefiles} subdirectory of
     * {@link #jetResourcesDir} of your project, but you may also dynamically generate the contents
     * of the package files directory by means of other Gragle plugins.
     * </p>
     */
    File packageFilesDir

    /**
     * Directory containing Excelsior JET specific resource files such as application icons, installer splash,  etc.
     * It is recommended to place the directory in the source root directory.
     * The default value is "src/main/jetresources" subdirectory of the Gradle project.
     */
    File jetResourcesDir

    /**
     * (Windows) If set to {@code true}, the resulting executable file will not have a console upon startup.
     */
    boolean hideConsole

    /**
     * (Windows) .ico file to associate with the resulting executable file.
     *
     * Default value is "icon.ico" of {@link #jetResourcesDir} directory.
     */
    File icon
}

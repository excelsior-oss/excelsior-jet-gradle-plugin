package com.excelsiorjet.gradle.plugin

import com.excelsiorjet.api.tasks.ApplicationType

/**
 * Extension class for Excelsior JET plugin, that allows to set configuration parameters for all plugin tasks
 */
class ExcelsiorJetExtension {

    public static final String EXTENSION_NAME = "excelsiorJet"

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
     * Project groupId
     */
    String groupId

    /**
     * Product version. Required for Excelsior Installer.
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
     * of {@code jetOutputDir}. You may deploy it to other systems using a simple copy operation.
     * For convenience, the plugin will also create a ZIP archive {@code ${project.build.finalName}.zip}
     * with the same content, if the {@code packaging} parameter is set to {@code zip}.
     * </p>
     */
    File jetOutputDir

    /**
     * Application type.
     * By default {@code ApplicationType.PLAIN} used
     * @see ApplicationType
     */
    ApplicationType appType

    /**
     * The main application jar.
     * The default is the main project artifact, if it is a jar file.
     */
    File mainJar

    /**
     * Name of final executable file.
     * By default used "$artifactId-$version"
     */
    String finalName

    /**
     * Directory containing additional package files - README, license, media, help files, native libraries, and the like.
     * The plugin will copy its contents recursively to the final application package.
     * <p>
     * By default, the plugin assumes that those files reside in the "src/main/jetresources/packagefiles" subdirectory
     * of your project, but you may also dynamically generate the contents of the package files directory
     * by means of other Maven plugins such as {@code maven-resources-plugin}.
     * </p>
     */
    File packageFilesDir

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
     * Directory containing Excelsior JET specific resource files such as application icons, installer splash,  etc.
     * It is recommended to place the directory in the source root directory.
     * The default value is "src/main/jetresources" subdirectory of the Gradle project.
     */
    File jetResourcesDir
}

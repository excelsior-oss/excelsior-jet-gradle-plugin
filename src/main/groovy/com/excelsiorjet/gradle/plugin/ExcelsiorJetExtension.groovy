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
import com.excelsiorjet.api.tasks.config.DependencySettings
import com.excelsiorjet.api.tasks.config.DependencySettings
import com.excelsiorjet.api.tasks.config.ExcelsiorInstallerConfig
import com.excelsiorjet.api.tasks.config.OSXAppBundleConfig
import com.excelsiorjet.api.tasks.config.SlimDownConfig
import com.excelsiorjet.api.tasks.config.TomcatConfig
import com.excelsiorjet.api.tasks.config.TrialVersionConfig
import groovy.transform.PackageScope

/**
 * Gradle Extension class for Excelsior JET Gradle Plugin.
 * Declares configuration parameters for all plugin tasks.
 *
 * @author Aleksey Zhidkov
 */
class ExcelsiorJetExtension {

    public static final String EXTENSION_NAME = "excelsiorJet"

    // appType is package private property in Java terms
    @PackageScope
    ApplicationType appType

    @PackageScope
    ApplicationType getAppType() {
        return this.appType
    }

    @PackageScope
    void setAppType(ApplicationType appType) {
        this.appType = appType
    }

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
    String packaging

    /**
     * Project group id. Unique identifier that can be shared by multiple projects.
     * Usually reverse domain name is used for group id such as "com.example".
     *
     * Default value is {@code "project.group"}.
     */
    String groupId

    /**
     * Product version. Required for Excelsior Installer.
     *
     * Default value is {@code "project.version"}.
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
     * For convenience, the plugin will also create a ZIP archive {@code "project.build.finalName".zip}
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
     * The main web application archive.
     * The default is the main project artifact, if it is a war file.
     */
    File mainWar

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

    /**
     * Splash image to display upon application start up.
     *
     * By default, the file "splash.png" from the {@link #jetResourcesDir} folder is used.
     * If it does not exist, but a splash image is specified in the manifest
     * of the application JAR file, that image will be used automatically.
     */
    File splash

    /**
     * The JET Runtime supports three modes of stack trace printing: {@code minimal}, {@code full}, and {@code none}.
     * <p>
     * In the {@code minimal} mode (default), line numbers and names of some methods are omitted in call stack entries,
     * but the class names are exact.
     * </p>
     * <p>
     * In the {@code full} mode, the stack trace info includes all line numbers and method names.
     * However, enabling the full stack trace has a side effect - substantial growth of the resulting
     * executable size, approximately by 30%.
     * </p>
     * <p>
     * In the {@code none} mode, Throwable.printStackTrace() methods print a few fake elements.
     * It may result in performance improvement if the application throws and catches exceptions repeatedly.
     * Note, however, that some third-party APIs may rely on stack trace printing. One example
     * is the Log4J API that provides logging services.
     * </p>
     */
    String stackTraceSupport

    /**
     * Controls the aggressiveness of method inlining.
     * Available values are:
     *   {@code aggressive} (default), {@code very-aggressive}, {@code medium}, {@code low}, {@code tiny-methods-only}.
     * <p>
     * If you need to reduce the size of the executable,
     * set the {@code low} or {@code tiny-methods-only} option. Note that it does not necessarily worsen application performance.
     * </p>
     */
    String inlineExpansion;

    /**
     * The target location for application execution profiles gathered during Test Run.
     * By default, they are placed into the {@link #jetResourcesDir} directory.
     * It is recommended to commit the collected profiles (.usg, .startup) to VCS to enable the plugin
     * to re-use them during subsequent builds without performing a Test Run.
     *
     * @see JetTestRunTask
     */
    File execProfilesDir

    /**
     * The base file name of execution profiles. By default, ${project.artifactId} is used.
     *
     * Default value is ${project.artifactId}
     */
    String execProfilesName

    /**
     * Defines system properties and JVM arguments to be passed to the Excelsior JET JVM at runtime, e.g.:
     * {@code -Dmy.prop1 -Dmy.prop2=value -ea -Xmx1G -Xss128M -Djet.gc.ratio=11}.
     * <p>
     * Please note that only some of the non-standard Oracle HotSpot JVM arguments
     * (those prefixed with {@code -X}) are recognized.
     * For instance, the {@code -Xms} argument setting the initial Java heap size on HotSpot
     * has no meaning for the Excelsior JET JVM, which has a completely different
     * memory management policy. At the same time, Excelsior JET provides its own system properties
     * for GC tuning, such as {@code -Djet.gc.ratio}.
     * For more details, consult the {@code README} file of the plugin or the Excelsior JET User's Guide.
     * </p>
     */
    String[] jvmArgs = []

    /**
     * (Windows) If set to {@code true}, a version-information resource will be added to the final executable.
     *
     * @see #vendor vendor
     * @see #product product
     * @see #winVIVersion winVIVersion
     * @see #winVICopyright winVICopyright
     * @see #winVIDescription winVIDescription
     */
    boolean addWindowsVersionInfo = true

    /**
     * (Windows) Version number string for the version-information resource.
     * (Both {@code ProductVersion} and {@code FileVersion} resource strings are set to the same value.)
     * Must have {@code v1.v2.v3.v4} format where {@code vi} is a number.
     * If not set, {@code project.version} is used. If the value does not meet the required format,
     * it is coerced. For instance, "1.2.3-SNAPSHOT" becomes "1.2.3.0"
     *
     * @see #version version
     */
    String winVIVersion

    /**
     * (Windows) Legal copyright notice string for the version-information resource.
     * By default, {@code "Copyright © [inceptionYear],[curYear] [vendor]"} is used.
     */
    String winVICopyright

    /**
     * (Windows) File description string for the version-information resource.
     */
    String winVIDescription

    /**
     * Inception year of this project.
     *
     * Used to construct default value of {@link #winVICopyright}.
     */
    String inceptionYear

    /**
     * Application vendor name. Required for Windows version-information resource and Excelsior Installer.
     * By default, {@code project.group} is used, with first letter capitalized.
     */
    String vendor

    /**
     * Product name. Required for Windows version-information resource and Excelsior Installer.
     * By default, {@code project.name} is used.
     */
    String product

    /**
     * Excelsior Installer configuration parameters.
     *
     * @see ExcelsiorInstallerConfig#eula
     * @see ExcelsiorInstallerConfig#eulaEncoding
     * @see ExcelsiorInstallerConfig#installerSplash
     */
    ExcelsiorInstallerConfig excelsiorInstaller = new ExcelsiorInstallerConfig()

    def excelsiorInstaller(Closure closure) {
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = excelsiorInstaller
        closure()
    }

    /**
     * (32-bit only) If set to {@code true}, the Global Optimizer is enabled,
     * providing higher performance and lower memory usage for the compiled application.
     * Performing a Test Run is mandatory when the Global Optimizer is enabled.
     * The Global Optimizer is enabled automatically when you enable Java Runtime Slim-Down.
     *
     * @see JetTestRunTask
     * @see #javaRuntimeSlimDown
     */
    boolean globalOptimizer

    /**
     * (32-bit only) Java Runtime Slim-Down configuration parameters.
     *
     * @see SlimDownConfig#detachedBaseURL
     * @see SlimDownConfig#detachComponents
     * @see SlimDownConfig#detachedPackage
     */
    SlimDownConfig javaRuntimeSlimDown = new SlimDownConfig()

    def javaRuntimeSlimDown(Closure closure) {
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = javaRuntimeSlimDown
        closure()
    }
    /**
     * Trial version configuration parameters.
     *
     * @see TrialVersionConfig#expireInDays
     * @see TrialVersionConfig#expireDate
     * @see TrialVersionConfig#expireMessage
     */
    TrialVersionConfig trialVersion = new TrialVersionConfig()

    def trialVersion(Closure closure) {
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = trialVersion
        closure()
    }
    /**
     * OS X Application Bundle configuration parameters.
     *
     * @see OSXAppBundleConfig#fileName
     * @see OSXAppBundleConfig#bundleName
     * @see OSXAppBundleConfig#identifier
     * @see OSXAppBundleConfig#shortVersion
     * @see OSXAppBundleConfig#icon
     * @see OSXAppBundleConfig#developerId
     * @see OSXAppBundleConfig#publisherId
     */
    OSXAppBundleConfig osxBundle = new OSXAppBundleConfig()

    def osxBundle(Closure closure) {
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = osxBundle
        closure()
    }

    /**
     * If set to {@code true}, the multi-app mode is enabled for the resulting executable
     * (it mimicks the command line syntax of the conventional {@code java} launcher).
     */
    boolean multiApp = false

    /**
     * Enable/disable startup accelerator.
     * If enabled, the compiled application will run after build
     * for {@link #profileStartupTimeout} seconds for collecting a startup profile.
     */
    boolean profileStartup = true

    /**
     * The duration of the after-build profiling session in seconds. Upon exhaustion,
     * the application will be automatically terminated.
     */
    int profileStartupTimeout = 20

    /**
     * If set to {@code true}, enables protection of application data - reflection information,
     * string literals, and resource files packed into the executable, if any.
     *
     * @see #cryptSeed
     */
    boolean protectData

    /**
     * Sets a seed string that will be used by the Excelsior JET compiler to generate a key for
     * scrambling the data that the executable contains.
     * If data protection is enabled, but {@code cryptSeed} is not set explicitly, a random value is used.
     * <p>
     * You may want to set a {@code cryptSeed} value if you need the data to be protected in a stable way.
     * </p>
     *
     * @see #protectData
     */
    String cryptSeed

    /**
     * Add optional JET Runtime components to the package.
     * By default, only the {@code jce} component (Java Crypto Extension) is added.
     * You may pass a special value {@code all} to include all available optional components at once
     * or {@code none} to not include any of them.
     * Available optional components:
     * {@code runtime_utilities}, {@code fonts}, {@code awt_natives}, {@code api_classes}, {@code jce},
     * {@code accessibility}, {@code javafx}, {@code javafx-webkit}, {@code nashorn}, {@code cldr}
     */
    String[] optRtFiles = []

    /**
     * Add locales and charsets.
     * By default, only the {@code European} locales are added.
     * You may pass a special value {@code all} to include all available locales at once
     * or {@code none} to not include any of them.
     * Available locales and charsets:
     *    {@code European}, {@code Indonesian}, {@code Malay}, {@code Hebrew}, {@code Arabic},
     *    {@code Chinese}, {@code Japanese}, {@code Korean}, {@code Thai}, {@code Vietnamese}, {@code Hindi},
     *    {@code Extended_Chinese}, {@code Extended_Japanese}, {@code Extended_Korean}, {@code Extended_Thai},
     *    {@code Extended_IBM}, {@code Extended_Macintosh}, {@code Latin_3}
     */
    String[] locales = []

    /**
     * Additional compiler options and equations.
     * The commonly used compiler options and equations are mapped to plugin parameters,
     * so usually there is no need to specify them with this parameter.
     * However, the compiler also has some advanced options and equations that you may find
     * in the Excelsior JET User's Guide, and troubleshooting settings that the Excelsior JET
     * Support team may suggest.
     * You may enumerate such options and equations with this parameter and they will be appended to
     * the Excelsior JET project generated by {@link JetBuildTask}.
     * <b>Notice:</b> Care must be taken with using this parameter to avoid conflicts 
     * with other project parameters.
     */
    String[] compilerOptions = []

    /**
     * Tomcat web applications specific parameters.
     *
     * @see TomcatConfig#tomcatHome
     * @see TomcatConfig#warDeployName
     * @see TomcatConfig#hideConfig
     * @see TomcatConfig#genScripts
     */
    TomcatConfig tomcat = new TomcatConfig()

    def tomcat(Closure closure) {
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = tomcat
        closure()
    }

    /**
     * Command line arguments that will be passed to the application during startup accelerator profiling and the test run.
     * You may also set the parameter via the {@code jet.runArgs} system property, where arguments
     * are comma separated (use "\" to escape commas within arguments,
     * i.e. {@code -Djet.runArgs="arg1,Hello\, World"} will be passed to your application as {@code arg1 "Hello, World"})
     */
    String[] runArgs = []

    /**
     * List of external dependencies (that which have path and do not have groupId, artifactId and version)
     * and dependency settings (that which have no path, but have groupId or artifactId or version).
     */
    List<DependencySettings> dependencies = []

    def dependencies(Closure closure) {
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = this
        closure()
    }

    def dependency(Closure closure) {
        def dep = new DependencySettings()
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = dep
        closure()
        dependencies.add(dep)
    }

    /**
     * If set to to {@code true} project dependencies is ignored.
     */
    boolean ignoreProjectDependencies = false
}

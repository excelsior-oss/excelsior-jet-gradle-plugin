/*
 * Copyright (c) 2016-2017 Excelsior LLC.
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

import com.excelsiorjet.api.tasks.config.excelsiorinstaller.FileAssociation
import com.excelsiorjet.api.tasks.config.packagefile.PackageFile
import com.excelsiorjet.api.tasks.config.excelsiorinstaller.PostInstallCheckbox
import com.excelsiorjet.api.tasks.config.excelsiorinstaller.Shortcut
import com.excelsiorjet.api.tasks.config.dependencies.OptimizationPreset
import com.excelsiorjet.api.tasks.config.dependencies.DependencySettings
import com.excelsiorjet.api.tasks.config.excelsiorinstaller.ExcelsiorInstallerConfig
import com.excelsiorjet.api.tasks.config.OSXAppBundleConfig
import com.excelsiorjet.api.tasks.config.packagefile.PackageFileType
import com.excelsiorjet.api.tasks.config.runtime.RuntimeConfig
import com.excelsiorjet.api.tasks.config.runtime.SlimDownConfig
import com.excelsiorjet.api.tasks.config.TomcatConfig
import com.excelsiorjet.api.tasks.config.compiler.TrialVersionConfig
import com.excelsiorjet.api.tasks.config.windowsservice.WindowsServiceConfig
import com.excelsiorjet.api.tasks.config.compiler.WindowsVersionInfoConfig

/**
 * Gradle Extension class for Excelsior JET Gradle Plugin.
 * Declares configuration parameters for all plugin tasks.
 *
 * @author Aleksey Zhidkov
 */
class ExcelsiorJetExtension {

    public static final String EXTENSION_NAME = "excelsiorJet"

    /**
     * Application type. Permitted values are:
     * <dl>
     * <dt>plain</dt>
     * <dd>plain Java application, that runs standalone,
     * default type if {@code java} plugin applied</dd>
     * <dt>invocation-dynamic-library</dt>
     * <dd>dynamic library callable from a non-Java environment</dd>
     * <dt>windows-service</dt>
     * <dd>Windows service (Windows only)</dd>
     * <dt>tomcat</dt>
     * <dd>servlet-based Java application, that runs within Tomcat servlet container,
     * default type if {@code war} plugin applied</dd>
     * </dl>
     */
    String appType;

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
     *
     * @see #packageFiles
     */
    File packageFilesDir

    /**
     * If you only need to add a few additional package files,
     * it may be more convenient to specify them separately then to prepare {@link #packageFilesDir} directory.
     */
    List<PackageFile> packageFiles = []

    /**
     * Used to build Gradle sub DSLs for sub configurations like excelsiorInstaller().
     * The method calls the given closure with the given delegate telling Groovy resolve properties
     * from the delegate first. For example, if we have the following Gradle sub configuration of excelsiorJet{}:
     * <pre><code>
     *    excelsiorInstaller {
     *      language = "English"
     *    }
     * </code></pre>
     *
     * we should first create ExcelsiorInstallerConfig instance and supply it as delegate argument of applyClosure.
     * Then the "language" property will be set for the ExcelsiorInstallerConfig instance during above closure execution.
     *
     * @param closure closure to apply
     * @param delegate object which properties will be set during closure execution.
     */
    static def applyClosure(Closure closure, Object delegate) {
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = delegate
        closure()
    }

    def packageFiles(Closure closure) {
        applyClosure(closure, this)
    }

    def packageFile(Closure closure) {
        def pFile = new PackageFile(PackageFileType.AUTO)
        applyClosure(closure, pFile)
        packageFiles.add(pFile)
    }

    def file(Closure closure) {
        def pFile = new PackageFile(PackageFileType.FILE)
        applyClosure(closure, pFile)
        packageFiles.add(pFile)
    }

    def folder(Closure closure) {
        def pFile = new PackageFile(PackageFileType.FOLDER)
        applyClosure(closure, pFile)
        packageFiles.add(pFile)
    }
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
     * @see #windowsVersionInfo
     * @see WindowsVersionInfoConfig#company
     * @see WindowsVersionInfoConfig#product
     * @see WindowsVersionInfoConfig#version
     * @see WindowsVersionInfoConfig#copyright
     * @see WindowsVersionInfoConfig#description
     */
    Boolean addWindowsVersionInfo

    /**
     * Windows version-information resource description.
     */
    WindowsVersionInfoConfig windowsVersionInfo = new WindowsVersionInfoConfig()

    def windowsVersionInfo(Closure closure) {
        applyClosure(closure, windowsVersionInfo)
    }

    /**
     * Deprecated. Use {@link #windowsVersionInfo} parameter instead.
     */
    @Deprecated
    String winVIVersion

    /**
     * Deprecated. Use {@link #windowsVersionInfo} parameter instead.
     */
    @Deprecated
    String winVICopyright

    /**
     * Deprecated. Use {@link #windowsVersionInfo} parameter instead.
     */
    @Deprecated
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
    ExcelsiorInstallerConfig excelsiorInstaller = {
        def config = new ExcelsiorInstallerConfig()
        //init embedded configurations
        config.metaClass.afterInstallRunnable = {Closure closure ->
            applyClosure(closure, config.afterInstallRunnable)
        }
        config.metaClass.installationDirectory = {Closure closure ->
            applyClosure(closure, config.installationDirectory)
        }
        config.metaClass.uninstallCallback = {Closure closure ->
            applyClosure(closure, config.uninstallCallback)
        }

        config.shortcuts = new ArrayList()
        config.metaClass.shortcuts = {Closure closure ->
            applyClosure(closure, config)
        }
        config.metaClass.shortcut = {Closure closure ->
            def shortcut = new Shortcut()
            shortcut.icon = new PackageFile(PackageFileType.FILE)
            shortcut.metaClass.icon = {Closure iconClosure ->
                applyClosure(iconClosure, shortcut.icon)
            }
            applyClosure(closure, shortcut)
            config.shortcuts.add(shortcut)
        }

        config.postInstallCheckboxes = new ArrayList<>();
        config.metaClass.postInstallCheckboxes = {Closure closure ->
            applyClosure(closure, config)
        }
        config.metaClass.postInstallCheckbox = {Closure closure ->
            def postInstallCheckbox = new PostInstallCheckbox();
            applyClosure(closure, postInstallCheckbox)
            config.postInstallCheckboxes.add(postInstallCheckbox)
        }

        config.fileAssociations = new ArrayList()
        config.metaClass.fileAssociations = {Closure closure ->
            applyClosure(closure, config)
        }
        config.metaClass.fileAssociation = {Closure closure ->
            def fileAssociation = new FileAssociation()
            fileAssociation.icon = new PackageFile(PackageFileType.FILE)
            fileAssociation.metaClass.icon = {Closure iconClosure ->
                applyClosure(iconClosure, fileAssociation.icon)
            }
            applyClosure(closure, fileAssociation)
            config.fileAssociations.add(fileAssociation)
        }
        config
    }.call()

    def excelsiorInstaller(Closure closure) {
        applyClosure(closure, excelsiorInstaller)
    }

    /**
     * Windows Service configuration parameters.
     *
     * @see WindowsServiceConfig#name
     * @see WindowsServiceConfig#displayName
     * @see WindowsServiceConfig#description
     * @see WindowsServiceConfig#arguments
     * @see WindowsServiceConfig#logOnType
     * @see WindowsServiceConfig#allowDesktopInteraction
     * @see WindowsServiceConfig#startupType
     * @see WindowsServiceConfig#startServiceAfterInstall
     * @see WindowsServiceConfig#dependencies
     */
    WindowsServiceConfig windowsService = new WindowsServiceConfig();

    def windowsService(Closure closure) {
        applyClosure(closure, windowsService)
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
     * Runtime configuration parameters.
     *
     * @see RuntimeConfig#flavor
     * @see RuntimeConfig#profile
     * @see RuntimeConfig#components
     * @see RuntimeConfig#locales
     * @see RuntimeConfig#diskFootprintReduction
     * @see RuntimeConfig#location
     */
    RuntimeConfig runtime = {
        def config = new RuntimeConfig()
        //init embedded configuration
        config.slimDown = new SlimDownConfig()
        config.metaClass.slimDown = {Closure closure ->
            applyClosure(closure, config.slimDown)
        }
        config
    }.call()

    def runtime(Closure closure) {
        applyClosure(closure, runtime)
    }

    /**
     * Deprecated. Use {@link RuntimeConfig#profile} of {@link #runtime} parameter instead.
     */
    @Deprecated
    String profile

    /**
     * Deprecated. Use {@link RuntimeConfig#components} of {@link #runtime} parameter instead.
     */
    @Deprecated
    String[] optRtFiles = []

    /**
     * Deprecated. Use {@link RuntimeConfig#locales} of {@link #runtime} parameter instead.
     */
    @Deprecated
    String[] locales = []

    /**
     * Deprecated. Use {@link RuntimeConfig#slimDown} of {@link #runtime} parameter instead.
     */
    @Deprecated
    SlimDownConfig javaRuntimeSlimDown = new SlimDownConfig()

    def javaRuntimeSlimDown(Closure closure) {
        applyClosure(closure, javaRuntimeSlimDown)
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
        applyClosure(closure, trialVersion)
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
        applyClosure(closure, osxBundle)
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
        applyClosure(closure, tomcat)
    }

    /**
     * Command line arguments that will be passed to the application during startup accelerator profiling and the test run.
     * You may also set the parameter via the {@code jet.runArgs} system property, where arguments
     * are comma separated (use "\" to escape commas within arguments,
     * i.e. {@code -Djet.runArgs="arg1,Hello\, World"} will be passed to your application as {@code arg1 "Hello, World"})
     */
    String[] runArgs = []

    /**
     * Optimization presets define the default optimization mode for application dependencies.
     * There are two optimization presets available: {@code typical} and {@code smart}.
     *
     * <dl>
     * <dt>{@code typical} (default)</dt>
     * <dd>
     * Compile all classes from all dependencies to optimized native code.
     * </dd>
     * <dt>{@code smart}</dt>
     * <dd>
     * Use heuristics to determine which of the project dependencies are libraries and
     * compile them selectively, leaving the supposedly unused classes in bytecode form.
     * </dd>
     * </dl>
     * <p>
     * For details, refer to the Excelsior JET User's Guide, Chapter "JET Control Panel",
     * section "Step 3: Selecing a compilation mode / Classpath Grid / Selective Optimization".
     * </p>
     * <p>
     * <strong>Note:</strong> Unlike the identically named preset of the JET Control Panal,
     * selecting the {@code smart} preset does NOT automatically enable the Global Optimizer.
     * </p>
     *
     * @see #dependencies
     * @see DependencySettings
     * @see #globalOptimizer
     */
    String optimizationPreset = OptimizationPreset.TYPICAL.toString()

    /**
     * List of settings of project dependencies.
     *
     * @see DependencySettings#optimize
     * @see DependencySettings#protect
     * @see DependencySettings#pack
     * @see DependencySettings#isLibrary
     * @see DependencySettings#path
     * @see DependencySettings#packagePath
     */
    List<DependencySettings> dependencies = []

    def dependencies(Closure closure) {
        applyClosure(closure, this)
    }

    def dependency(Closure closure) {
        def dep = new DependencySettings()
        applyClosure(closure, dep)
        dependencies.add(dep)
    }

    /**
     * If set to to {@code true} project dependencies is ignored.
     */
    boolean ignoreProjectDependencies = false
}

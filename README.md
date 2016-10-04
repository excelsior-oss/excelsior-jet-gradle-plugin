[![Maven Central](https://img.shields.io/maven-central/v/com.excelsiorjet/excelsior-jet-gradle-plugin.svg)](https://maven-badges.herokuapp.com/maven-central/com.excelsiorjet/excelsior-jet-gradle-plugin)
Excelsior JET Gradle Plugin
=====

*Excelsior JET Gradle Plugin* provides Gradle users with an easy way to compile their applications
down to optimized native Windows, OS X, or Linux executables with [Excelsior JET](http://excelsiorjet.com).
Such precompiled applications start and often work faster, do not depend on the JRE,
and are as difficult to reverse engineer as if they were written in C++.

### Prerequisites
Before using this plugin, you need to install Excelsior JET.
You may find a fully functional evaluation version of Excelsior JET [here](http://www.excelsiorjet.com/evaluate).
It is free for evaluation purposes and the only limitation it has is that it expires 90 days
after installation, along with all compiled applications.

**Note:** Excelsior JET does not yet support cross-compilation, so you need to build your application on each target platform
separately. The supported platforms are Windows (32- and 64-bit), Linux (32- and 64-bit), and OS X (64-bit).


### Overview

This plugin will transform your application into an optimized native executable for the platform
on which you run Gradle, and place it into a separate directory together with all required
Excelsior JET runtime files. In addition, it can either pack that directory into a zip archive
(all platforms), create an Excelsior Installer setup (Windows and Linux only),
or create an OS X application bundle/installer.

The current version of the plugin can handle two types of applications:

* **Plain Java SE applications**, i.e. applications that have a main class
and have all their dependencies explicitly listed in the JVM classpath at launch time, and

* **Tomcat Web applications** &mdash; `.war` files that can be deployed to the
  Apache Tomcat application server.

In other words, if your application can be launched using a command line
of the following form:

```
java -cp [dependencies-list] [main class]
```

and loads classes mostly from jars that are present
in the `dependencies-list`, *or* if it is packaged into a `.war` file that can be deployed
to a Tomcat application server instance, then you can use this plugin.

Excelsior JET can also compile Eclipse RCP applications and create dynamic libraries (e.g. Windows DLLs)
callable via the Invocation API.
The plugin does not yet support projects of these types nor some advanced Excelsior JET features.
We plan to cover all that functionality in the future, but if you need the plugin to support
a particular feature sooner rather than later, you can help us prioritize the roadmap
by creating a feature request [here](https://github.com/excelsior-oss/excelsior-jet-gradle-plugin/issues).

### Usage
Excelsior JET Gradle plugin is hosted on Maven Central so at first
you need to add the plugin dependency in the `buildscript{}` configuration of the `build.gradle` file such as:

```gradle
buildscript {
    ext.jetPluginVersion = '0.7.2'
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath "com.excelsiorjet:excelsior-jet-gradle-plugin:$jetPluginVersion"
    }
}
```

and then apply and configure `excelsiorJet` plugin as:

```gradle
apply plugin: 'excelsiorJet'
excelsiorJet {
    mainClass = ''
}
```
set the `mainClass` parameter, and use the following command line to build the application:
```
gradlew jetBuild
```

**Note:** The Excelsior JET Gradle plugin requires the Java plugin be applied beforehand: ```apply plugin: 'java'```

For a Tomcat Web application, the `mainClass` parameter is not needed. Instead, you would need to add
the `tomcatHome` parameter pointing to a *clean* Tomcat installation, a copy of which will be used
for the deployment of your Web application at build time.
See [Building Tomcat Web Applications](#building-tomcat-web-applications) section below for more details.

### Excelsior JET Installation Directory Lookup

In order to do its job, the plugin needs to locate an Excelsior JET installation.
You have three ways to specify the Excelsior JET installation directory explicitly:

- add the `jetHome` parameter to the `excelsiorJet{}` plugin extension
- pass the `jet.home` system property on the Gradle command line as follows:
```
gradlew jetBuild -Djet.home=[JET-Home]
```
- or set the `JET_HOME` O/S environment variable

If none of above is set, the plugin searches for an Excelsior JET installation along the `PATH`.
So if you only have one copy of Excelsior JET installed, the plugin should be able to find it on Windows right away,
and on Linux and OS X - if you have run the Excelsior JET `setenv` script prior to launching Gradle.

### Build process

The native build is performed in the `jet` subdirectory of the Gradle build directory.
First, the plugin copies the main application jar to the `jet/build` directory,
and copies all its run time dependencies to `jet/build/lib`.
Then it invokes the Excelsior JET AOT compiler to compile all those jars into a native executable.
Upon success, it copies that executable and the required Excelsior JET Runtime files
into the `jet/app` directory, binds the executable to that copy of the Runtime,
and copies the contents of the `packageFilesDir` directory recursively
to `jet/app`, if applicable (see "Customizing Package Content" below.)

> Your natively compiled application is ready for distribution at this point: you may copy
> the contents of the `jet/app` directory to another computer that has neither Excelsior JET nor
> the Oracle JRE installed, and the executable should work as expected.

Finally, the plugin packs the contents of the `jet/app` directory into
a zip archive named `<artifactName>.zip` so as to aid single file re-distribution.
On Windows and Linux, you can also set the `packaging = 'excelsior-installer'`
configuration parameter to have the plugin create an Excelsior Installer setup instead,
and on OS X, setting `packaging = 'osx-app-bundle'` will result in the creation
of an application bundle and, optionally, a native OS X installer package (`.pkg` file).

### Performing a Test Run

The plugin can run your Java application on the Excelsior JET JVM
using a JIT compiler before pre-compiling it to native code. This so-called Test Run
helps Excelsior JET:

* verify that your application can be executed successfully on the Excelsior JET JVM.
Usually, if the Test Run completes normally, the natively compiled application also works well.
* detect the optional parts of Excelsior JET Runtime that are used by your application.
For instance, JavaFX Webkit is not included in the resulting package by default
due to its size, but if the application used it during a Test Run, it gets included automatically.
* collect profile information to optimize your app more effectively

To perform a Test Run, execute the following Gradle command:

```
gradlew jetTestRun
```

The plugin will place the gathered profiles in the `<project.projectDir>/src/main/jetresources` directory.
Incremental changes of application code do not typically invalidate the profiles, so
it is recommended to commit the profiles (`.usg`, `.startup`) to VCS to allow the plugin
to re-use them during automatic application builds without performing a Test Run.

It is recommended to perform a Test Run at least once before building your application.

**Note:** 64-bit versions of Excelsior JET do not collect `.usg` profiles yet.
  So it is recommended to perform a Test Run on the 32-bit version of Excelsior JET at least once.

The profiles will be used by the Startup Optimizer and the Global Optimizer (see below).

**Note:** During a Test Run, the application executes in a special profiling mode,
  so disregard its modest start-up time and performance.

**New in 0.7.2:**
Your application may require command line arguments to run. If that is the case, set the `runArgs` plugin parameter as follows:
```gradle
runArgs = ["arg1", "arg2"]
```
You may also pass the arguments via the `jet.runArgs` system property, where arguments are comma-separated (use "`\`" to escape commas within arguments, e.g. `-Djet.runArgs="arg1,Hello\, World"` will be passed to your application as `arg1 "Hello, World"`)

### Configurations other than `mainClass`

For a complete list of parameters, look into the Javadoc of field declarations
of the
[ExcelsiorJetExtension](https://github.com/excelsior-oss/excelsior-jet-gradle-plugin/blob/master/src/main/groovy/com/excelsiorjet/gradle/plugin/ExcelsiorJetExtension.groovy) class.
Most of them have default values derived from your `build.gradle` project,
such as the `outputName` parameter specifying the name of the resulting executable.

#### Application appearance
If the startup of your client application takes longer than you would have liked,
the thumb rule is to show a splash screen.
A splash screen provides visial feedback about the loading process to the end user, and
gives you an opportunity to display information about your product and company.
The splash screen functionality appeared in the Java API since Java SE 6. For more details, see
http://docs.oracle.com/javase/tutorial/uiswing/misc/splashscreen.html

If the splash image has been specified in the manifest of the application JAR file,
the respective image will be obtained automatically,
otherwise, you may assign a splash screen image to the application manually:

`splash = `&nbsp;*`splash-image-file`*

It is recommended to store the splash image in a VCS, and if you place it at
`<project.projectDir>/src/main/jetresources/splash.png`, you won't need to specify it
in the configuration explicitly. The plugin uses the location `<project.projectDir>/src/main/jetresources`
for other Excelsior JET-specific resource files (such as the EULA for Excelsior Installer setups).

There are also two useful Windows-specific configuration parameters:

`hideConsole = true` – hide console

`icon = `&nbsp;*`icon-file`*  – set executable icon (in Windows .ico format)

Just as it works for the splash image, if you place the icon file at
`<project.projectDir>/src/main/jetresources/icon.ico`, you won't need to specify it
in the configuration explicitly.

#### Dependencies Management

As mentioned [above](#build-process) the plugin automatically picks up and compiles runtime dependencies
of your Gradle project.
In addition, the plugin allows you to select how to compile your application at discretion of
the dependencies. That is, for each dependency, you can enable/disable:

- code protection for all classes
- selective compilation of classes
- packing resource files into executable

##### Dependencies configuration

If you need to set a certain property to a certain dependency add the following configuration 
section:

```gradle
dependencies {
   dependency {
       groupId = 'groupId'
       artifactId = 'artifactId'
       version = 'version'
       protect = ''
       optimize = ''
       pack = ''
   }
}
```

where `groupId`, `artifactId`, `version` describes the dependency in the same way as you describe the dependency in
a respective global `dependencies` section of the Gradle project,
and `protect`, `optimize`, `pack` are Excelsior JET specific properties for the dependency described below.
You may omit `groupId` or/and `version` from the configuration, if you are sure that there is the only dependency with a certain
`artifactId` in the project else the plugin will issue ambiguous dependency resolution error.
You may also set the `groupId` parameter only to set the same properties to all dependencies sharing the same `groupId`
at once.
Finally, if you need some additional dependencies to appear in the application classpath that are not listed
in the project explicitly (for example, you need to access some resources in a directory via `ResourceBundle.getResource()`)
set the `path` parameter instead of `groupId/artifactId/version` pointing to a directory or jar/zip.

##### Code protection

If you need to protect your classes from decompilers,
make sure that the respective dependency have the `protect` property set to `all` value.
If you do not need to protect classes for a certain dependency (f.i. it is a third-party library),
set the `not-required` value. It may reduce compilation time and the size of the resulting executable in some cases.

##### Selective optimization

To optimize all classes and all methods of every class of a dependency for performance, set the `optimize` parameter
to `all` value. The other option for the parameter is `auto-detect`.
It means that the Optimizer detects which classes from the dependency are used by the application
and compiles the dependency selectively leaving a part of classes in the bytecode form or not optimized.
It helps reduce compilation time and download size of the application.
You may enable selective optimization for third-party dependencies, if your application uses
a small part of their implementing classes. However, it is not recommended to choose the `auto-detect` option
for your own classes, because, in general, the Excelsior JET Optimizer cannot detect an exact set of used classes due to
possible access via Reflection API (though you can significantly help it to detect used classes performing
the [Test Run](#performing-a-test-run) prior to the build).

##### isLibrary hint

As mentioned above, it is recommended to set `optimize` parameter to `auto-detect` value
and `protect` parameter to `not-required` value for third party dependencies  while
it is better to set both parameters to `all` for your own classes.
You may provide a hint to the plugin if a dependency is a third party library setting `isLibrary` parameter to `true`.
This way the plugin will set `protect` to `not-required` and `optimize` to `auto-detect` automatically.
Consequently if you set `isLibrary` to `false` both parameters will be set to `all`.

By default, the plugin detects the hint automatically using the following strategy:
it treats all dependencies sharing the same `groupId` with your main artifact as your application classes
while all other dependencies are treated as third-party dependencies.
So if some of your application classes reside in a dependency with a different `groupId` make sure to set `isLibrary`
hint to `false` for such `groupId` to enable maximum protection and optimization level for it.

##### Resource packing

Dependencies often contain resource files such as images, icons, media files, etc.
By default, the JET Optimizer packs such files into the resulting executable.
If selective optimization is enabled for a dependency and protection is also disabled,
the classes not compiled are also packed into the executable and will be handled by the JIT compiler at run time
on attempt to load them. As a result, original jar files are no longer needed for the running application.
This is the default option for `pack` parameter named `auto-detect`.

Some third-party components may require presence of the original class files at run time.
For instance, third party security providers such as Bouncy Castle check the sizes of their class files during execution.
In such cases, the class files serve as both program code and resources.
Therefore, despite all the classes are pre-compiled, you have to make them available to the running application.
Setting the `pack` parameter to `all` for such a dependency resolves the problem.

You may also opt to not pack a dependency to the executable at all using `none` value for the `pack` parameter.
This way the dependency will be copied to the final package as is instead.
To control the placement of the dependency in the package use `packagePath` plugin parameter.
By default, non-packed jar files are copied to `lib` subfolder of the package while directories
(referenced by `path` parameter) are copied to the root of the package.

Finally, if you are sure that a certain dependency does not contain any resources and all classes of it were compiled,
you can disable copying of such a (non-packed) dependency to the package
setting `disableCopyToPackage` parameter to `true`.

##### Ignoring project dependencies

If you build your main artifact as so called fat jar
(using `com.github.johnrengelman.shadow` plugin for example) so all your project dependencies are packed
into your main artifact then the most-likely you do not need separate dependencies to be compiled with Excelsior JET
because all needed classes and resources are already inside of your main artifact.
For such a case you may set the `ignoreProjectDependencies` plugin parameter to `true`
to disable compilation of project dependencies.
This way you may set `protect/optimize/pack` properties for your main artifact only and for entries described with `path`
parameter of `dependencies` section of the plugin.
It gives you a complete freedom to specify what will be compiled by the plugin.

#### Customizing Package Content

By default, the final package contains just the resulting executable and the necessary Excelsior JET Runtime files.
However, you may want the plugin to add other files to it: README, license, media, help files,
third-party native libraries, and so on. For that, add the following configuration parameter:

`packageFilesDir = `&nbsp;*`extra-package-files-directory`*

referencing a directory with all such extra files that you need added to the package.
The contents of the directory will be copied recursively to the final package.

By default, the plugin assumes that the extra package files reside
in the `src/main/jetresources/packagefiles` subdirectory of your project,
but you may dynamically generate the contents of that directory by means of other Gradle plugins.

#### Excelsior Installer Configurations

The plugin supports the creation of Excelsior Installer setups -
conventional installer GUIs for Windows or self-extracting archives with command-line interface
for Linux.

To create an Excelsior Installer setup, add the following configuration into the `excelsiorJet{}` plugin
extension:

`packaging = excelsior-installer`

**Note:** if you use the same build.gradle for all three supported platforms (Windows, OS X, and Linux),
it is recommended to use another configuration:

`packaging = native-bundle`

to create Excelsior Installer setups on Windows and Linux and an application bundle and installer on OS X.

Excelsior Installer setup, in turn, has the following configurations:

* `product = `&nbsp;*` product-name`* - default is `<project.name>`

* `vendor = `&nbsp;*`vendor-name`* -  default is `<project.group>`

* `version = `&nbsp;*`product-version`* - default is `<project.version>`

The above parameters are also used by Windows Version Information and OS X bundle configurations.

To further configure the Excelsior Installer setup, you need to add the following configuration section:

```gradle
excelsiorInstaller {
}
```

that has the following configuration parameters:

* `eula = `&nbsp;*`end-user-license-agreement-file`* - default is `<project.projectDir>/src/main/jetresources/eula.txt`

* `eulaEncoding = `&nbsp;*`eula-file-encoding`* - default is `autodetect`. Supported encodings are `US-ASCII` (plain text), `UTF16-LE`

* `installerSplash = `&nbsp;*`installer-splash-screen-image`* - default is `<project.projectDir>/src/main/jetresources/installerSplash.bmp`

#### Creating OS X application bundles and installers

The plugin supports the creation of OS X application bundles and installers.

To create an OS X application bundle, add the following configuration into the `excelsiorJet{}` plugin extension:

`packaging = "osx-app-bundle"`

**Note:** if you use the same build.gradle for all three supported platforms (Windows, OS X, and Linux), it is recommended to use another configuration:

`packaging = "native-bundle"`

to create Excelsior Installer setups on Windows and Linux and an application bundle and installer on OS X.

To configure the OS X application bundle, you need to add the following configuration section:

```gradle
osxBundle {
}
```

The values of most bundle parameters are derived automatically from the other parameters of your `build.gradle`.
The complete list of the parameters can be obtained
[here](https://github.com/excelsior-oss/excelsior-jet-api/blob/master/src/main/java/com/excelsiorjet/api/tasks/config/OSXAppBundleConfig.java).

You still need to tell the plugin where the OS X icon (`.icns` file) for your bundle is located.
Do that using the `osxBundle.icon` parameter, or simply place the icon file at
`<project.projectDir>/src/main/jetresources/icon.icns` to let the plugin pick it up automatically.

By default, the plugin will create an OS X application bundle only,
but to distribute your application to your customers you probably need to sign it and package as an
OS X installer (`.pkg` file).
The plugin enables you to do that using the following parameters under `osxBundle` section:

* `developerId = `&nbsp;*`developer-identity-certificate`* - "Developer ID Application" or "Mac App Distribution" certificate name for signing resulting OSX app bundle with `codesign` tool.
* `publisherId = `&nbsp;*`publisher-identity-certificate`* - "Developer ID Installer" or "Mac Installer Distribution"
certificate name for signing the resulting OS X Installer Package (`.pkg` file) with the `productbuild` tool.

If you do not want to expose above parameters via `build.gradle`, you may pass them as system properties
to the `gradlew` command instead, using the arguments `-Dosx.developer.id` and `-Dosx.publisher.id` respectively.
 
**Troubleshooting:** If you would like to test the created installer file on the same OS X system on which
it was built, you need to first remove the OS X application bundle created by the plugin and located
next to the installer. Otherwise, the installer will overwrite that existing OS X application bundle
instead of installing the application into the `Applications` folder.

#### Windows Version-Information Resource Configurations

On Windows, the plugin automatically adds a
[version-information resource](https://msdn.microsoft.com/en-us/library/windows/desktop/ms646981%28v=vs.85%29.aspx)
to the resulting executable. This can be disabled by specifying the following
configuration:

    addWindowsVersionInfo = false

By default, the values of version-information resource strings are derived from project settings.
The values of `product` and `vendor` configurations are used verbatim as
`ProductName` and `CompanyName` respectively;
other defaults can be changed using the following configuration parameters:

  * `winVIVersion =`&nbsp;*`version-string`*

    Version number (both `FileVersion` and `ProductVersion` strings are set to this same value)

    **Notice:** unlike Gradle `project.version`, this string must have format `v1.v2.v3.v4`, where vi is a number.
    The plugin would use heuristics to derive a correct version string from the specified value if the latter
    does not meet this requirement, or from `project.version` if this configuration is not present.

  * `winVICopyright =`&nbsp;*`legal-copyright`*

    `LegalCopyright` string, with default value derived from other parameters

  * `winVIDescription =`&nbsp;*`executable-description`*

    `FileDescription` string, default is `project.name`

#### Stack trace support
The Excelsior JET Runtime supports three modes of stack trace printing: `minimal`, `full`, and `none`.

In the `minimal` mode (default), line numbers and names of some methods are omitted in call stack entries,
but class names are exact.

In the `full` mode, the stack trace info includes all line numbers and method names.
However, enabling the full stack trace has a side effect &mdash; substantial growth of the resulting executable in size, approximately by 30%.

In the `none` mode, `Throwable.printStackTrace()` methods print a few fake elements.
It may result in a performance improvement, if the application throws and catches exceptions repeatedly.
Note, however, that certain third-party APIs rely on stack trace printing. One example is the Log4J API that provides logging services.

To set the stack trace support mode, use the `stackTraceSupport` configuration parameter:

`stackTraceSupport =`&nbsp;*`stack-trace-mode`*

#### Method Inlining
When optimizing a Java program, the compiler often replaces method call statements with bodies of the methods
that would be called at run time. This optimization, known as method inlining, improves application performance,
especially when tiny methods, such as get/set accessors, are inlined.
However, inlining of larger methods increases code size, and its impact on performance may be uncertain.
To control the aggressiveness of method inlining, use the `inlineExpansion` plugin parameter:

`inlineExpansion =`&nbsp;*`inline-expasnion-mode`*

The available modes are:
  `aggressive` (default), `very-aggressive`, `medium`, `low`, and `tiny-methods-only`

If you need to reduce the size of the executable, opt for the `low` or `tiny-methods-only` setting.
Note that it does not necessarily worsen application performance.

#### Multi-app Executables

The plugin may compile more than one application into a single executable and
let you select a particular application at launch time via command line arguments.

The command line syntax of [multi-app executables](http://www.excelsior-usa.com/doc/jet/jetw011.html#0330)
is an extension of the `java` launcher command
line syntax that allows specifying the main class, VM options, Java system properties,
and the arguments of the application:

```
    Exe-name [Properties-and-options] Main-classname [App-arguments]
```

To enable the multi-app mode add the following configuration parameter:

`multiApp = true`

<a name="jvmargs"></a>
#### Defining System Properties and JVM Arguments

Unless you opted for multi-app executable generation, the resulting executable interprets
all its command line arguments as arguments of the main class you have specified.
In other words, there is no place on the application command line for an argument
setting a system property or altering JVM defaults, such as `-Dprop=value` or `-Xmx1G` .
To address this, the plugin enables you to hardwire system properties and JVM arguments
into the resulting executable using the following plugin configuration:

```gradle
jvmArgs = ["-Dprop=value", "-jvmArg"]
```

This configuration also affects Test Runs and can be used with multi-app executables as well
(relieving the user from the need to specify those arguments explicitly on the command line).

System property values may contain references to the *actual* root directory of the installed package
in the form of `$(Root)`.
For example, suppose the package contains a subdirectory `AppFiles`. You may define the following property:

`-Dmy.app.files.dir=$(Root)/AppFiles`

Then you prepare a package and install it into a certain directory on the target system.
Upon application startup, the JET Runtime replaces `$(Root)` with the absolute pathname of the installation directory.
Thus, when the installed application retrieves the value of the `my.app.files.dir` property,
it gets the full path to the `AppFiles` directory on the target system.

**Note:** most of the `-XX` options recognized by the Oracle JRE are not supported,
as they are specific to that HotSpot VM. Moreover, certain `-X` options are not supported either,
for example setting `-Xbootclasspath` or `-Xms` (initial Java heap size) makes no effect.

All in all, the JET Runtime recognizes the following standard JVM arguments:

`-ea, -da, -enableassertions, -disableassertions` - assertions control

`-esa, -dsa, -enablesystemassertions, -disablesystemassertions` - system assertions control

`-Xmx` - set maximum heap size

> **Note:** Setting maximum heap size to zero (default) enables adaptive heap sizing.
> Refer to the "Memory Management" section of the "Application Considerations" chapter 
> of the Excelsior JET User's Guide
> and [Knowledge Base Article #25](http://www.excelsiorjet.com/kb/25/)
> for more information.

`-Xss` - set maximum thread stack size

`-Xverify:all` - enable the strict verifier

`-XX:MaxDirectMemorySize` - set maximum memory size for direct buffers

`-javaagent:` - specify a Java Agent (for non-precompiled classes)

`-version` - print version information on startup

`-verbose:gc` - be verbose about garbage collection

The Excelsior JET Runtime also recognizes a handful of system properties controlling
its own behavior, such as `‑Djet.gc.ratio`.
For more information, consult the "Java System Properties / JET Runtime Specific Properties" section
of the "Application Considerations" chapter of the Excelsior JET User's Guide.

#### Startup Accelerator Configurations

The Startup Accelerator improves the startup time of applications compiled with Excelsior JET.
The plugin automatically runs the compiled application immediately after build,
collects the necessary profile information and hard-wires it into the executable just created.
The JET Runtime will then use the information to reduce the application startup time.
The Startup Accelerator is enabled by default, but you may disable it by specifying the following
configuration:

`profileStartup = false`

You may also specify the duration of the profiling session in seconds by specifying the following
configuration:

`profileStartupTimeout =`&nbsp;*`duration-in-seconds`*

As soon as the specified period elapses, profiling stops and the application is automatically terminated,
so ensure that the timeout value is large enough to capture all actions the application normally carries out
during startup. (It is safe to close the application manually if the profiling period proves to be excessively long.)

**New in 0.7.2:**
If your application requires command line arguments to run, set the `runArgs` plugin parameter
in the same way as for a [Test Run](#performing-a-test-run).

#### Global Optimizer

The 32-bit versions of Excelsior JET feature the Global Optimizer - a powerful facility that has several
important advantages over the default compilation mode:

* single component linking yields an executable that does not require the dynamic libraries
  containing the standard Java library classes,
  thus reducing the size of the installation package and the disk footprint of the compiled application
* global optimizations improve application performance and reduce the startup time and memory usage

By default, Excelsior JET uses the *dynamic link model*. It only compiles application classes, 
linking them into an executable that depends on dynamic libraries containing precompiled
Java SE platform classes. These dynamic libraries, found in the JET Runtime, have to be
distributed together with the executable.

The Global Optimizer detects the platform classes that are actually used by the application
and compiles them along with application classes into a single executable.
Even though the resulting binary occupies more disk space compared with the one built
in the default mode, it no longer requires the dynamic libraries with platform classes.
This results, among other benefits, in a considerable reduction of the application
installation package size.

To enable the Global Optimizer, add the following configuration parameter:

`globalOptimizer = true`

**Note:** performing a Test Run is mandatory if the Global Optimizer is enabled.

#### Optional Runtime Components Configurations

##### Locales and charsets
Additional locales and character encoding sets that may potentially be in use in the regions
where you distribute your application can be added to the package with the following configuration:

```gradle
locales = ["Locale"`, "Locale2"]
```

You may specify `["all"]` as the value of `locales` to add all locales and charsets at once or
`["none"]` to not include any of them.
The available sets of locales and encodings are:

`European`, `Indonesian`, `Malay`, `Hebrew`, `Arabic`, `Chinese`, `Japanese`, `Korean`, `Thai`,
`Vietnamese`, `Hindi`, `Extended_Chinese`, `Extended_Japanese`, `Extended_Korean`, `Extended_Thai`,
`Extended_IBM`, `Extended_Macintosh`, `Latin_3`

By default, only the `European` locales are added.

##### Optional components
To include optional JET Runtime components in the package, use the following configuration:

```gradle
optRtFiles = ["optRtFile1", "optRtFile2"]
```

You may specify `["all"]` as the value of `optRtFiles` to add all components at once or
`["none"]` to not include any of them.

The available optional components are:

`runtime_utilities`, `fonts`, `awt_natives`, `api_classes`, `jce`, `accessibility`, `javafx`, `javafx-webkit`, `nashorn`, `cldr`

*Note:* by default, the plugin automatically includes the optional components which the compiler detected
   as used when building the executable(s).

#### Java Runtime Slim-Down Configurations

The 32-bit versions of Excelsior JET feature Java Runtime Slim-Down, a unique
Java application deployment model delivering a significant reduction
of application download size and disk footprint.

The key idea is to select the components of the Java SE API that are not used by the application,
and exclude them from the installation altogether. Such components are called *detached*.
For example, if your application does not use any of Swing, AWT, CORBA or, say, JNDI API,
Excelsior JET enables you to easily exclude from the main setup package the standard library
classes implementing those APIs and the associated files, placing them in a separate *detached package*.

The detached package should be placed on a Web server so that the JET Runtime could download it
if the deployed application attempts to use any of the detached components via JNI or the Reflection API.

To enable Java Runtime Slim-Down, copy and paste the following plugin configuration:

```gradle
javaRuntimeSlimDown {
    detachedBaseURL = ''
}
```

and specify the base URL of the location where you plan to place the detached package, e.g.
`http://www.example.com/download/myapp/detached/`.

By default, the plugin automatically detects which Java SE APIs your application does not use
and detaches the respective JET Runtime components from the installation package.
Alternatively, you may enforce detaching of particular components using the following parameter
under the `javaRuntimeSlimDown` configuration section:

`detachComponents =`&nbsp;*`comma-separated list of APIs`*

Available detachable components: `corba, management, xml, jndi, jdbc, awt/java2d, swing, jsound, rmi, jax-ws`

At the end of the build process, the plugin places the detached package in the `jet` subdirectory
of the Gradle target build directory. You may configure its name with the `detachedPackage` parameter
of the `javaRuntimeSlimDown` section (by default the name is `artifactName.pkl`).

Do not forget to upload the detached package to the location specified in `detachedBaseURL`
above before deploying your application to end-users.

**Note:** Enabling Java Runtime Slim-Down automatically enables the Global Optimizer, 
          so performing a Test Run is mandatory for Java Runtime Slim-Down as well.

**Fixed issue:** Java Runtime Slim-Down did not work with the `excelsior-installer` packaging type 
                 due to a bug in Excelsior JET. This issue is fixed in Excelsior JET 11 Maintenance Pack 2.

#### Creating Trial Versions

You can create a trial version of your Java application that will expire in a specified number of days
after the build date of the executable, or on a fixed date.
Once the trial period is over, the application will refuse to start up,
displaying a custom message.

To enable trial version generation, copy and paste into your `build.gradle` file the following plugin configuration:

```gradle
trialVersion {
    expireInDays = 0
    expireMessage = ''
}
```

and specify the number of calendar days after the build date when you want the application
to expire, and the error message that the expired binary should display to the user on a launch attempt.

You can also set a particular, fixed expiration date by using the `expireDate` parameter
instead of `expireInDays`. The format of the `expireDate` parameter value
is *ddMMMyyyy*, for example `15Sep2020`.

**Note:** If you choose the `"excelsior-installer"` `packaging` type, the generated setup
package will also expire, displaying the same message to the user.

One common usage scenario of this functionality is setting the hard expiration date further into the future,
while using some other mechanism to enforce a (shorter) trial period.
Typically, you would set the hard expiration date somewhat beyond the planned release 
date of the next version of your application. This way, you would ensure that nobody uses
an outdated trial copy for evaluation.

#### Data protection

If you do not wish constant data, such as reflection info, Java string literals, or packed resource files,
to be visible in the resulting executable, enable data protection by specifying the following configuration:

`protectData = true`

For more details on data protection, refer to the "Data Protection" section of
the "Intellectual Property Protection" chapter of the Excelsior JET User's Guide.

#### Additional Compiler Options and Equations
The commonly used compiler options and equations are mapped to the parameters of the plugin.
However the compiler also has some advanced options and equations that you may find in the
Excelsior JET User's Guide, plus some troubleshooting settings that the Excelsior JET Support
team may suggest you to use.
You may enumerate such options using the `compilerOptions` configuration, for instance:

```gradle
compilerOptions = ["-disablestacktrace+", "-inlinetolimit=200"]
```

These options will be appended to Excelsior JET project file generated by the plugin.

**Notice:** Care must be taken with using this parameter to avoid conflicts
with other project parameters.

### Building Tomcat Web Applications

The plugin enables you to compile Apache Tomcat together with your Web applications down
to a native binary using Excelsior JET. Compared to running your
application on a conventional JVM, this has the following benefits:

* More predictable latency for your Web application, as no code de-optimizations
  may occur suddenly at run time

* Better startup time, which may be important if you need to launch a multitude of microservices
  upon updating your distributed application.

* Better initial performance that remains stable later on, which can be important
  for load balancing inside an application cluster

* Security and IP protection, as reverse engineering of sensitive application code
  becomes much more expensive and the exposure of yet unknown to you security vulnerabilities is reduced

#### Supported Tomcat versions
Excelsior JET 11 supports Apache Tomcat 5.0.x (starting from version 5.0.1), 5.5.x, 6.0.x,
and 7.0.x up to version 7.0.62.
The next version of Excelsior JET will support Tomcat 8 and Tomcat 7.0.63+ versions.
For now, please stick to Tomcat 7.0.62 or earlier.

#### Usage
The plugin will treat your Gradle project as a Tomcat Web application project if the `war` plugin is applied **before** the `excelsiorJet` plugin.
To enable native compilation of your Tomcat Web application, you need to add the plugin dependency to the `buildscript` configuration of the `build.gradle` file, e.g.:

```gradle
buildscript {
    def jetPluginVersion = '0.7.2'
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath "com.excelsiorjet:excelsior-jet-gradle-plugin:$jetPluginVersion"
    }
}
```

then apply and configure the `excelsiorJet` plugin as follows:

```gradle
apply plugin: 'excelsiorJet'
excelsiorJet {
    tomcat {
        tomcatHome = ""
    }
}
```

and then set the `tomcatHome` parameter, which has to point to the *master* Tomcat installation &mdash; basically,
a clean Tomcat instance that was never launched.

You may also set the above parameter by passing the `tomcat.home` system property on the Gradle command line as follows:

```
gradlew jetBuild -Dtomcat.home=[Tomcat-Home]
```

or set the `TOMCAT_HOME` or `CATALINA_HOME` environment variables.

**NOTICE:** The binary distributions of Tomcat that are available from http://tomcat.apache.org/ usually contain
a set of standard examples in the `webapps` directory, which are most likely not needed in your own application distribution.
So it is safe to remove them from the `webapps` directory of the master Tomcat installation, making it empty.


#### Build process
During the build of your application, the plugin first copies the master Tomcat installation to the `jet/build` subdirectory.
Then it copies your main project artifact (`.war` file) to the `webapps` subdirectory of that copy,
and compiles it all together into a native executable.

Upon success, the plugin creates a directory structure similar to that of the master Tomcat installation in the `jet/app` directory,
placing the executable into the `jet/app/bin` subdirectory. It also copies the required Excelsior JET Runtime files
into the `jet/app` directory and binds the resulting executable to that copy of the Runtime.

> Your natively compiled Tomcat application is ready for distribution at this point: you may copy
> the contents of the `jet/app` directory to another computer that has neither Excelsior JET nor
> the Oracle JRE installed, and the executable should work as expected.
> You may also run your application using standard Tomcat scripts that are placed into the resulting
> `jet/app/bin` folder by default.

Finally, the plugin packs the contents of the `jet/app` directory into
a zip archive named `<artifactName>.zip` so as to aid single file re-distribution.
Other packaging types that are available for plain Java SE applications are supported for Tomcat as well (see above).

#### Tomcat configuration parameters
Most configuration parameters that are available for plain Java SE applications listed above
are also available for Tomcat web applications. There are also a few Tomcat-specific configuration parameters that
you may set within the `tomcat{}` parameters section:

* `warDeployName` - the name of the war file to be deployed into Tomcat.
   By default, Tomcat uses the name of the war file as the context path of the respective web application.
   If you need your web application to be on the "/" context path, set `warDeployName` to `ROOT` value.

* `hideConfig` - if you do not want your end users to inspect or modify the Tomcat configuration files
  located in `<tomcatHome>/conf/`, set this plugin parameter to `true`
  to have those files placed inside the executable, so they will not appear in the `conf/` subdirectory
  of end user installations of your Web application.

    **Important:**  For Tomcat to start your Web applications with hidden configuration files,
    you need to either mark the `conf/tomcat-users.xml` file read-only, or move it away from
    the `conf/` directory. If you opt for the latter, that file would remain visible, of course.

    You can do the above respectively by adding the attribute `readonly="true"` to the tag
    `<Resource name="UserDatabase">` in the `conf/server.xml` file of the master Tomcat installation,
    or modifying the `pathname` attribute of that tag. For example:
```
<Resource name="UserDatabase" auth="Container"
 type="org.apache.catalina.UserDatabase"
 description="User database that can be updated and saved"
 factory="org.apache.catalina.users.MemoryUserDatabaseFactory"
 pathname="conf/tomcat-users.xml"
 readonly="true"/>
```
  Also, you would likely want to pre-deploy the XML descriptors of your Web applications
  to `conf/<Engine>/<Host>`. Otherwise, Tomcat will extract those XML files
  from applications and place them in the `conf/` directory on startup,
  thus negating the effect of hiding.

* `genScripts` - you may continue to use the standard Tomcat scripts such as `bin/startup`
  and `bin/shutdown` with the natively compiled Tomcat, as by default
  the respective scripts are created in `jet/app/bin` along with the executable.
  However, if you are going to launch the created executable directly, you may set
  the `genScripts` parameter to `false`.

#### Multiple Web applications and Tomcat installation configuration
Excelsior JET can also compile multiple Web applications deployed onto a single Tomcat instance.

To do this with the help of this plugin, you need to do the following:

* Determine what is the *last* Web application in your build process and add the above Excelsior JET
  plugin configuration to its Gradle project.

* To the projects of all other Web applications, add a file copy operation that would copy the
  final `.war` artifact into the `webapps` subdirectory of the master Tomcat installation of
  your last Web application project.

This way, the Excelsior JET AOT compiler will pick up all the Web applications that were built earlier
and compile them into the same executable as the last one.

If you need to add or change some Tomcat configurations specific to your applications,
such as DB configurations, simply make the respective changes in the master Tomcat installation.
Similarly, if you need any additional files included in the resulting installation package, you can
place them in the master Tomcat installation as well: the plugin will copy them into the final package
automatically.

#### Test Run of a Tomcat Web application

You can launch your Tomcat Web application on Excelsior JET JVM using a JIT compiler
before pre-compiling it to native code using the `jetTestRun` task the same way as with plain Java SE applications.

However, please note that a running Tomcat instance would not terminate until you run its standard `shutdown` script.
Technically, you can terminate it using <key>Ctrl-C</key>, but that would terminate the entire Gradle build
and would not constitute a correct Tomcat termination.
So it is recommended to use the standard Tomcat `shutdown` script for correct Tomcat termination at the end
of a Test Run. You may launch it from any standard Tomcat installation.

## Sample Project

To demonstrate the process and result of plugin usage, we have forked the [Pax Britannica](https://github.com/libgdx/libgdx-demo-pax-britannica) Libgdx demo project on GitHub,
added the Excelsior JET plugin to its `build.gradle` file, and run it through Gradle to build native binaries for three platforms.

You can download the binaries from here:

* [Windows (32-bit, 27MB installer)](http://www.excelsior-usa.com/download/jet/gradle/pax-britannica-windows-x86.exe)
* [OS X (64-bit, 50MB installer)](http://www.excelsior-usa.com/download/jet/gradle/pax-britannica-osx-amd64.pkg)
* [Linux (64-bit, 37MB installer)](http://www.excelsior-usa.com/download/jet/gradle/pax-britannica-linux-amd64.bin)

or clone [the project](https://github.com/excelsior-oss/libgdx-demo-pax-britannica) and build it yourself:

```
    git clone https://github.com/excelsior-oss/libgdx-demo-pax-britannica
    cd libgdx-demo-pax-britannica
    gradlew :desktop:jetBuild
```

## Release Notes
Version 0.8.0 (??-Oct-2016)

The release adds the capability to set Excelsior JET specific properties for project dependencies such as
code protection, selective optimization and resource packing.

Version 0.7.2 (19-Aug-2016)

This release adds the capability to pass commmand-line arguments to the application
during startup profiling and the test run.

Version 0.7.1 (10-Aug-2016)

This release covers most of the compiler options that are available in the JET Control Panel UI,
and all options of the `xpack` utility as of Excelsior JET 11.0 release:

  * `splash` parameter introduced to control the appearance of your application on startup
  * `inlineExpansion` parameter introduced to control aggressiveness of methods inlining
  * `stackTraceSupport` parameter introduced to set stack trace support level
  * `compilerOptions` parameter introduced to set advanced compiler options and equations
  * `locales` parameter introduced to add additional locales and charsets to the resulting package

Version 0.7.0 (12-Jul-2016)

* Compilation of Tomcat Web applications is supported

Version 0.3.0 (06-Jul-2016)

* Support of Excelsior Installer setup generation
* Windows Version Information generation
* Support of multi-app executables
* Startup Accelerator supported and enabled by default
* Test Run Task implemented that enables:
   - running an application on the Excelsior JET JVM before pre-compiling it to native code
   - gathering application execution profiles to enable the Startup Optimizer
* `optRtFiles` parameter introduced to add optional JET runtime components
* Reduced the download size and disk footprint of resulting packages by means of supporting:
   * Global Optimizer
   * Java Runtime Slim-Down
* `packageFilesDir` parameter introduced to add extra files to the final package
* Trial version generation is supported
* `jvmArgs` parameter introduced to define system properties and JVM arguments
* `protectData` parameter added to enable data protection
* Mac OS X application bundles and installers support

Version 0.1.0 (24-Jun-2016)
* Initial release supporting compilation of the Gradle Project with all dependencies into native executable
and placing it into a separate directory with required Excelsior JET runtime files.

## Roadmap

Even though we are going to base the plugin development on your feedback in the future, we have our own short-term plan as well.
So the next few releases will add the following features:

* Windows services support.
* Multi-component support: building dependencies into separate native libraries
                           to reuse them across multiple Gradle project builds
                           so as to reduce overall compilation time
* Code signing.

Note that the order of appearance of these features is not fixed and can be adjusted based on your feedback.

package com.excelsiorjet.gradle.plugin

import com.excelsiorjet.api.tasks.ClasspathEntry
import org.gradle.api.Project

import java.util.stream.Stream

class ExcelsiorJetExtension {

    def String mainClass = 'HelloWorld'

    def Project project

    String excelsiorJetPackaging() {
        return "zip"
    }

    String artifactId() {
        return project.name
    }

    String version() {
        return project.version.toString()
    }

    String outputName() {
        return "HelloWorld"
    }

    File jetOutputDir() {
        return new File("${project.buildDir}/jet")
    }

    String packaging() {
        return "jar"
    }

    File mainJar() {
        return project.tasks.getByPath(":jar").archivePath
    }

    Stream<ClasspathEntry> getArtifacts() {
        return project.configurations.getByName("compile").getDependencies().stream().map {
            new ClasspathEntry(new File(it.toString()), project.group.equals(it.group))
        }
    }

    File buildDir() {
        return project.buildDir.toPath().resolve("jet/build").toFile()
    }

    String finalName() {
        return "${artifactId()}-${version()}"
    }

    String mainClass() {
        return mainClass
    }

    File packageFilesDir() {
        return project.projectDir.toPath().resolve("src/main/jetresources/packagefiles").toFile()
    }

}

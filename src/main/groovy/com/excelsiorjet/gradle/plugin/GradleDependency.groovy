package com.excelsiorjet.gradle.plugin

import com.excelsiorjet.api.Artifact
import org.gradle.api.artifacts.Dependency

/**
 * Created by azhidkov on 03/06/16.
 */
class GradleDependency implements Artifact {

    private Dependency dep

    GradleDependency(Dependency dep) {
        this.dep = dep
    }

    @Override
    File getFile() {
        return new File(dep.toString())
    }

    @Override
    String getGroupId() {
        return dep.group
    }

}

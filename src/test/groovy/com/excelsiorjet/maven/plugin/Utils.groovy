package com.excelsiorjet.maven.plugin

import java.nio.file.Files
import java.nio.file.Path

class Utils {

    static def copyDirectoryContents(Path from, Path to) {
        from.eachFileRecurse {
            Path src = (it instanceof File) ? it.toPath() : it
            def relativePath = from.relativize(src)
            def dst = to.resolve(relativePath)
            if (Files.isDirectory(src)) {
                Files.createDirectories(dst)
            } else {
                Files.copy(src, dst)
            }
        }
    }

}

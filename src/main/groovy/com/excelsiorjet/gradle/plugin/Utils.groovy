package com.excelsiorjet.gradle.plugin

class Utils {

    /**
     * Tries to guess groupId by given sources root.
     * If given sources root is not directory or do not contain subdirectories returns {@code null}.
     * Otherwise group id is formed from first subdirectory and, if exists, first subdirectory if that directory names joined with '.'.
     */
    public static String guessGroupId(File sourcesRoot) {
        if (!sourcesRoot.isDirectory()) {
            return null
        }

        def firstLevelDirs = sourcesRoot.listFiles({File f -> f.isDirectory()} as FileFilter)
        if (firstLevelDirs == null || firstLevelDirs.size() == 0) {
            return null
        }
        def firstDir = firstLevelDirs.first()

        def secondLevelDirs = firstDir.listFiles({File f -> f.isDirectory()} as FileFilter)
        if (secondLevelDirs == null || secondLevelDirs.size() == 0) {
            return firstDir.name
        }
        def secondDir = secondLevelDirs.first()

        return "${firstDir.name}.${secondDir.name}"
    }

    private Utils() {
    }
}

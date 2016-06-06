package com.excelsiorjet.gradle.plugin

import com.excelsiorjet.api.AbstractLog

class GradleLog extends AbstractLog {

    @Override
    void info(CharSequence msg) {
        println msg
    }

    @Override
    void warn(CharSequence msg) {
        println msg
    }

    @Override
    void warn(CharSequence msg, Throwable t) {
        println msg
    }

    @Override
    void error(CharSequence msg) {
        System.err.println(msg)
    }
}

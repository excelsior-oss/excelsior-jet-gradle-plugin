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

import com.excelsiorjet.api.log.Log
import org.gradle.api.logging.Logger

/**
 * Implementation of {@link Log} that redirects logs into Gradle logging system.
 */
class GradleLog extends Log {

    private final Logger log;

    GradleLog(Logger log) {
        this.log = log
    }

    @Override
    void debug(String msg, Throwable t) {
        log.debug(msg, t)
    }

    @Override
    void info(String msg) {
        log.quiet(msg)
    }

    @Override
    void warn(String msg) {
        log.warn(msg)
    }

    @Override
    void warn(String msg, Throwable t) {
        log.warn(msg, t)
    }

    @Override
    void error(String msg) {
        log.error(msg)
    }

}

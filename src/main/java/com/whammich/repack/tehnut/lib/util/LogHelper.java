package com.whammich.repack.tehnut.lib.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogHelper {

    private final Logger logger;
    private final boolean enabled;

    public LogHelper(Logger logger, boolean enabled) {
        this.logger = logger;
        this.enabled = enabled;
    }

    public LogHelper(Logger logger) {
        this(logger, true);
    }

    public LogHelper(String logger, boolean enabled) {
        this(LogManager.getLogger(logger), enabled);
    }

    public LogHelper(String logger) {
        this(logger, true);
    }

    public void info(String info, Object... format) {
        if (enabled)
            logger.info(info, format);
    }

    public void error(String error, Object... format) {
        if (enabled)
            logger.info(error, format);
    }

    public void debug(String debug, Object... format) {
        if (enabled)
            logger.info(debug, format);
    }

    public void severe(String severe, Object... format) {
        logger.fatal(severe, format);
    }

    public Logger getLogger() {
        return logger;
    }
}

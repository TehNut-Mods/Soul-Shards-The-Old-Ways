package com.whammich.sstow.util;

import com.whammich.sstow.SoulShardsTOW;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Deprecated
public final class ModLogger {
    private static Logger logger = LogManager.getLogger(SoulShardsTOW.MODID);

    public static void logInfo(String message) {
        logger.log(Level.INFO, message);
    }

    public static void logWarn(String message) {
        logger.log(Level.WARN, message);
    }

    public static void logFatal(String message) {
        logger.log(Level.FATAL, message);
    }

    public static void logDebug(String message) {
        logger.log(Level.DEBUG, message);
    }

    public static void log(Level level, String message) {
        logger.log(level, message);
    }
}
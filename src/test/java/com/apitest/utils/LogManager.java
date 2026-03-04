package com.apitest.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LogManager {

    private static final Logger logger = Logger.getLogger("APITestLogger");
    private static FileHandler fileHandler;

    static {
        try {
            Files.createDirectories(Paths.get("logs"));
            fileHandler = new FileHandler("logs/test-execution.log", true);
            fileHandler.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord record) {
                    return String.format("[%1$tF %1$tT] [%2$-7s] %3$s%n",
                            record.getMillis(),
                            record.getLevel().getName(),
                            record.getMessage());
                }
            });
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            System.err.println("Failed to initialize file logger: " + e.getMessage());
        }
    }

    public static void info(String message) {
        logger.info(message);
        System.out.println("[INFO]  " + message);
    }

    public static void pass(String message) {
        logger.info("[PASS]  " + message);
        System.out.println("[PASS]  " + message);
    }

    public static void fail(String message) {
        logger.warning("[FAIL]  " + message);
        System.err.println("[FAIL]  " + message);
    }

    public static void warn(String message) {
        logger.warning(message);
        System.out.println("[WARN]  " + message);
    }

    public static void request(String method, String url) {
        String msg = "REQUEST  --> " + method.toUpperCase() + " " + url;
        logger.info(msg);
        System.out.println(msg);
    }

    public static void response(int statusCode, long responseTime) {
        String msg = "RESPONSE <-- Status: " + statusCode + " | Time: " + responseTime + "ms";
        logger.info(msg);
        System.out.println(msg);
    }

    public static void separator() {
        String line = new String(new char[60]).replace("\0", "-");
        logger.info(line);
        System.out.println(line);
    }
}
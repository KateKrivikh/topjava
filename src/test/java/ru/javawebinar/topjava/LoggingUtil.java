package ru.javawebinar.topjava;

import org.slf4j.bridge.SLF4JBridgeHandler;

public class LoggingUtil {
    /**
    * Only for postgres driver logging
    * It uses java.util.logging and logged via jul-to-slf4j bridge
    */
    public static void initBridgeForPostgresDriverLogging() {
        if (!SLF4JBridgeHandler.isInstalled())
            SLF4JBridgeHandler.install();
    }
}

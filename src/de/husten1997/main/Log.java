package de.husten1997.main;

import java.util.Date;
import java.util.logging.*;

public class Log {
    public static Logger setupLogger(String Name) {
        Logger LOGGER = Logger.getLogger( Name );
        Formatter formatter = new SimpleFormatter() {
            private String format = "[%1$tF %1$tT.%1$tL] [%2$-7s] [%3$s] %4$s %n";

            @Override
            public synchronized String format(LogRecord lr) {
                return String.format(format, new Date(lr.getMillis()), lr.getLevel().getLocalizedName(),
                        lr.getLoggerName(),
                        lr.getMessage());
            }
        };
        ConsoleHandler consoleHandler = new ConsoleHandler();
//        consoleHandler.setFormatter(new SimpleFormatter());
        consoleHandler.setFormatter(formatter);
        consoleHandler.setLevel(Level.FINEST);
        LOGGER.addHandler(consoleHandler);
        LOGGER.setLevel(Level.FINEST);

        return LOGGER;
    }
}

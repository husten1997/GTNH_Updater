package de.husten1997.main;

import de.husten1997.gui.Gui;
import java.util.logging.*;

import static de.husten1997.main.Log.setupLogger;


public class Main {

    public static void main(String[] args) {
        final Logger LOGGER = setupLogger( Main.class.getName() );
        LOGGER.log(Level.FINE, "Starting App");

        ApplicationContextHandler configHandler = new ApplicationContextHandler();

        Gui gui = new Gui("GTNH Updater", configHandler.getApplicationConfig(), configHandler::writeConfigFile);
        gui.show();

        LOGGER.log(Level.FINEST, "Closing App");
    }
}

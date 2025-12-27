package de.husten1997.main;

import de.husten1997.gui.Gui;

import java.util.Date;
import java.util.logging.*;

import static de.husten1997.main.Log.setupLogger;


public class Main {
    private static final Logger LOGGER = setupLogger( Main.class.getName() );
    public static ApplicationConfigHandler configHandler;


    public static void main(String[] args) {
        LOGGER.log(Level.FINE, "Starting App");

        configHandler = new ApplicationConfigHandler();

        Gui gui = new Gui(800, 600);
        gui.show();

        LOGGER.log(Level.FINEST, "Closing App");
    }
}

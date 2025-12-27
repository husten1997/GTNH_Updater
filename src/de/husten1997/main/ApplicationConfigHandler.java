package de.husten1997.main;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.husten1997.changesettings.Setting;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApplicationConfigHandler {
    Logger LOGGER = Log.setupLogger( ApplicationConfigHandler.class.getName() );
    ApplicationConfig applicationConfig;
    ObjectMapper mapper;

    public ApplicationConfigHandler() {
        this.mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);

        try {
            this.applicationConfig = readConfigFile("GTNH_Updater.cfg");
        } catch(IOException e) {
            LOGGER.log(Level.FINE, "Error loading config file");
            this.applicationConfig = new ApplicationConfig();
        }

    }

    public ApplicationConfig readConfigFile(String path) throws IOException {
        return mapper.readValue(new File("GTNH_Updater.cfg"), ApplicationConfig.class);
    }

    public void writeConfigFile(String path) {
        try {
            mapper.writeValue(new File("GTNH_Updater.cfg"), this.applicationConfig);
        } catch (IOException e) {
            LOGGER.log(Level.FINE, "Error saving config file");
        }
    }

    public ApplicationConfig getApplicationConfig() {
        return this.applicationConfig;
    }




}

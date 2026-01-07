package de.husten1997.main;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApplicationContextHandler {
    Logger LOGGER = Log.setupLogger( ApplicationContextHandler.class.getName() );
    private final String configFilePath = "GTNH_Updater.cfg";
    ApplicationContext applicationContext;
    ObjectMapper mapper;

    public ApplicationContextHandler() {
        this.mapper = new ObjectMapper();
//        mapper.setDefaultPrettyPrinter(new DefaultPrettyPrinter());
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            this.applicationContext = readConfigFile();
        } catch(IOException e) {
            LOGGER.log(Level.FINE, "Error loading config file");
            e.printStackTrace();
            this.applicationContext = new ApplicationContext();
        }

    }

    public ApplicationContext readConfigFile() throws IOException {
        return mapper.readValue(new File(configFilePath), ApplicationContext.class);
    }

    public void writeConfigFile() {
        try {
            mapper.writeValue(new File(configFilePath), this.applicationContext);
        } catch (IOException e) {
            LOGGER.log(Level.FINE, "Error saving config file");
            e.printStackTrace();
        }
    }

    public ApplicationContext getApplicationConfig() {
        return this.applicationContext;
    }



}

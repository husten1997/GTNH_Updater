package de.husten1997.changesettings;

import de.husten1997.actionpipeline.ActionPipelineStep;

import static de.husten1997.main.Log.setupLogger;
import org.jetbrains.annotations.NotNull;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.*;

public class ChangeSettings implements ActionPipelineStep {
    private ChangeFile[] changeSettingsList;

    private static final Logger LOGGER = setupLogger( ChangeSettings.class.getName() );

    public ChangeSettings(@NotNull Map<String, Map.Entry<String, String>> settingsMap) {
        ArrayList<ChangeFile> tmpChangeFileArray = initChangeFileArray(settingsMap);

        changeSettingsList = new ChangeFile[tmpChangeFileArray.size()];
        changeSettingsList = tmpChangeFileArray.toArray(changeSettingsList);
    }

    private ArrayList<ChangeFile> initChangeFileArray(@NotNull Map<String, Map.Entry<String, String>> settingsMap) {
        int size = settingsMap.size();
        ArrayList<ChangeFile> tmpChangeFileArray = new ArrayList<ChangeFile>();

        for (Map.Entry<String, Map.Entry<String, String>> entry : settingsMap.entrySet()) {
            String file = entry.getKey();
            String settingName = entry.getValue().getKey();
            String settingValue = entry.getValue().getValue();
            try {
                tmpChangeFileArray.add(new ChangeFile(file, settingName, settingValue));

            } catch (Exception e) {
                break;
            }
        }

        return tmpChangeFileArray;
    }

    @Override
    public boolean execute() {
        LOGGER.log(Level.FINE, "Start change of settings");
        for (ActionPipelineStep step: this.changeSettingsList) {
            LOGGER.log(Level.FINEST, String.format("ChangeAction: %s", step.identify()));
            try {
                step.execute();
            } catch (IOException e) {
//                throw new RuntimeException(e);
                LOGGER.log(Level.FINE, String.format("Failed Step: %s", step.identify()));
                LOGGER.log(Level.FINEST, String.format("Error: %s", e.getStackTrace()));
//                LOGGER.log(Level.ALL, "Failed Step: {0} \n {1}", new Object[]{step.identify(), e.getStackTrace()});
            }
        }
        return false;
    }

    @Override
    public String identify() {
        return "Overall change settings action";
    }
}

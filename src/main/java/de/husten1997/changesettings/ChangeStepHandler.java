package de.husten1997.changesettings;

import javax.swing.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import static de.husten1997.main.Log.setupLogger;

public class ChangeStepHandler {
    private static final Logger LOGGER = setupLogger( ChangeStepHandler.class.getName() );
    private ChangeStepFile[] changeSettingsBatch;

    public ChangeStepHandler() {}

    public ChangeStepHandler(ChangeStepFile[] changeSettingsBatch) {
        this.changeSettingsBatch = changeSettingsBatch;
    }

    public ChangeStepHandler(DefaultListModel<SettingFile> fileListDataModel, HashMap<String, DefaultListModel<SettingEntry>> settingsListDataModel) {
        this.changeSettingsBatch = translateToChangeSettingBatch(fileListDataModel, settingsListDataModel);
    }

    public ChangeStepFile[] getChangeSettingsBatch() {
        return changeSettingsBatch;
    }

    public void setChangeSettingsBatch(ChangeStepFile[] changeSettingsBatch) {
        this.changeSettingsBatch = changeSettingsBatch;
    }

    public static ChangeStepFile[] translateToChangeSettingBatch(DefaultListModel<SettingFile> fileListDataModel, HashMap<String, DefaultListModel<SettingEntry>> settingsListDataModel) {
        SettingFile[] tmpFileList = Arrays.stream(fileListDataModel.toArray()).toArray(SettingFile[]::new) ;
        HashMap<String, SettingEntry[]> tmpSettingsList = new HashMap<>();

        for (String file: settingsListDataModel.keySet()) {
            tmpSettingsList.put(file, Arrays.stream(settingsListDataModel.get(file).toArray()).toArray(SettingEntry[]::new));
        }

        ChangeStepFile[] tmpChangeSettingsList = new ChangeStepFile[tmpFileList.length];

        for (int i = 0; i < tmpFileList.length; i++) {
            tmpChangeSettingsList[i] = new ChangeStepFile(tmpFileList[i], tmpSettingsList.get(tmpFileList[i].toString()));
        }
        return tmpChangeSettingsList;
    }

    public static Object[] translateFromChangeSettingsBatch(ChangeStepFile[] changeSettingsBatch) {
        DefaultListModel<SettingFile> fileListDataModel = new DefaultListModel<>();
        HashMap<String, DefaultListModel<SettingEntry>> settingsListDataModel = new HashMap<>();

        for (ChangeStepFile step: changeSettingsBatch) {
            SettingFile tmpFile = step.getSettingFile();
            SettingEntry[] tmpEntry = step.getSettingsEntries();
            DefaultListModel<SettingEntry> tmpEntryListModel = new DefaultListModel<>();

            fileListDataModel.addElement(tmpFile);

            for (SettingEntry entry: tmpEntry) {
                tmpEntryListModel.addElement(entry);
            }
            settingsListDataModel.put(tmpFile.toString(), tmpEntryListModel);
        }

        return new Object[] {fileListDataModel, settingsListDataModel};
    }


//    @Override
    public boolean execute(String parentPath) {
        LOGGER.log(Level.FINE, "Start change of settings");
        for (ChangeStepFile step: this.changeSettingsBatch) {
            LOGGER.log(Level.FINEST, String.format("ChangeAction: %s", step.identify()));
            try {
                step.execute(parentPath);
            } catch (IOException e) {
                LOGGER.log(Level.FINE, String.format("Failed Step: %s", step.identify()));
                LOGGER.log(Level.FINEST, String.format("Error: %s", e.getStackTrace()));
            }
        }
        return false;
    }

//    @Override
    public String identify() {
        return "Change Settings Step Handler";
    }
}

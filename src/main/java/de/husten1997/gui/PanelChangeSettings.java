package de.husten1997.gui;

import de.husten1997.changesettings.ChangeStepHandler;
import de.husten1997.changesettings.SettingEntry;
import de.husten1997.changesettings.SettingFile;
import de.husten1997.main.ApplicationContext;
import de.husten1997.main.Log;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PanelChangeSettings extends GtnhUpdaterGuiComponent {
    private final Logger LOGGER = Log.setupLogger( PanelChangeSettings.class.getName() );

    private final DefaultListModel<SettingFile> fileListDataModel;
    private final HashMap<String, DefaultListModel<SettingEntry>> settingsListDataModel;
    private final JList<SettingFile> fileList;
    private final JList<SettingEntry> settingsList;


    public PanelChangeSettings(String frameName, ApplicationContext applicationContext) {
        super(frameName, applicationContext);

        // Set up the necessary lists and datamodels
        Object[] tmpData = ChangeStepHandler.translateFromChangeSettingsBatch(this.getApplicationConfig().getChangeSettingsBatch());
        this.fileListDataModel = (DefaultListModel<SettingFile>) tmpData[0];
        this.settingsListDataModel = (HashMap<String, DefaultListModel<SettingEntry>>) tmpData[1];
        this.fileList = new JList<>(fileListDataModel);
        this.settingsList = new JList<>();

        // If different file is selected
        ListSelectionModel fileListSelectionModel = this.fileList.getSelectionModel();
        fileListSelectionModel.addListSelectionListener(e -> {
            // Retrieve index of selected entry and break if something is weird
            final int selection = fileList.getSelectedIndex();
            if ( selection == -1 ) {
                settingsList.setModel(new DefaultListModel<>());
                return;
            }
            if ( !fileListDataModel.contains(selection) ) {
                settingsList.setModel(new DefaultListModel<>());
                return;
            }

            // Retrieve SettingsFile at index and check whether there are related SettingsEntry entries
            final String key = fileListDataModel.get(selection).toString();
            if ( !settingsListDataModel.containsKey(key) ) {
                settingsList.setModel(new DefaultListModel<SettingEntry>());
                return;
            }

            // Sett the SettingsList component to show the correct list of settings entries
            settingsList.setModel(settingsListDataModel.get(key));

        });

        // Set boarder & layout of the Panel
        setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(this.getFrameName()),
                        BorderFactory.createEmptyBorder(5,5,5,5)
                )
        );
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Left Buttons
        JPanel leftButtonPanel = new JPanel(new GridLayout(-1, 1));
        JButton addFile = new JButton(IconHandler.fetchIcon(BootstrapIcons.FILE_EARMARK_PLUS));
        addFile.addActionListener(e -> {
            // Check if there is a GTNH Folder picked, break if not
            // TODO: Add isValid function which also checks if the path is valid
            if (applicationContext.getNewGtnhFolderPath().isEmpty()) {
                JOptionPane.showMessageDialog(addFile, "Please select the instance folders first");
                return;
            }

            // Open File Chooser and set it to the GTNH Path
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setAcceptAllFileFilterUsed(true);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setDialogTitle("Chose the settings file");
            Path newGtnhFolderPath = Paths.get(applicationContext.getNewGtnhFolderPath());
            fileChooser.setCurrentDirectory(newGtnhFolderPath.toFile());

            // Fetch Response and break if the user did not approve the action
            final int returnValue = fileChooser.showOpenDialog(leftButtonPanel);
            if (returnValue != JFileChooser.APPROVE_OPTION) { return; }

            // Get selected file, fetch relative path (to GTNH Folder path) and create new SettingsFile Object
            File file = fileChooser.getSelectedFile();
            Path relPath = newGtnhFolderPath.relativize(file.toPath());
            SettingFile newSettingsFileEntry = new SettingFile(relPath.toString());

            // Check whether the file is already tracked, if so, show a dialog and switch the selection of the list accordingly
            if (fileListDataModel.contains(newSettingsFileEntry)) {
                LOGGER.log(Level.FINE, String.format("File %s at %s is already present", file.getName(), file.getPath()));
                JOptionPane.showMessageDialog(addFile, "File is already tracked");
                int index = fileListDataModel.indexOf(newSettingsFileEntry);
                fileList.setSelectedIndex(index);
                return;
            }

            // Add new entry and new list of tracked settings
            fileListDataModel.addElement(newSettingsFileEntry);
            settingsListDataModel.put(relPath.toString(), new DefaultListModel<>());
            fileList.setSelectedIndex(fileListDataModel.size() - 1);
            LOGGER.log(Level.FINE, String.format("Added file %s at %s", file.getName(), file.getPath()));
        });

        // Button Edit
        JButton editFile = new JButton(IconHandler.fetchIcon(BootstrapIcons.PENCIL_SQUARE));
        editFile.addActionListener(e -> {
            // Retrieve index of selected entry and break if something is weird
            final int selection = fileList.getSelectedIndex();
            if ( selection == -1 ) { return; }
            if ( !fileListDataModel.contains(selection) ) { return; }

            // Check if there is a GTNH Folder picked, break if not
            // TODO: Add isValid function which also checks if the path is valid
            if (applicationContext.getNewGtnhFolderPath().isEmpty()) {
                JOptionPane.showMessageDialog(addFile, "Please select the instance folders first");
                return;
            }

            // Retrieve selected SettingsFile from list based on index
            SettingFile selectedFile = fileListDataModel.get(selection);

            // Open file chooser and set path to the GTNH Path
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setAcceptAllFileFilterUsed(true);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setDialogTitle("Chose the settings file");
            Path newGtnhFolderPath = Paths.get(applicationContext.getNewGtnhFolderPath());
            fileChooser.setCurrentDirectory(newGtnhFolderPath.resolve( selectedFile.getFilePath()).toFile());

            // Retrieve response and break if user did not approve
            final int returnValue = fileChooser.showOpenDialog(leftButtonPanel);
            if (returnValue != JFileChooser.APPROVE_OPTION) { return; }

            // Get selected file and extract relative path + create new SettingsFile Object
            File file = fileChooser.getSelectedFile();
            Path relPath = newGtnhFolderPath.relativize(file.toPath());
            SettingFile newSettingsFileEntry = new SettingFile(relPath.toString());

            // If the selected file is already tracked, prompt the user, switch selection and break
            if (fileListDataModel.contains(newSettingsFileEntry)) {
                LOGGER.log(Level.FINE, String.format("File %s at %s is already present", file.getName(), file.getPath()));
                JOptionPane.showMessageDialog(addFile, "File is already tracked");
                int index = fileListDataModel.indexOf(newSettingsFileEntry);
                fileList.setSelectedIndex(index);
                return;
            }

            // create new entry at the index of the old one
            fileListDataModel.setElementAt(newSettingsFileEntry, selection);
            DefaultListModel<SettingEntry> settings = settingsListDataModel.remove(selectedFile.toString());
            settingsListDataModel.put(relPath.toString(), settings);
        });

        // Remove Button
        JButton removeFile = new JButton(IconHandler.fetchIcon(BootstrapIcons.FILE_EARMARK_MINUS));
        removeFile.addActionListener(e -> {
            // Retrieve index of selected entry and break if something is weird
            final int selection = fileList.getSelectedIndex();
            if (selection == -1) { return; }
            if ( !fileListDataModel.contains(selection) ) { return; }

            // Remove file from List of SettingsFiles as well as the tracked settings
            // TODO: Are you sure dialog
            SettingFile selectedFile = fileListDataModel.get(selection);
            fileListDataModel.remove(selection);
            settingsListDataModel.remove(selectedFile);
            LOGGER.log(Level.FINE, String.format("Removed file %s at position %d", selectedFile, selection));
        });

        leftButtonPanel.add(addFile);
        leftButtonPanel.add(editFile);
        leftButtonPanel.add(removeFile);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.BOTH;
        add(leftButtonPanel, gbc);


        // Split Pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, fileList, settingsList);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation( this.getApplicationConfig().getWindowWidth() / 2 );

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.BOTH;
        add(splitPane, gbc);


        // Right Buttons
        JPanel rightButtonPanel = new JPanel(new GridLayout(-1, 1));
        JButton addSetting = new JButton(IconHandler.fetchIcon(BootstrapIcons.PLUS_SQUARE));
        addSetting.addActionListener(e -> {
            // Retrieve index of selected entry and break if something is weird
            int selected = fileList.getSelectedIndex();
            if (selected == -1) { return; }

            // Show SettingsInputDialog & test how and if the user responded correctly
            // TODO: Prompt user or block approve action as log as values are not correct
            SettingsInputDialog inputDialog = new SettingsInputDialog();
            int responseValue = JOptionPane.showConfirmDialog(rightButtonPanel, inputDialog, "Input Setting to Change", JOptionPane.YES_NO_OPTION);

            if ( responseValue != JOptionPane.YES_OPTION ) { return; }
            if ( inputDialog.getSettingsName().isEmpty() ) { return; }
            if ( inputDialog.getSettingsValue().isEmpty() ) { return; }
            if ( !fileListDataModel.contains(selected) ) { return; }

            SettingFile selectedFile = fileListDataModel.get(selected);
            SettingEntry newSettingEntry = new SettingEntry(
                    inputDialog.getSettingsActive(),
                    inputDialog.getSettingsName(),
                    inputDialog.getSettingsValue(),
                    inputDialog.getSettingsLine()
            );

            // If the settingsListDataModel should not contain a List for the currently selected file, we just add it
            if ( !settingsListDataModel.containsKey(selectedFile.toString()) ) {
                settingsListDataModel.put(selectedFile.toString(), new DefaultListModel<SettingEntry>());
                LOGGER.log(Level.FINE, "For some reason the settings list for this file was not tracked");
            }

            // If the settings list for the selected file already contains an item with the same name, we prompt the user and break
            if (settingsListDataModel.get(selectedFile.toString()).contains(newSettingEntry)) {
                LOGGER.log(Level.FINE, String.format("Setting %s is already tracked", inputDialog.getSettingsName()));
                JOptionPane.showMessageDialog(addFile, "Setting is already tracked");
                int index = settingsListDataModel.get(selectedFile.toString()).indexOf(newSettingEntry);
                settingsList.setSelectedIndex(index);
                return;
            }

            // Add
            settingsListDataModel.get(selectedFile.toString()).addElement(newSettingEntry);
            LOGGER.log(Level.FINE, "Added a new settings value");
        });

        JButton editSetting = new JButton(IconHandler.fetchIcon(BootstrapIcons.PENCIL_SQUARE));
        editSetting.addActionListener(e -> {
            // Get selection
            int selectedFileIndex = fileList.getSelectedIndex();
            int selectedSettingIndex = settingsList.getSelectedIndex();

            // Break if nothing is selected
            if (selectedFileIndex == -1 || selectedSettingIndex == -1) { return; }
            if ( !fileListDataModel.contains(selectedFileIndex) ) { return; }

            // Identify entry which should be changed
            SettingFile selectedFile = fileListDataModel.get(selectedFileIndex);
            if ( !settingsListDataModel.containsKey(selectedFile) ) { return; }

            // Extract the necessary data
            DefaultListModel<SettingEntry> settingsData = settingsListDataModel.get(selectedFile);
            SettingEntry selectedSettings = settingsData.get(selectedSettingIndex);

            // Start Input Dialog
            SettingsInputDialog inputDialog = new SettingsInputDialog();
            inputDialog.setValues(
                    selectedSettings.isSettingActive(),
                    selectedSettings.getSettingName(),
                    selectedSettings.getSettingValue(),
                    selectedSettings.getSettingLine()
            );
            final int responseValue = JOptionPane.showConfirmDialog(rightButtonPanel, inputDialog, "Input Setting to Change", JOptionPane.YES_NO_OPTION);

            // Break conditions after dialog is closed
            if (responseValue != JOptionPane.YES_OPTION) { return; }
            if (inputDialog.getSettingsName().isEmpty()) { return; }
            if (inputDialog.getSettingsValue().isEmpty()) { return; }

            // Create new setting entry
            SettingEntry newSettingEntry = new SettingEntry(
                    inputDialog.getSettingsActive(),
                    inputDialog.getSettingsName(),
                    inputDialog.getSettingsValue(),
                    inputDialog.getSettingsLine()
            );

            // Check whether an entry should be overwritten
            final boolean overwrite = isOverwrite(selectedSettings, newSettingEntry);

            if ((settingsListDataModel.get(selectedFile.toString()).contains(newSettingEntry)) ^ (overwrite)) {
                LOGGER.log(Level.FINE, String.format("Setting %s is already tracked", inputDialog.getSettingsName()));
                JOptionPane.showMessageDialog(addFile, "Setting is already tracked");
                int index = settingsListDataModel.get(selectedFile.toString()).indexOf(newSettingEntry);
                settingsList.setSelectedIndex(index);
                return;
            }

            settingsListDataModel.get(selectedFile.toString()).setElementAt(newSettingEntry, selectedSettingIndex);
            LOGGER.log(Level.FINE, "Updated Settings");
        });
        JButton removeSetting = new JButton(IconHandler.fetchIcon(BootstrapIcons.DASH_SQUARE));
        removeSetting.addActionListener(e -> {
            int selectedFileIndex = fileList.getSelectedIndex();
            int selectedSettingIndex = settingsList.getSelectedIndex();

            if (selectedFileIndex == -1 || selectedSettingIndex == -1) { return; }

            SettingFile selectedFile = fileListDataModel.get(selectedFileIndex);
            settingsListDataModel.get(selectedFile.toString()).remove(selectedSettingIndex);
            LOGGER.log(Level.FINE, String.format("Removed Settings at %d", selectedSettingIndex));
        });

        rightButtonPanel.add(addSetting);
        rightButtonPanel.add(editSetting);
        rightButtonPanel.add(removeSetting);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.BOTH;
        add(rightButtonPanel, gbc);

    }

    private static boolean isOverwrite(SettingEntry selectedSettings, SettingEntry newSettingEntry) {
        final boolean isNameEqual = selectedSettings.getSettingName().equals(newSettingEntry.getSettingName());
        final boolean isValueDifferent = !selectedSettings.getSettingValue().equals(newSettingEntry.getSettingValue());
        final boolean isActiveDifferent = (selectedSettings.isSettingActive() != newSettingEntry.isSettingActive());
        final boolean isLineDifferent = (selectedSettings.getSettingLine() != newSettingEntry.getSettingLine());
        return isNameEqual && (isValueDifferent || isActiveDifferent || isLineDifferent);
    }

    public DefaultListModel<SettingFile> getFileListDataModel() {
        return fileListDataModel;
    }

    public HashMap<String, DefaultListModel<SettingEntry>> getSettingsListDataModel() {
        return settingsListDataModel;
    }

}

package de.husten1997.gui;

import de.husten1997.changesettings.ChangeSettingsExecuter;
import de.husten1997.changesettings.ChangeStepHandler;
import de.husten1997.copyinstance.CopyInstance;
import de.husten1997.copyinstance.CopyPlan;
import de.husten1997.main.ApplicationContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import static de.husten1997.main.Log.setupLogger;

public class Gui extends GtnhUpdaterGuiComponent {
    private final Runnable callbackWriteConfig;

    private PanelGtnhFolder panelFolderSelect;
    private PanelCopySelect panelCopySelect;
    private PanelChangeSettings panelChangeSettings;
    private PanelFinalDialog panelFinalDialog;

    private static final Logger LOGGER = setupLogger( Gui.class.getName() );

    public Gui(String frameName, ApplicationContext applicationContext, Runnable callbackWriteConfig) {
        super(frameName, applicationContext);

        this.callbackWriteConfig = callbackWriteConfig;

        LOGGER.log(Level.FINE, String.format("Setup window with dimensions W:%d, H:%d", this.getApplicationConfig().getWindowWidth(), this.getApplicationConfig().getWindowHeight()));

        this.prepare_gui();
    }

    private void prepare_gui(){
        LOGGER.log(Level.FINEST, "Preparing GUI");

        // Setup MainWindow
        JFrame mainWindow = new JFrame();
        mainWindow.setTitle(this.getFrameName());
        mainWindow.setSize(this.getApplicationConfig().getWindowWidth(), this.getApplicationConfig().getWindowHeight());
        mainWindow.setLayout(new GridLayout(4, 1));

        this.panelFolderSelect = new PanelGtnhFolder("Select Instance Folder", this.getApplicationConfig());
        this.panelCopySelect = new PanelCopySelect("Select the copy steps", this.getApplicationConfig());
        this.panelChangeSettings = new PanelChangeSettings("Settings and Setting Files to change", this.getApplicationConfig());
        this.panelFinalDialog = new PanelFinalDialog("", this.getApplicationConfig(), this::migrateInstance, this::updateSettings);

        // Main Window Event Listener
        mainWindow.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
            updateConfigHandler();
            callbackWriteConfig.run();
            System.exit(0);
            }
        });

        // Add components to Main Window
        mainWindow.add(this.panelFolderSelect);
        mainWindow.add(this.panelCopySelect);
        mainWindow.add(this.panelChangeSettings);
        mainWindow.add(this.panelFinalDialog);
        mainWindow.setVisible(true);
    }

    public void show(){
        LOGGER.log(Level.FINEST, "Show GUI");
    }

    private void updateConfigHandler() {
        String source = panelFolderSelect.getOldGtnhFolderPath();
        String target = panelFolderSelect.getNewGtnhFolderPath();
        CopyPlan[] copyPlans = panelCopySelect.getCopyPlans();
        ChangeStepHandler settingsToChange = new ChangeStepHandler(panelChangeSettings.getFileListDataModel(), panelChangeSettings.getSettingsListDataModel());

        this.getApplicationConfig().setOldGtnhFolderPath(source);
        this.getApplicationConfig().setNewGtnhFolderPath(target);
        this.getApplicationConfig().setCopyPlanBatch(copyPlans);
        this.getApplicationConfig().setChangeSettingsBatch(settingsToChange.getChangeSettingsBatch());
    }

    private void migrateInstance() {
        LOGGER.log(Level.FINE, "Start Copying");
        updateConfigHandler();

        if (this.getApplicationConfig().canCopy()) {
            CopyInstance copyInstanceHandler = new CopyInstance(this.getApplicationConfig());
            copyInstanceHandler.execute();
            JOptionPane.showMessageDialog(null, "Finished migration of instance", "Finished", JOptionPane.INFORMATION_MESSAGE);
        } else {
            LOGGER.log(Level.FINE, "Did nothing");
        }
    }

    private void updateSettings() {
        LOGGER.log(Level.FINE, "Start updating settings");
        updateConfigHandler();

        if (this.getApplicationConfig().canCopy()) {
            ChangeSettingsExecuter changeSettingsHandler = new ChangeSettingsExecuter(this.getApplicationConfig());
            changeSettingsHandler.execute();
            JOptionPane.showMessageDialog(null, "Finished updating settings", "Finished", JOptionPane.INFORMATION_MESSAGE);
        } else {
            LOGGER.log(Level.FINE, "Did nothing");
        }
    }

}

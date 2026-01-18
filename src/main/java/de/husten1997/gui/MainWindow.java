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

public class MainWindow extends GtnhUpdaterPanelComponent {
    private final Runnable callbackWriteConfig;

    private final PanelHeader panelHeader;
    private final PanelGtnhFolder panelFolderSelect;
    private final PanelCopySelect panelCopySelect;
    private final PanelChangeSettings panelChangeSettings;
    private final PanelFinalDialog panelFinalDialog;

    private static final Logger LOGGER = setupLogger( MainWindow.class.getName() );

    public MainWindow(ApplicationContext applicationContext, Runnable callbackWriteConfig) {
        super(applicationContext);

        // Listen for language changes
        i18nManager.addLocaleChangeListener(locale -> this.updateLabels());

        this.callbackWriteConfig = callbackWriteConfig;

        LOGGER.log(Level.FINE, String.format("Setup window with dimensions W:%d, H:%d", this.getApplicationConfig().getWindowWidth(), this.getApplicationConfig().getWindowHeight()));

        this.panelHeader = new PanelHeader(this.getApplicationConfig());
        this.panelFolderSelect = new PanelGtnhFolder(this.getApplicationConfig());
        this.panelCopySelect = new PanelCopySelect(this.getApplicationConfig());
        this.panelChangeSettings = new PanelChangeSettings(this.getApplicationConfig());
        this.panelFinalDialog = new PanelFinalDialog(this.getApplicationConfig(), this::migrateInstance, this::updateSettings);

        this.prepare_gui();
    }

    private void prepare_gui(){
        LOGGER.log(Level.FINEST, "Preparing GUI");

        // Setup MainWindow
        JFrame mainWindow = new JFrame();
        mainWindow.setTitle(i18nManager.get("app.mainWindow.title", false));
        mainWindow.setSize(this.getApplicationConfig().getWindowWidth(), this.getApplicationConfig().getWindowHeight());
        mainWindow.setLayout(new GridBagLayout());

        // Main Window Event Listener
        mainWindow.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
            updateConfigHandler();
            callbackWriteConfig.run();
            System.exit(0);
            }
        });

        // Add components to Main Window
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1;

        gbc.gridy = 0; gbc.weighty = 0.1;
        mainWindow.add(this.panelHeader, gbc);
        gbc.gridy = 1; gbc.weighty = 0.1;
        mainWindow.add(this.panelFolderSelect, gbc);
        gbc.gridy = 2; gbc.weighty = 0.1;
        mainWindow.add(this.panelCopySelect, gbc);
        gbc.gridy = 3; gbc.weighty = 1;
        mainWindow.add(this.panelChangeSettings, gbc);
        gbc.gridy = 4; gbc.weighty = 0.1;
        mainWindow.add(this.panelFinalDialog, gbc);

        this.updateLabels();

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

    @Override
    public void updateLabels() {
        this.panelHeader.updateLabels();
        this.panelFolderSelect.updateLabels();
        this.panelCopySelect.updateLabels();
        this.panelChangeSettings.updateLabels();
        this.panelFinalDialog.updateLabels();
    }
}

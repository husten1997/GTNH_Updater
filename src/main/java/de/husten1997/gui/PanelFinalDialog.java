package de.husten1997.gui;

import de.husten1997.main.ApplicationContext;
import de.husten1997.main.Log;

import javax.swing.*;
import java.util.logging.Logger;

public class PanelFinalDialog extends GtnhUpdaterPanelComponent {
    // Logger
    private final Logger LOGGER = Log.setupLogger( PanelFinalDialog.class.getName() );

    // UI Components
    private final JButton buttonMigrate;
    private final JButton buttonUpdateSettings;
    private final JButton buttonApprove;
    private final JButton buttonCancel;

    public PanelFinalDialog(ApplicationContext applicationContext, Runnable copyCallback, Runnable settingsCallback) {
        super(applicationContext);

        buttonMigrate = new JButton();
        buttonUpdateSettings = new JButton();
        buttonApprove = new JButton();
        buttonCancel = new JButton();

        buttonMigrate.addActionListener(e -> copyCallback.run());

        buttonUpdateSettings.addActionListener(e -> settingsCallback.run());

        buttonApprove.addActionListener(e -> {
            copyCallback.run();
            settingsCallback.run();
        });

        buttonCancel.addActionListener(e -> System.exit(0));

        add(buttonMigrate);
        add(buttonUpdateSettings);
        add(buttonApprove);
        add(buttonCancel);
    }

    @Override
    public void updateLabels() {
        this.buttonMigrate.setText(i18nManager.get("app.finalDialog.button.migrate"));
        this.buttonMigrate.setToolTipText(i18nManager.get("app.finalDialog.tooltip.migrate"));
        this.buttonUpdateSettings.setText(i18nManager.get("app.finalDialog.button.updateSettings"));
        this.buttonUpdateSettings.setToolTipText(i18nManager.get("app.finalDialog.tooltip.updateSettings"));
        this.buttonApprove.setText(i18nManager.get("app.finalDialog.button.approve"));
        this.buttonApprove.setToolTipText(i18nManager.get("app.finalDialog.tooltip.approve"));
        this.buttonCancel.setText(i18nManager.get("app.finalDialog.button.cancel"));
        this.buttonCancel.setToolTipText(i18nManager.get("app.finalDialog.tooltip.cancel"));

    }
}

package de.husten1997.gui;

import de.husten1997.main.ApplicationContext;
import de.husten1997.main.Log;

import javax.swing.*;
import java.util.logging.Logger;

public class PanelFinalDialog extends GtnhUpdaterGuiComponent {
    private final Logger LOGGER = Log.setupLogger( PanelFinalDialog.class.getName() );

    public PanelFinalDialog(String frameName, ApplicationContext applicationContext, Runnable copyCallback, Runnable settingsCallback) {
        super( frameName, applicationContext);

        JButton ButtonMigrate = new JButton("Migrate Instance");
        JButton ButtonUpdateSettings = new JButton("UpdateSettings");
        JButton ButtonApprove = new JButton("Full Run");
        JButton ButtonCancel = new JButton("Cancel");

        ButtonMigrate.addActionListener(e -> copyCallback.run());

        ButtonUpdateSettings.addActionListener(e -> settingsCallback.run());

        ButtonApprove.addActionListener(e -> {
            copyCallback.run();
            settingsCallback.run();
        });

        ButtonCancel.addActionListener(e -> {
            System.out.println("Cancel");
            System.exit(0);
        });

        add(ButtonMigrate);
        add(ButtonUpdateSettings);
        add(ButtonApprove);
        add(ButtonCancel);
    }
}

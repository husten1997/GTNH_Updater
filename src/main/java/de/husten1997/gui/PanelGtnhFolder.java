package de.husten1997.gui;

import de.husten1997.main.ApplicationContext;
import de.husten1997.main.Log;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class PanelGtnhFolder extends GtnhUpdaterPanelComponent {
    private final Logger LOGGER = Log.setupLogger( PanelGtnhFolder.class.getName() );

    //UI Elements
    private final GtnhUpdaterComponentBorder titledBorder;
    private final JLabel oldGtnhFolderInputLabel;
    private final JTextField oldGtnhFolderInput;
    private final JButton oldGtnhFolderBrowse;

    private final JLabel newGtnhFolderInputLabel;
    private final JTextField newGtnhFolderInput;
    private final JButton newGtnhFolderBrowse;

    public PanelGtnhFolder(ApplicationContext applicationContext) {
        super(applicationContext);

        String oldGtnhFolderPath = this.getApplicationConfig().getOldGtnhFolderPath();
        String newGtnhFolderPath = this.getApplicationConfig().getNewGtnhFolderPath();

        // Old GTNH Folder Part
        this.oldGtnhFolderInputLabel = new JLabel();
        this.oldGtnhFolderInput = new JTextField(oldGtnhFolderPath);
        this.oldGtnhFolderBrowse = new JButton();

        // Old GTNH Folder Part
        this.newGtnhFolderInputLabel = new JLabel();
        this.newGtnhFolderInput = new JTextField(newGtnhFolderPath);
        this.newGtnhFolderBrowse = new JButton();

        setLayout(new GridBagLayout());
        this.titledBorder = new GtnhUpdaterComponentBorder(i18nManager.get("app.gtnhInstances.border.title"));
        setBorder(titledBorder);

        GridBagConstraints gbc = new GridBagConstraints();

        // Old GTNH Input Label
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(this.oldGtnhFolderInputLabel, gbc);

        // Old GTNH Folder Input Textfield
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.EAST; gbc.fill = GridBagConstraints.HORIZONTAL;
        add(this.oldGtnhFolderInput, gbc);

        // Old GTNH Select Button
        gbc.gridx = 4; gbc.gridy = 0; gbc.gridwidth = 0; gbc.weightx = 0.1;
        gbc.anchor = GridBagConstraints.EAST; gbc.fill = GridBagConstraints.HORIZONTAL;

        this.oldGtnhFolderBrowse.setIcon(IconHandler.fetchIcon(BootstrapIcons.FOLDER2_OPEN));
        this.oldGtnhFolderBrowse.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setDialogTitle("app.choseDirectoryDialog.title.oldGtnhFolder");
            int returnValue = fileChooser.showOpenDialog(null);

            if (returnValue != JFileChooser.APPROVE_OPTION) { return; }
            this.oldGtnhFolderInput.setText(fileChooser.getSelectedFile().toString());
        });
        add(this.oldGtnhFolderBrowse, gbc);

        // New GTNH Folder
        gbc = new GridBagConstraints();

        // Old GTNH Input Label
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(this.newGtnhFolderInputLabel, gbc);

        // Old GTNH Folder Input Textfield
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.EAST; gbc.fill = GridBagConstraints.HORIZONTAL;
        add(this.newGtnhFolderInput, gbc);

        // Old GTNH Select Button
        gbc.gridx = 4; gbc.gridy = 1; gbc.gridwidth = 0; gbc.weightx = 0.1;
        gbc.anchor = GridBagConstraints.EAST; gbc.fill = GridBagConstraints.HORIZONTAL;

        this.newGtnhFolderBrowse.setIcon(IconHandler.fetchIcon(BootstrapIcons.FOLDER2_OPEN));
        this.newGtnhFolderBrowse.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setDialogTitle(i18nManager.get("app.choseDirectoryDialog.title.newGtnhFolder"));
            int returnValue = fileChooser.showOpenDialog(null);

            if (returnValue != JFileChooser.APPROVE_OPTION) { return; }
            this.newGtnhFolderInput.setText(fileChooser.getSelectedFile().toString());
        });
        add(this.newGtnhFolderBrowse, gbc);
    }

    @Override
    public void updateLabels() {
        // Border Title
        this.titledBorder.setTitle(i18nManager.get("app.gtnhInstances.border.title"));
        this.setToolTipText(i18nManager.get("app.gtnhInstances.tooltip.title"));

        // Old GTNH
        this.oldGtnhFolderInputLabel.setText(i18nManager.get("app.gtnhInstances.label.oldGtnhFolder"));
        this.oldGtnhFolderInputLabel.setToolTipText(i18nManager.get("app.gtnhInstances.tooltip.oldGtnhFolder"));
        this.oldGtnhFolderInput.setToolTipText(i18nManager.get("app.gtnhInstances.tooltip.oldGtnhFolder"));
        this.oldGtnhFolderBrowse.setToolTipText(i18nManager.get("app.gtnhInstances.tooltip.oldGtnhFolderBrowse"));

        // New GTNH
        this.newGtnhFolderInputLabel.setText(i18nManager.get("app.gtnhInstances.label.newGtnhFolder"));
        this.newGtnhFolderInputLabel.setToolTipText(i18nManager.get("app.gtnhInstances.tooltip.newGtnhFolder"));
        this.newGtnhFolderInput.setToolTipText(i18nManager.get("app.gtnhInstances.tooltip.newGtnhFolder"));
        this.newGtnhFolderBrowse.setToolTipText(i18nManager.get("app.gtnhInstances.tooltip.newGtnhFolderBrowse"));
    }


    public String getOldGtnhFolderPath() {
        return this.oldGtnhFolderInput.getText();
    }

    public String getNewGtnhFolderPath() {
        return this.newGtnhFolderInput.getText();
    }

    public void setOldGtnhFolderPath(String text) {
        this.oldGtnhFolderInput.setText(text);
    }

    public void setNewGtnhFolderPath(String text) {
        this.newGtnhFolderInput.setText(text);
    }
}

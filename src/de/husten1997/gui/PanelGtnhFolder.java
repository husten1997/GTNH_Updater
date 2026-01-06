package de.husten1997.gui;

import de.husten1997.main.ApplicationContext;
import de.husten1997.main.Log;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class PanelGtnhFolder extends GtnhUpdaterGuiComponent {
    private final Logger LOGGER = Log.setupLogger( PanelGtnhFolder.class.getName() );

    //UI Elements
    private final JTextField oldGtnhFolderInput;
    private final JTextField newGtnhFolderInput;

    public PanelGtnhFolder(String frameName, ApplicationContext applicationContext) {
        super(frameName, applicationContext);

        String oldGtnhFolderPath = this.getApplicationConfig().getOldGtnhFolderPath();
        String newGtnhFolderPath = this.getApplicationConfig().getNewGtnhFolderPath();

        this.oldGtnhFolderInput = new JTextField(oldGtnhFolderPath);
        this.newGtnhFolderInput = new JTextField(newGtnhFolderPath);

        setLayout(new GridLayout(2, 1));
        setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(this.getFrameName()),
                        BorderFactory.createEmptyBorder(5,5,5,5)
                )
        );

        add(build_input_panel("Old GTNH Instance", this.oldGtnhFolderInput));
        add(build_input_panel("New GTNH Instance", this.newGtnhFolderInput));
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

    private JPanel build_input_panel(String labelName, JTextField folderInput) {
        JPanel parentPanel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        parentPanel.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();

        // Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        parentPanel.add(new JLabel(labelName), gbc);

        // Folder Input Textfield
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        parentPanel.add(folderInput, gbc);

        // Select Button
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.gridwidth = 0;
        gbc.weightx = 0.1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton browse_button = new JButton(IconHandler.fetchIcon(BootstrapIcons.FOLDER2_OPEN));
        browse_button.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setDialogTitle("Chose the directory");
            int returnValue = fileChooser.showOpenDialog(null);

            if (returnValue != JFileChooser.APPROVE_OPTION) { return; }
            folderInput.setText(fileChooser.getSelectedFile().toString());
        });
        parentPanel.add(browse_button, gbc);

        return parentPanel;
    }
}

package de.husten1997.gui;

import de.husten1997.main.Log;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class SettingsInputDialog extends JPanel {
    private final Logger LOGGER = Log.setupLogger( SettingsInputDialog.class.getName() );

    JTextField settingsNameInput;
    JTextField settingsValueInput;
    JCheckBox settingActive;

    JSpinner settingLine;
    SpinnerNumberModel settingLineData;

    public SettingsInputDialog() {
        super();

        setLayout(new GridLayout(-1, 1));

        add(new JLabel("Setting Active"));
        this.settingActive = new JCheckBox("Setting should be un-commented");
        this.settingActive.setSelected(true);
        add(settingActive);

        add(new JLabel("Setting Name"));
        this.settingsNameInput = new JTextField();
        add(settingsNameInput);

        add(new JLabel("Settings Value"));
        this.settingsValueInput = new JTextField();
        add(settingsValueInput);

        add(new JLabel("(Optional) Line of Setting"));
        this.settingLineData = new SpinnerNumberModel(0, 0, 1_000, 1);
        this.settingLine = new JSpinner(this.settingLineData);
        add(this.settingLine);
    }

    public void setValues(boolean settingActive, String settingsName, String settingsValue, int settingLine) {
        this.settingActive.setSelected(settingActive);
        this.settingsNameInput.setText(settingsName);
        this.settingsValueInput.setText(settingsValue);
        this.settingLine.setValue(settingLine);
    }

    public String getSettingsName() {
        return this.settingsNameInput.getText();
    }

    public String getSettingsValue() {
        return this.settingsValueInput.getText();
    }

    public int getSettingsLine() { return this.settingLineData.getNumber().intValue(); }

    public boolean getSettingsActive() { return this.settingActive.isSelected(); }
}

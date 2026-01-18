package de.husten1997.gui;

import de.husten1997.main.I18nManager;
import de.husten1997.main.Log;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class SettingsInputDialog extends JPanel implements GtnhUpdaterLocalizedComponent {
    private final I18nManager i18nManager = I18nManager.getInstance();

    // Logger
    private final Logger LOGGER = Log.setupLogger( SettingsInputDialog.class.getName() );

    // UI Components
    private final JLabel settingsActiveInputLabel;
    private final JCheckBox settingActive;
    private final JLabel settingsNameInputLabel;
    private final JTextField settingsNameInput;
    private final JLabel settingsValueInputLabel;
    private final JTextField settingsValueInput;
    private final JLabel settingsLineInputLabel;
    private final JSpinner settingLine;

    // Data Components
    private final SpinnerNumberModel settingLineData;

    public SettingsInputDialog() {
        super();

        setLayout(new GridLayout(-1, 1));

        this.settingsActiveInputLabel = new JLabel(i18nManager.get("app.choseSettingDialog.label.settingActive"));
        this.settingActive = new JCheckBox(i18nManager.get("app.choseSettingDialog.checkbox.settingActive"));
        this.settingsActiveInputLabel.setToolTipText(i18nManager.get("app.choseSettingDialog.tooltip.settingActive"));
        this.settingActive.setToolTipText(i18nManager.get("app.choseSettingDialog.tooltip.settingActive"));
        this.settingActive.setSelected(true);
        add(this.settingsActiveInputLabel);
        add(this.settingActive);

        this.settingsNameInputLabel = new JLabel(i18nManager.get("app.choseSettingDialog.label.settingName"));
        this.settingsNameInput = new JTextField();
        this.settingsNameInputLabel.setToolTipText(i18nManager.get("app.choseSettingDialog.tooltip.settingName"));
        this.settingsNameInput.setToolTipText(i18nManager.get("app.choseSettingDialog.tooltip.settingName"));
        add(this.settingsNameInputLabel);
        add(this.settingsNameInput);

        this.settingsValueInputLabel = new JLabel(this.i18nManager.get("app.choseSettingDialog.label.settingValue"));
        this.settingsValueInput = new JTextField();
        this.settingsValueInputLabel.setToolTipText(i18nManager.get("app.choseSettingDialog.tooltip.settingValue"));
        this.settingsValueInput.setToolTipText(i18nManager.get("app.choseSettingDialog.tooltip.settingValue"));
        add(this.settingsValueInputLabel);
        add(this.settingsValueInput);

        this.settingsLineInputLabel = new JLabel(this.i18nManager.get("app.choseSettingDialog.label.settingLine"));
        this.settingLineData = new SpinnerNumberModel(0, 0, 1_000, 1);
        this.settingLine = new JSpinner(this.settingLineData);
        this.settingsLineInputLabel.setToolTipText(i18nManager.get("app.choseSettingDialog.tooltip.settingLine"));
        this.settingLine.setToolTipText(i18nManager.get("app.choseSettingDialog.tooltip.settingLine"));
        add(this.settingsLineInputLabel);
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

    @Override
    public void updateLabels() {

    }
}

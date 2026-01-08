package de.husten1997.changesettings;

public class SettingEntry {
    private boolean settingActive;
    private String settingName;
    private String settingValue;
    private int settingLine;

    public SettingEntry(boolean settingActive, String settingName, String settingValue, int settingLine) {
        this.settingActive = settingActive;
        this.settingName = settingName;
        this.settingValue = settingValue;
        this.settingLine = settingLine;
    }

    public SettingEntry() {
        this.settingActive = true;
        this.settingName = null;
        this.settingValue = null;
        this.settingLine = 0;
    }

    public String toString() {
        String template = this.settingActive ? "%s: %s": "# %s: %s";
        String line_template = this.settingLine != 0 ? String.format("(Line %d)", this.settingLine): "";
        return String.format(template, this.settingName, this.settingValue) + line_template;
    }

    public String getSettingName() {
        return settingName;
    }

    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }

    public String getSettingValue() {
        return settingValue;
    }

    public void setSettingValue(String settingValue) {
        this.settingValue = settingValue;
    }

    public boolean isSettingActive() {
        return settingActive;
    }

    public void setSettingActive(boolean settingActive) {
        this.settingActive = settingActive;
    }

    public int getSettingLine() {
        return settingLine;
    }

    public void setSettingLine(int settingLine) {
        this.settingLine = settingLine;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final SettingEntry other = (SettingEntry) obj;
        if (this.settingName == null) { return false; }
        if (other.settingName == null) { return false; }

        if (!this.settingName.equals(other.settingName)) {
            return false;
        }

        return true;
    }
}
package de.husten1997.changesettings;

public class Setting {
    String file;
    String settingName;
    String settingValue;

    public Setting(String file, String settingName, String settingValue) {
        this.file = file;
        this.settingName = settingName;
        this.settingValue = settingValue;
    }

    public Setting() {
        this.file = null;
        this.settingName = null;
        this.settingValue = null;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
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
}

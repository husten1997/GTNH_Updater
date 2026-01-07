package de.husten1997.changesettings;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static de.husten1997.utils.FsIo.changeFile;

public class ChangeStepFile implements ChangeStep {
    private SettingFile settingFile;
    private SettingEntry[] settingsEntries;

    public ChangeStepFile(@NotNull SettingFile file, @NotNull SettingEntry[] settings) {
        this.settingFile = file;
        this.settingsEntries = settings;
    }

    public ChangeStepFile() {
        this.settingFile = new SettingFile();
        this.settingsEntries = new SettingEntry[0];
    }

    public boolean execute(String parentPath) throws IOException {
        changeFile(parentPath + "/" + this.settingFile, this.settingsEntries);
        return false;
    }

    @Override
    public String identify() {
        return String.format("Change file %s", this.settingFile.toString());
    }

    public SettingFile getSettingFile() {
        return settingFile;
    }

    public void setSettingFile(SettingFile settingFile) {
        this.settingFile = settingFile;
    }

    public SettingEntry[] getSettingsEntries() {
        return settingsEntries;
    }

    public void setSettingsEntries(SettingEntry[] settingsEntries) {
        this.settingsEntries = settingsEntries;
    }
}

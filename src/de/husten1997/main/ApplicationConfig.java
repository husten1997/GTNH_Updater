package de.husten1997.main;

import de.husten1997.changesettings.Setting;

public class ApplicationConfig {
    String oldGtnhPath;
    String newGtnhPath;

    Setting[] trackedSettings;

    public ApplicationConfig(String oldGtnhPath, String newGtnhPath, Setting[] trackedSettings) {
        this.oldGtnhPath = oldGtnhPath;
        this.newGtnhPath = newGtnhPath;
        this.trackedSettings = trackedSettings;
    }

    public ApplicationConfig() {
        this.oldGtnhPath = "";
        this.newGtnhPath = "";
        this.trackedSettings = new Setting[0];
    }

    public String getOldGtnhPath() {
        return oldGtnhPath;
    }

    public void setOldGtnhPath(String oldGtnhPath) {
        this.oldGtnhPath = oldGtnhPath;
    }

    public String getNewGtnhPath() {
        return newGtnhPath;
    }

    public void setNewGtnhPath(String newGtnhPath) {
        this.newGtnhPath = newGtnhPath;
    }

    public Setting[] getTrackedSettings() {
        return trackedSettings;
    }

    public void setTrackedSettings(Setting[] trackedSettings) {
        this.trackedSettings = trackedSettings;
    }
}

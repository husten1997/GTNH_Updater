package de.husten1997.changesettings;

public class SettingFile {
    private String filePath;

    public SettingFile(String filePath) {
        this.filePath = filePath;
    }

    public SettingFile() {
        this.filePath = null;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String toString() {
        return this.filePath;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final SettingFile other = (SettingFile) obj;
        if (!this.filePath.equals(other.filePath)) {
            return false;
        }

        return true;
    }
}

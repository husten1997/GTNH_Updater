package de.husten1997.changesettings;

import javax.swing.tree.DefaultMutableTreeNode;

public class SettingTree {
    private class SettingEntry {
        private String settingName;
        private String settingValue;

        public SettingEntry(String settingName, String settingValue) {
            this.settingName = settingName;
            this.settingValue = settingValue;
        }

        public String toString() {
            return this.settingName + ": " + this.settingValue;
        }
    }
    DefaultMutableTreeNode settingFiles;

    public SettingTree(DefaultMutableTreeNode settingFiles) {
        this.settingFiles = settingFiles;
    }

    public SettingTree() {
        DefaultMutableTreeNode file1 = new DefaultMutableTreeNode("File1.cfg");
        file1.add(new DefaultMutableTreeNode(new SettingEntry("Test1", "true")));
        file1.add(new DefaultMutableTreeNode(new SettingEntry("Test2", "false")));
        file1.add(new DefaultMutableTreeNode(new SettingEntry("Test3", "1")));

        DefaultMutableTreeNode file2 = new DefaultMutableTreeNode("File2.cfg");
        file2.add(new DefaultMutableTreeNode(new SettingEntry("Test1", "true")));
        file2.add(new DefaultMutableTreeNode(new SettingEntry("Test2", "false")));
        file2.add(new DefaultMutableTreeNode(new SettingEntry("Test3", "1")));

        DefaultMutableTreeNode file3 = new DefaultMutableTreeNode("File3.cfg");
        file3.add(new DefaultMutableTreeNode(new SettingEntry("Test1", "true")));
        file3.add(new DefaultMutableTreeNode(new SettingEntry("Test2", "false")));
        file3.add(new DefaultMutableTreeNode(new SettingEntry("Test3", "1")));

        DefaultMutableTreeNode tmpSettingFiles = new DefaultMutableTreeNode("SettingFiles");
        tmpSettingFiles.add(file1);
        tmpSettingFiles.add(file2);
        tmpSettingFiles.add(file3);
        this.settingFiles = tmpSettingFiles;
    }

    public DefaultMutableTreeNode getSettingFiles() {
        return settingFiles;
    }

    public void setSettingFiles(DefaultMutableTreeNode settingFiles) {
        this.settingFiles = settingFiles;
    }
}

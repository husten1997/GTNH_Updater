package de.husten1997.changesettings;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SettingFileTest {

    @Test
    void testConstructorWithParameter() {
        SettingFile file = new SettingFile("config/settings.cfg");

        assertThat(file.getFilePath()).isEqualTo("config/settings.cfg");
    }

    @Test
    void testDefaultConstructor() {
        SettingFile file = new SettingFile();

        assertThat(file.getFilePath()).isNull();
    }

    @Test
    void testSetFilePath() {
        SettingFile file = new SettingFile();

        file.setFilePath("config/custom.cfg");

        assertThat(file.getFilePath()).isEqualTo("config/custom.cfg");
    }

    @Test
    void testToString() {
        SettingFile file = new SettingFile("config/settings.cfg");

        assertThat(file.toString()).isEqualTo("config/settings.cfg");
    }

    @Test
    void testEquals_sameObject() {
        SettingFile file = new SettingFile("config/settings.cfg");

        assertThat(file.equals(file)).isTrue();
    }

    @Test
    void testEquals_sameFilePath() {
        SettingFile file1 = new SettingFile("config/settings.cfg");
        SettingFile file2 = new SettingFile("config/settings.cfg");

        assertThat(file1.equals(file2)).isTrue();
    }

    @Test
    void testEquals_differentFilePath() {
        SettingFile file1 = new SettingFile("config/settings.cfg");
        SettingFile file2 = new SettingFile("config/other.cfg");

        assertThat(file1.equals(file2)).isFalse();
    }

    @Test
    void testEquals_null() {
        SettingFile file = new SettingFile("config/settings.cfg");

        assertThat(file.equals(null)).isFalse();
    }

    @Test
    void testEquals_differentClass() {
        SettingFile file = new SettingFile("config/settings.cfg");
        String string = "config/settings.cfg";

        assertThat(file.equals(string)).isFalse();
    }
}

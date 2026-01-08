package de.husten1997.changesettings;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ChangeStepFileTest {

    @Test
    void testConstructorWithParameters() {
        SettingFile settingFile = new SettingFile("config/settings.cfg");
        SettingEntry[] entries = {
                new SettingEntry(true, "key1", "value1", 0),
                new SettingEntry(true, "key2", "value2", 0)
        };

        ChangeStepFile changeStep = new ChangeStepFile(settingFile, entries);

        assertThat(changeStep.getSettingFile()).isEqualTo(settingFile);
        assertThat(changeStep.getSettingsEntries()).isEqualTo(entries);
    }

    @Test
    void testDefaultConstructor() {
        ChangeStepFile changeStep = new ChangeStepFile();

        assertThat(changeStep.getSettingFile()).isNotNull();
        assertThat(changeStep.getSettingsEntries()).isEmpty();
    }

    @Test
    void testSetters() {
        ChangeStepFile changeStep = new ChangeStepFile();
        SettingFile settingFile = new SettingFile("config/new.cfg");
        SettingEntry[] entries = {
                new SettingEntry(true, "key", "value", 0)
        };

        changeStep.setSettingFile(settingFile);
        changeStep.setSettingsEntries(entries);

        assertThat(changeStep.getSettingFile()).isEqualTo(settingFile);
        assertThat(changeStep.getSettingsEntries()).isEqualTo(entries);
    }

    @Test
    void testIdentify() {
        SettingFile settingFile = new SettingFile("config/settings.cfg");
        SettingEntry[] entries = new SettingEntry[0];
        ChangeStepFile changeStep = new ChangeStepFile(settingFile, entries);

        assertThat(changeStep.identify()).isEqualTo("Change file config/settings.cfg");
    }

    @Test
    void testExecute_singleSetting(@TempDir Path tempDir) throws IOException {
        // Create a config file
        Path configDir = tempDir.resolve("config");
        Files.createDirectories(configDir);
        Path configFile = configDir.resolve("settings.cfg");

        List<String> lines = Arrays.asList(
                "# Configuration",
                "B:enableFeature=false",
                "B:otherSetting=true"
        );
        Files.write(configFile, lines);

        // Create change step
        SettingFile settingFile = new SettingFile("config/settings.cfg");
        SettingEntry[] entries = {
                new SettingEntry(true, "enableFeature", "true", 0)
        };
        ChangeStepFile changeStep = new ChangeStepFile(settingFile, entries);

        // Execute
        boolean result = changeStep.execute(tempDir.toString());

        // Verify
        assertThat(result).isFalse(); // execute returns false according to implementation
        List<String> updatedLines = Files.readAllLines(configFile);
        assertThat(updatedLines.get(1)).isEqualTo("B:enableFeature=true");
        assertThat(updatedLines.get(2)).isEqualTo("B:otherSetting=true");
    }

    @Test
    void testExecute_multipleSettings(@TempDir Path tempDir) throws IOException {
        // Create a config file
        Path configDir = tempDir.resolve("config");
        Files.createDirectories(configDir);
        Path configFile = configDir.resolve("game.cfg");

        List<String> lines = Arrays.asList(
                "B:setting1=false",
                "B:setting2=100",
                "B:setting3=true"
        );
        Files.write(configFile, lines);

        // Create change step with multiple entries
        SettingFile settingFile = new SettingFile("config/game.cfg");
        SettingEntry[] entries = {
                new SettingEntry(true, "setting1", "true", 0),
                new SettingEntry(true, "setting3", "false", 0)
        };
        ChangeStepFile changeStep = new ChangeStepFile(settingFile, entries);

        // Execute
        changeStep.execute(tempDir.toString());

        // Verify
        List<String> updatedLines = Files.readAllLines(configFile);
        assertThat(updatedLines.get(0)).isEqualTo("B:setting1=true");
        assertThat(updatedLines.get(1)).isEqualTo("B:setting2=100"); // Unchanged
        assertThat(updatedLines.get(2)).isEqualTo("B:setting3=false");
    }

    @Test
    void testExecute_jsonFormat(@TempDir Path tempDir) throws IOException {
        // Create a JSON config file
        Path configDir = tempDir.resolve("config");
        Files.createDirectories(configDir);
        Path configFile = configDir.resolve("settings.json");

        List<String> lines = Arrays.asList(
                "{",
                "  \"threads\": 2,",
                "  \"timeout\": 30",
                "}"
        );
        Files.write(configFile, lines);

        // Create change step
        SettingFile settingFile = new SettingFile("config/settings.json");
        SettingEntry[] entries = {
                new SettingEntry(true, "threads", "4", 0)
        };
        ChangeStepFile changeStep = new ChangeStepFile(settingFile, entries);

        // Execute
        changeStep.execute(tempDir.toString());

        // Verify
        List<String> updatedLines = Files.readAllLines(configFile);
        assertThat(updatedLines.get(1)).contains("\"threads\": 4,");
    }

    @Test
    void testExecute_emptyEntries(@TempDir Path tempDir) throws IOException {
        // Create a config file
        Path configDir = tempDir.resolve("config");
        Files.createDirectories(configDir);
        Path configFile = configDir.resolve("settings.cfg");

        List<String> lines = Arrays.asList(
                "B:setting1=false",
                "B:setting2=true"
        );
        Files.write(configFile, lines);

        // Create change step with no entries
        SettingFile settingFile = new SettingFile("config/settings.cfg");
        SettingEntry[] entries = new SettingEntry[0];
        ChangeStepFile changeStep = new ChangeStepFile(settingFile, entries);

        // Execute
        changeStep.execute(tempDir.toString());

        // Verify - file should remain unchanged
        List<String> updatedLines = Files.readAllLines(configFile);
        assertThat(updatedLines).containsExactly("B:setting1=false", "B:setting2=true");
    }

    @Test
    void testExecute_withSubdirectory(@TempDir Path tempDir) throws IOException {
        // Create nested directory structure
        Path nestedDir = tempDir.resolve("instance/config");
        Files.createDirectories(nestedDir);
        Path configFile = nestedDir.resolve("settings.cfg");

        List<String> lines = Arrays.asList("B:test=false");
        Files.write(configFile, lines);

        // Create change step
        SettingFile settingFile = new SettingFile("instance/config/settings.cfg");
        SettingEntry[] entries = {
                new SettingEntry(true, "test", "true", 0)
        };
        ChangeStepFile changeStep = new ChangeStepFile(settingFile, entries);

        // Execute
        changeStep.execute(tempDir.toString());

        // Verify
        List<String> updatedLines = Files.readAllLines(configFile);
        assertThat(updatedLines.get(0)).isEqualTo("B:test=true");
    }
}

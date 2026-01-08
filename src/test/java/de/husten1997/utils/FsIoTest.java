package de.husten1997.utils;

import de.husten1997.changesettings.SettingEntry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class FsIoTest {

    // ConfigLine tests
    @Test
    void testConfigLine_getters() {
        FsIo.ConfigLine configLine = new FsIo.ConfigLine("testKey", "testValue", "prefix_", "_suffix");

        assertThat(configLine.getKey()).isEqualTo("testKey");
        assertThat(configLine.getValue()).isEqualTo("testValue");
    }

    @Test
    void testConfigLine_reconstruct() {
        FsIo.ConfigLine configLine = new FsIo.ConfigLine("testKey", "oldValue", "B:", "");

        String reconstructed = configLine.reconstruct("newValue");

        assertThat(reconstructed).isEqualTo("B:newValue");
    }

    @Test
    void testConfigLine_reconstructWithComplexBoundaries() {
        FsIo.ConfigLine configLine = new FsIo.ConfigLine("testKey", "true", "    B:testKey=", " # comment");

        String reconstructed = configLine.reconstruct("false");

        assertThat(reconstructed).isEqualTo("    B:testKey=false # comment");
    }

    @Test
    void testConfigLine_toString() {
        FsIo.ConfigLine configLine = new FsIo.ConfigLine("myKey", "myValue", "", "");

        assertThat(configLine.toString()).isEqualTo("ConfigLine{key='myKey', value='myValue'}");
    }

    // parseLine tests - Format: B:key=value
    @Test
    void testParseLine_booleanFormat() {
        Optional<FsIo.ConfigLine> result = FsIo.parseLine("B:fixImmersiveEngineering=true");

        assertThat(result).isPresent();
        assertThat(result.get().getKey()).isEqualTo("fixImmersiveEngineering");
        assertThat(result.get().getValue()).isEqualTo("true");
        assertThat(result.get().reconstruct("false")).isEqualTo("B:fixImmersiveEngineering=false");
    }

    @Test
    void testParseLine_booleanFormatWithWhitespace() {
        Optional<FsIo.ConfigLine> result = FsIo.parseLine("    B:enableFeature=false");

        assertThat(result).isPresent();
        assertThat(result.get().getKey()).isEqualTo("enableFeature");
        assertThat(result.get().getValue()).isEqualTo("false");
    }

    @Test
    void testParseLine_booleanFormatWithTrailingComment() {
        Optional<FsIo.ConfigLine> result = FsIo.parseLine("B:someOption=true # this is a comment");

        assertThat(result).isPresent();
        assertThat(result.get().getKey()).isEqualTo("someOption");
        assertThat(result.get().getValue()).isEqualTo("true");
        assertThat(result.get().reconstruct("false")).isEqualTo("B:someOption=false # this is a comment");
    }

    // parseLine tests - Format: I:key=value (Integer format)
    @Test
    void testParseLine_integerFormat() {
        Optional<FsIo.ConfigLine> result = FsIo.parseLine("I:maxPlayers=20");

        assertThat(result).isPresent();
        assertThat(result.get().getKey()).isEqualTo("maxPlayers");
        assertThat(result.get().getValue()).isEqualTo("20");
        assertThat(result.get().reconstruct("50")).isEqualTo("I:maxPlayers=50");
    }

    @Test
    void testParseLine_integerFormatWithWhitespace() {
        Optional<FsIo.ConfigLine> result = FsIo.parseLine("    I:renderDistance=16");

        assertThat(result).isPresent();
        assertThat(result.get().getKey()).isEqualTo("renderDistance");
        assertThat(result.get().getValue()).isEqualTo("16");
    }

    @Test
    void testParseLine_integerFormatWithComment() {
        Optional<FsIo.ConfigLine> result = FsIo.parseLine("I:timeout=300 # in seconds");

        assertThat(result).isPresent();
        assertThat(result.get().getKey()).isEqualTo("timeout");
        assertThat(result.get().getValue()).isEqualTo("300");
        assertThat(result.get().reconstruct("600")).isEqualTo("I:timeout=600 # in seconds");
    }

    @Test
    void testParseLine_integerFormatNegativeNumber() {
        Optional<FsIo.ConfigLine> result = FsIo.parseLine("I:offset=-10");

        assertThat(result).isPresent();
        assertThat(result.get().getKey()).isEqualTo("offset");
        assertThat(result.get().getValue()).isEqualTo("-10");
        assertThat(result.get().reconstruct("-20")).isEqualTo("I:offset=-20");
    }

    // parseLine tests - Format: C:key="value" (Character/String format)
    @Test
    void testParseLine_characterFormat() {
        Optional<FsIo.ConfigLine> result = FsIo.parseLine("C:serverName=\"MyServer\"");

        assertThat(result).isPresent();
        assertThat(result.get().getKey()).isEqualTo("serverName");
        assertThat(result.get().getValue()).isEqualTo("MyServer");
        assertThat(result.get().reconstruct("NewServer")).isEqualTo("C:serverName=\"NewServer\"");
    }

    @Test
    void testParseLine_characterFormatWithWhitespace() {
        Optional<FsIo.ConfigLine> result = FsIo.parseLine("    C:motd=\"Welcome to the server\"");

        assertThat(result).isPresent();
        assertThat(result.get().getKey()).isEqualTo("motd");
        assertThat(result.get().getValue()).isEqualTo("Welcome to the server");
    }

    @Test
    void testParseLine_characterFormatWithComment() {
        Optional<FsIo.ConfigLine> result = FsIo.parseLine("C:prefix=\"[Admin]\" # admin prefix");

        assertThat(result).isPresent();
        assertThat(result.get().getKey()).isEqualTo("prefix");
        assertThat(result.get().getValue()).isEqualTo("[Admin]");
        assertThat(result.get().reconstruct("[Moderator]")).isEqualTo("C:prefix=\"[Moderator]\" # admin prefix");
    }

    @Test
    void testParseLine_characterFormatEmptyString() {
        Optional<FsIo.ConfigLine> result = FsIo.parseLine("C:customValue=\"\"");

        assertThat(result).isPresent();
        assertThat(result.get().getKey()).isEqualTo("customValue");
        assertThat(result.get().getValue()).isEqualTo("");
        assertThat(result.get().reconstruct("newValue")).isEqualTo("C:customValue=\"newValue\"");
    }

    @Test
    void testParseLine_characterFormatWithSpecialCharacters() {
        Optional<FsIo.ConfigLine> result = FsIo.parseLine("C:message=\"Hello, World! @#$%\"");

        assertThat(result).isPresent();
        assertThat(result.get().getKey()).isEqualTo("message");
        assertThat(result.get().getValue()).isEqualTo("Hello, World! @#$%");
    }

    // parseLine tests - Format: "key": value
    @Test
    void testParseLine_jsonFormat() {
        Optional<FsIo.ConfigLine> result = FsIo.parseLine("\"chunk_builder_threads\": 0,");

        assertThat(result).isPresent();
        assertThat(result.get().getKey()).isEqualTo("chunk_builder_threads");
        assertThat(result.get().getValue()).isEqualTo("0");
        assertThat(result.get().reconstruct("4")).isEqualTo("\"chunk_builder_threads\": 4,");
    }

    @Test
    void testParseLine_jsonFormatWithSpacing() {
        Optional<FsIo.ConfigLine> result = FsIo.parseLine("    \"maxPlayers\":   16,");

        assertThat(result).isPresent();
        assertThat(result.get().getKey()).isEqualTo("maxPlayers");
        assertThat(result.get().getValue()).isEqualTo("16");
    }

    // parseLine tests - Format: key.with.dots: value
    @Test
    void testParseLine_yamlFormat() {
        Optional<FsIo.ConfigLine> result = FsIo.parseLine("serverutilities.claims.max_chunks: 100");

        assertThat(result).isPresent();
        assertThat(result.get().getKey()).isEqualTo("serverutilities.claims.max_chunks");
        assertThat(result.get().getValue()).isEqualTo("100");
        assertThat(result.get().reconstruct("200")).isEqualTo("serverutilities.claims.max_chunks: 200");
    }

    @Test
    void testParseLine_yamlFormatSimple() {
        Optional<FsIo.ConfigLine> result = FsIo.parseLine("timeout: 30");

        assertThat(result).isPresent();
        assertThat(result.get().getKey()).isEqualTo("timeout");
        assertThat(result.get().getValue()).isEqualTo("30");
    }

    // parseLine tests - No match
    @Test
    void testParseLine_noMatch_comment() {
        Optional<FsIo.ConfigLine> result = FsIo.parseLine("# This is a comment");

        assertThat(result).isEmpty();
    }

    @Test
    void testParseLine_noMatch_emptyLine() {
        Optional<FsIo.ConfigLine> result = FsIo.parseLine("");

        assertThat(result).isEmpty();
    }

    @Test
    void testParseLine_noMatch_invalidFormat() {
        Optional<FsIo.ConfigLine> result = FsIo.parseLine("some random text");

        assertThat(result).isEmpty();
    }

    // File copy tests
    @Test
    void testCopyFile(@TempDir Path tempDir) throws IOException {
        Path sourceFile = tempDir.resolve("source.txt");
        Path destFile = tempDir.resolve("dest.txt");
        String content = "Test content";

        Files.writeString(sourceFile, content);

        FsIo.copyFile(sourceFile.toString(), destFile.toString());

        assertThat(destFile).exists();
        assertThat(Files.readString(destFile)).isEqualTo(content);
    }

    @Test
    void testCopyFile_replacesExisting(@TempDir Path tempDir) throws IOException {
        Path sourceFile = tempDir.resolve("source.txt");
        Path destFile = tempDir.resolve("dest.txt");

        Files.writeString(sourceFile, "New content");
        Files.writeString(destFile, "Old content");

        FsIo.copyFile(sourceFile.toString(), destFile.toString());

        assertThat(Files.readString(destFile)).isEqualTo("New content");
    }

    @Test
    void testCopyDirectory(@TempDir Path tempDir) throws IOException {
        Path sourceDir = tempDir.resolve("source");
        Path destDir = tempDir.resolve("dest");
        Files.createDirectories(sourceDir);

        // Create some test files
        Files.writeString(sourceDir.resolve("file1.txt"), "Content 1");
        Path subDir = sourceDir.resolve("subdir");
        Files.createDirectories(subDir);
        Files.writeString(subDir.resolve("file2.txt"), "Content 2");

        FsIo.copyDirectory(sourceDir.toString(), destDir.toString());

        assertThat(destDir.resolve("file1.txt")).exists();
        assertThat(Files.readString(destDir.resolve("file1.txt"))).isEqualTo("Content 1");
        assertThat(destDir.resolve("subdir/file2.txt")).exists();
        assertThat(Files.readString(destDir.resolve("subdir/file2.txt"))).isEqualTo("Content 2");
    }

    // changeFile tests
    @Test
    void testChangeFile_singleMatch(@TempDir Path tempDir) throws IOException {
        Path configFile = tempDir.resolve("config.cfg");
        List<String> lines = Arrays.asList(
                "# Configuration file",
                "B:enableFeature=false",
                "B:otherOption=true"
        );
        Files.write(configFile, lines);

        SettingEntry[] settings = {
                new SettingEntry(true, "enableFeature", "true", 0)
        };

        int matches = FsIo.changeFile(configFile.toString(), settings);

        assertThat(matches).isEqualTo(1);
        List<String> result = Files.readAllLines(configFile);
        assertThat(result).containsExactly(
                "# Configuration file",
                "B:enableFeature=true",
                "B:otherOption=true"
        );
    }

    @Test
    void testChangeFile_multipleMatches(@TempDir Path tempDir) throws IOException {
        Path configFile = tempDir.resolve("settings.json");
        List<String> lines = Arrays.asList(
                "{",
                "  \"threads\": 2,",
                "  \"timeout\": 30,",
                "  \"enabled\": false",
                "}"
        );
        Files.write(configFile, lines);

        SettingEntry[] settings = {
                new SettingEntry(true, "threads", "4", 0),
                new SettingEntry(true, "enabled", "true", 0)
        };

        int matches = FsIo.changeFile(configFile.toString(), settings);

        assertThat(matches).isEqualTo(2);
        List<String> result = Files.readAllLines(configFile);
        assertThat(result.get(1)).contains("\"threads\": 4,");
        assertThat(result.get(3)).contains("\"enabled\": true");
    }

    @Test
    void testChangeFile_noMatches(@TempDir Path tempDir) throws IOException {
        Path configFile = tempDir.resolve("config.cfg");
        List<String> lines = Arrays.asList(
                "B:someOption=true",
                "B:anotherOption=false"
        );
        Files.write(configFile, lines);

        SettingEntry[] settings = {
                new SettingEntry(true, "nonExistentKey", "value", 0)
        };

        int matches = FsIo.changeFile(configFile.toString(), settings);

        assertThat(matches).isEqualTo(0);
    }

    @Test
    void testChangeFile_preservesUnmatchedLines(@TempDir Path tempDir) throws IOException {
        Path configFile = tempDir.resolve("config.cfg");
        List<String> lines = Arrays.asList(
                "# Comment line",
                "B:setting1=true",
                "",
                "B:setting2=false",
                "Some other text"
        );
        Files.write(configFile, lines);

        SettingEntry[] settings = {
                new SettingEntry(true, "setting1", "false", 0)
        };

        FsIo.changeFile(configFile.toString(), settings);

        List<String> result = Files.readAllLines(configFile);
        assertThat(result).hasSize(5);
        assertThat(result.get(0)).isEqualTo("# Comment line");
        assertThat(result.get(1)).isEqualTo("B:setting1=false");
        assertThat(result.get(2)).isEmpty();
        assertThat(result.get(4)).isEqualTo("Some other text");
    }

    @Test
    void testChangeFile_yamlFormat(@TempDir Path tempDir) throws IOException {
        Path configFile = tempDir.resolve("config.yml");
        List<String> lines = Arrays.asList(
                "server.port: 8080",
                "server.timeout: 30",
                "database.pool.size: 10"
        );
        Files.write(configFile, lines);

        SettingEntry[] settings = {
                new SettingEntry(true, "server.port", "9090", 0),
                new SettingEntry(true, "database.pool.size", "20", 0)
        };

        int matches = FsIo.changeFile(configFile.toString(), settings);

        assertThat(matches).isEqualTo(2);
        List<String> result = Files.readAllLines(configFile);
        assertThat(result.get(0)).isEqualTo("server.port: 9090");
        assertThat(result.get(2)).isEqualTo("database.pool.size: 20");
    }

    @Test
    void testChangeFile_integerFormat(@TempDir Path tempDir) throws IOException {
        Path configFile = tempDir.resolve("config.cfg");
        List<String> lines = Arrays.asList(
                "# Integer settings",
                "I:maxPlayers=20",
                "I:renderDistance=10",
                "I:tickRate=20"
        );
        Files.write(configFile, lines);

        SettingEntry[] settings = {
                new SettingEntry(true, "maxPlayers", "50", 0),
                new SettingEntry(true, "renderDistance", "16", 0)
        };

        int matches = FsIo.changeFile(configFile.toString(), settings);

        assertThat(matches).isEqualTo(2);
        List<String> result = Files.readAllLines(configFile);
        assertThat(result.get(1)).isEqualTo("I:maxPlayers=50");
        assertThat(result.get(2)).isEqualTo("I:renderDistance=16");
        assertThat(result.get(3)).isEqualTo("I:tickRate=20"); // Unchanged
    }

    @Test
    void testChangeFile_characterFormat(@TempDir Path tempDir) throws IOException {
        Path configFile = tempDir.resolve("config.cfg");
        List<String> lines = Arrays.asList(
                "# String settings",
                "C:serverName=\"OldName\"",
                "C:motd=\"Welcome!\"",
                "C:prefix=\"[User]\""
        );
        Files.write(configFile, lines);

        SettingEntry[] settings = {
                new SettingEntry(true, "serverName", "NewName", 0),
                new SettingEntry(true, "prefix", "[Admin]", 0)
        };

        int matches = FsIo.changeFile(configFile.toString(), settings);

        assertThat(matches).isEqualTo(2);
        List<String> result = Files.readAllLines(configFile);
        assertThat(result.get(1)).isEqualTo("C:serverName=\"NewName\"");
        assertThat(result.get(2)).isEqualTo("C:motd=\"Welcome!\""); // Unchanged
        assertThat(result.get(3)).isEqualTo("C:prefix=\"[Admin]\"");
    }

    @Test
    void testChangeFile_mixedFormats(@TempDir Path tempDir) throws IOException {
        Path configFile = tempDir.resolve("config.cfg");
        List<String> lines = Arrays.asList(
                "# Mixed configuration",
                "B:enableFeature=true",
                "I:maxConnections=100",
                "C:serverName=\"TestServer\"",
                "B:debugMode=false"
        );
        Files.write(configFile, lines);

        SettingEntry[] settings = {
                new SettingEntry(true, "enableFeature", "false", 0),
                new SettingEntry(true, "maxConnections", "200", 0),
                new SettingEntry(true, "serverName", "ProductionServer", 0)
        };

        int matches = FsIo.changeFile(configFile.toString(), settings);

        assertThat(matches).isEqualTo(3);
        List<String> result = Files.readAllLines(configFile);
        assertThat(result.get(1)).isEqualTo("B:enableFeature=false");
        assertThat(result.get(2)).isEqualTo("I:maxConnections=200");
        assertThat(result.get(3)).isEqualTo("C:serverName=\"ProductionServer\"");
        assertThat(result.get(4)).isEqualTo("B:debugMode=false"); // Unchanged
    }

    @Test
    void testChangeFile_integerWithNegativeValues(@TempDir Path tempDir) throws IOException {
        Path configFile = tempDir.resolve("config.cfg");
        List<String> lines = Arrays.asList(
                "I:xOffset=0",
                "I:yOffset=-10",
                "I:zOffset=5"
        );
        Files.write(configFile, lines);

        SettingEntry[] settings = {
                new SettingEntry(true, "xOffset", "-5", 0),
                new SettingEntry(true, "yOffset", "-20", 0)
        };

        int matches = FsIo.changeFile(configFile.toString(), settings);

        assertThat(matches).isEqualTo(2);
        List<String> result = Files.readAllLines(configFile);
        assertThat(result.get(0)).isEqualTo("I:xOffset=-5");
        assertThat(result.get(1)).isEqualTo("I:yOffset=-20");
    }
}

package de.husten1997.utils;

import de.husten1997.changesettings.SettingEntry;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static de.husten1997.main.Log.setupLogger;


public class FsIo {
    private static final Logger LOGGER = setupLogger( FsIo.class.getName() );

    public static void copyDirectory(String sourceDirectoryLocation, String destinationDirectoryLocation) throws IOException {
        Files.walk(Paths.get(sourceDirectoryLocation)).forEach(source -> {
            String file = source.toString().substring(sourceDirectoryLocation.length());
            Path destination = Paths.get(destinationDirectoryLocation, file);

            if (Files.isDirectory(source) && Files.exists(destination)) {
                LOGGER.log(Level.FINER, String.format("Skip %s (Folder already exists)", "." + file));
                return;
            }

            try {
                Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                LOGGER.log(Level.FINEST, String.format("Successfully copy %s", file));
            } catch (IOException e) {
                LOGGER.log(Level.FINE, String.format("\tFailed copy %s (because %s)", file, e.getClass().getName()));
            }
        });
    }

    public static void copyFile(String sourceFileLocation, String destinationFileLocation) throws IOException {
        Path source = Paths.get(sourceFileLocation);
        Path destination = Paths.get(destinationFileLocation);
        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
    }

    // Data class to hold parsed config line information
    public static class ConfigLine {
        private final String key;
        private final String value;
        private final String before;  // Everything before the value
        private final String after;   // Everything after the value

        public ConfigLine(String key, String value, String before, String after) {
            this.key = key;
            this.value = value;
            this.before = before;
            this.after = after;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        // Reconstruct the line with a new value
        public String reconstruct(String newValue) {
            return before + newValue + after;
        }

        @Override
        public String toString() {
            return String.format("ConfigLine{key='%s', value='%s'}", key, value);
        }
    }

    /**
     * Tries to parse a config line using all known patterns.
     * Returns Optional.empty() if no pattern matches.
     */
    public static Optional<ConfigLine> parseLine(String line) {
        // List of patterns for different config formats
        final List<Pattern> PATTERNS = Arrays.asList(
                // Format: C:key="value" (Character/String format with quotes, e.g., "C:serverName=\"MyServer\"")
                // This must be checked before the general [BIC]: pattern to handle quoted values correctly
                Pattern.compile("^(?<before>\\s*C:(?<key>\\w+)=\")(?<value>[^\"]*)(?<after>\".*)$"),

                // Format: [BIC]:key=value (e.g., "B:fixImmersiveEngineering=true", "I:maxPlayers=20")
                // Supports Boolean (B:), Integer (I:), and Character (C:) prefixes
                // Value can contain word characters, hyphens, and plus signs for numbers
                Pattern.compile("^(?<before>\\s*[BIC]:(?<key>\\w+)=)(?<value>[\\w+\\-]+)(?<after>.*)$"),

                // Format: "key": value (JSON-style, e.g., "\"chunk_builder_threads\": 0,")
                Pattern.compile("^(?<before>\\s*\"(?<key>\\w+)\":\\s*)(?<value>\\w+)(?<after>.*)$"),

                // Format: key.with.dots: value (YAML-style, e.g., "serverutilities.claims.max_chunks: 100")
                Pattern.compile("^(?<before>\\s*(?<key>[\\w.]+):\\s*)(?<value>\\w+)(?<after>.*)$")
        );

        for (Pattern pattern : PATTERNS) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                return Optional.of(new ConfigLine(
                        matcher.group("key"),
                        matcher.group("value"),
                        matcher.group("before"),
                        matcher.group("after")
                ));
            }
        }
        return Optional.empty();
    }



    public static int changeFile(String settingFile, SettingEntry[] settingEntry) throws IOException {
        List<String> newLines = new ArrayList<>();
        List<String> keyMap = new ArrayList<>();
        List<Boolean> foundIndices = new ArrayList<>();

        for (SettingEntry entry: settingEntry) {
            keyMap.add(entry.getSettingName());
            foundIndices.add(false);
        }
        LOGGER.log(Level.FINE, "Starting file " + settingFile);
        int matches = 0;
        for (String line : Files.readAllLines(Paths.get(settingFile), StandardCharsets.UTF_8)) {
            LOGGER.log(Level.FINEST, "\tOriginal: " + line);

            Optional<ConfigLine> result = parseLine(line);
            if (result.isPresent()) {
                ConfigLine config = result.get();
                int index = keyMap.indexOf(config.getKey());

                if (index == -1) {
                    newLines.add(line);
                    LOGGER.log(Level.FINEST, "\tNo setting found to change.");
                } else {
                    LOGGER.log(Level.FINEST, "\tFound setting to change " + config);
                    newLines.add(config.reconstruct(settingEntry[index].getSettingValue()));
                    matches++;
                    foundIndices.set(index, true);
                }

            } else {
                LOGGER.log(Level.FINEST, "\tNo pattern matched.");
                newLines.add(line);
            }
        }
        LOGGER.log(Level.FINE, String.format("Finished file: Found %d / %d matches.", matches, settingEntry.length));
        if (matches + 1 < settingEntry.length) {
            for (int i = 0; i <  foundIndices.size(); i++) {
                if (foundIndices.get(i)) {
                    continue;
                }
                LOGGER.log(Level.FINE, "Not found: " + settingEntry[i].toString());
            }
        }

        Files.write(Paths.get(settingFile), newLines, StandardCharsets.UTF_8);

        return matches;
    }

    public enum CheckPathResponse {
        INVALID_PATH,
        EMPTY_PATH,
        NULL_PATH,
        WRONGTARGET_PATH,
        NOTFOUND_PATH,
        VALID_PATH
    }

    public static CheckPathResponse checkPath(String path, boolean directoryExpected) {
        if ( path == null) { return CheckPathResponse.NULL_PATH; }
        if ( path.isEmpty() ) { return CheckPathResponse.EMPTY_PATH; }

        Path tmpPath;
        try {
            tmpPath = Paths.get(path);
        } catch (NullPointerException ex) {
            return CheckPathResponse.NULL_PATH;
        } catch (InvalidPathException ex) {
            return CheckPathResponse.INVALID_PATH;
        }

        if ( tmpPath.toFile().isDirectory() != directoryExpected ) { return CheckPathResponse.WRONGTARGET_PATH; }
        if ( !tmpPath.toFile().exists() ) { return CheckPathResponse.NOTFOUND_PATH; }

        return CheckPathResponse.VALID_PATH;
    }

    public static CheckPathResponse checkPath(String path) {
        return checkPath(path, true);
    }

    public static CheckPathResponse checkFile(String path) {
        return checkPath(path, false);
    }
}

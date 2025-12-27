package de.husten1997.copyinstance;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import static de.husten1997.main.Log.setupLogger;


public class FileSystemCopy {
    private static final Logger LOGGER = setupLogger( FileSystemCopy.class.getName() );

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
//                throw new UncheckedIOException(e);
//                LOGGER.log(Level.FINE, String.format("Failed Step: Copy file %s to %s", source, destination));
                LOGGER.log(Level.FINE, String.format("\tFailed copy %s (because %s)", file, e.getClass().getName()));
            }
        });
    }

    public static void copyFile(String sourceFileLocation, String destinationFileLocation) throws IOException {
        Path source = Paths.get(sourceFileLocation);
        Path destination = Paths.get(destinationFileLocation);
        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
//        try {
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}

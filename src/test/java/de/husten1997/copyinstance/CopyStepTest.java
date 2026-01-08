package de.husten1997.copyinstance;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class CopyStepTest {

    // CopyFile tests
    @Test
    void testCopyFile_constructor() {
        CopyStep.CopyFile copyFile = new CopyStep.CopyFile("/source/file.txt", "/target/file.txt");

        assertThat(copyFile.getSourcePath()).isEqualTo("/source/file.txt");
        assertThat(copyFile.getTargetPath()).isEqualTo("/target/file.txt");
    }

    @Test
    void testCopyFile_setters() {
        CopyStep.CopyFile copyFile = new CopyStep.CopyFile("/source/file.txt", "/target/file.txt");

        copyFile.setSourcePath("/new/source.txt");
        copyFile.setTargetPath("/new/target.txt");

        assertThat(copyFile.getSourcePath()).isEqualTo("/new/source.txt");
        assertThat(copyFile.getTargetPath()).isEqualTo("/new/target.txt");
    }

    @Test
    void testCopyFile_identify() {
        CopyStep.CopyFile copyFile = new CopyStep.CopyFile("/source/file.txt", "/target/file.txt");

        assertThat(copyFile.identify()).isEqualTo("Copy File /source/file.txt to /target/file.txt");
    }

    @Test
    void testCopyFile_execute(@TempDir Path tempDir) throws IOException {
        Path sourceFile = tempDir.resolve("source.txt");
        Path targetFile = tempDir.resolve("target.txt");
        String content = "Test file content";

        Files.writeString(sourceFile, content);

        CopyStep.CopyFile copyFile = new CopyStep.CopyFile(sourceFile.toString(), targetFile.toString());
        boolean result = copyFile.execute();

        assertThat(result).isFalse(); // execute returns false according to implementation
        assertThat(targetFile).exists();
        assertThat(Files.readString(targetFile)).isEqualTo(content);
    }

    @Test
    void testCopyFile_executeReplacesExisting(@TempDir Path tempDir) throws IOException {
        Path sourceFile = tempDir.resolve("source.txt");
        Path targetFile = tempDir.resolve("target.txt");

        Files.writeString(sourceFile, "New content");
        Files.writeString(targetFile, "Old content");

        CopyStep.CopyFile copyFile = new CopyStep.CopyFile(sourceFile.toString(), targetFile.toString());
        copyFile.execute();

        assertThat(Files.readString(targetFile)).isEqualTo("New content");
    }

    // CopyFolder tests
    @Test
    void testCopyFolder_constructor() {
        CopyStep.CopyFolder copyFolder = new CopyStep.CopyFolder("/source/folder", "/target/folder");

        assertThat(copyFolder.getSourcePath()).isEqualTo("/source/folder");
        assertThat(copyFolder.getTargetPath()).isEqualTo("/target/folder");
    }

    @Test
    void testCopyFolder_setters() {
        CopyStep.CopyFolder copyFolder = new CopyStep.CopyFolder("/source/folder", "/target/folder");

        copyFolder.setSourcePath("/new/source");
        copyFolder.setTargetPath("/new/target");

        assertThat(copyFolder.getSourcePath()).isEqualTo("/new/source");
        assertThat(copyFolder.getTargetPath()).isEqualTo("/new/target");
    }

    @Test
    void testCopyFolder_identify() {
        CopyStep.CopyFolder copyFolder = new CopyStep.CopyFolder("/source/folder", "/target/folder");

        assertThat(copyFolder.identify()).isEqualTo("Copy Folder /source/folder to /target/folder");
    }

    @Test
    void testCopyFolder_execute(@TempDir Path tempDir) throws IOException {
        Path sourceDir = tempDir.resolve("source");
        Path targetDir = tempDir.resolve("target");
        Files.createDirectories(sourceDir);

        // Create test files in source
        Files.writeString(sourceDir.resolve("file1.txt"), "Content 1");
        Path subDir = sourceDir.resolve("subdir");
        Files.createDirectories(subDir);
        Files.writeString(subDir.resolve("file2.txt"), "Content 2");

        CopyStep.CopyFolder copyFolder = new CopyStep.CopyFolder(sourceDir.toString(), targetDir.toString());
        boolean result = copyFolder.execute();

        assertThat(result).isFalse(); // execute returns false according to implementation
        assertThat(targetDir).exists();
        assertThat(targetDir.resolve("file1.txt")).exists();
        assertThat(Files.readString(targetDir.resolve("file1.txt"))).isEqualTo("Content 1");
        assertThat(targetDir.resolve("subdir/file2.txt")).exists();
        assertThat(Files.readString(targetDir.resolve("subdir/file2.txt"))).isEqualTo("Content 2");
    }

    @Test
    void testCopyFolder_executeWithMultipleFiles(@TempDir Path tempDir) throws IOException {
        Path sourceDir = tempDir.resolve("source");
        Path targetDir = tempDir.resolve("target");
        Files.createDirectories(sourceDir);

        // Create multiple files
        for (int i = 1; i <= 5; i++) {
            Files.writeString(sourceDir.resolve("file" + i + ".txt"), "Content " + i);
        }

        CopyStep.CopyFolder copyFolder = new CopyStep.CopyFolder(sourceDir.toString(), targetDir.toString());
        copyFolder.execute();

        for (int i = 1; i <= 5; i++) {
            Path targetFile = targetDir.resolve("file" + i + ".txt");
            assertThat(targetFile).exists();
            assertThat(Files.readString(targetFile)).isEqualTo("Content " + i);
        }
    }

    @Test
    void testCopyFolder_executeWithNestedDirectories(@TempDir Path tempDir) throws IOException {
        Path sourceDir = tempDir.resolve("source");
        Path targetDir = tempDir.resolve("target");
        Files.createDirectories(sourceDir);

        // Create nested structure
        Path level1 = sourceDir.resolve("level1");
        Path level2 = level1.resolve("level2");
        Files.createDirectories(level2);
        Files.writeString(level2.resolve("deep.txt"), "Deep content");

        CopyStep.CopyFolder copyFolder = new CopyStep.CopyFolder(sourceDir.toString(), targetDir.toString());
        copyFolder.execute();

        Path targetDeepFile = targetDir.resolve("level1/level2/deep.txt");
        assertThat(targetDeepFile).exists();
        assertThat(Files.readString(targetDeepFile)).isEqualTo("Deep content");
    }
}

package de.husten1997.copyinstance;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static de.husten1997.utils.FsIo.copyDirectory;
import static de.husten1997.utils.FsIo.copyFile;

public abstract class CopyStep {
    private String sourcePath;
    private String targetPath;

    public CopyStep(@NotNull String sourcePath, @NotNull String targetPath) {
        this.sourcePath = sourcePath;
        this.targetPath = targetPath;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public abstract boolean execute() throws IOException;
    public abstract String identify();

    public static class CopyFolder extends CopyStep {
        public CopyFolder(@NotNull String sourcePath, @NotNull String targetPath) {
            super(sourcePath, targetPath);
        }

        @Override
        public boolean execute() throws IOException {
            copyDirectory(this.getSourcePath(), this.getTargetPath());
            return false;
        }

        @Override
        public String identify() {
            return String.format("Copy Folder %s to %s", this.getSourcePath(), this.getTargetPath());
        }
    }
    public static class CopyFile extends CopyStep {
        public CopyFile(@NotNull String sourcePath, @NotNull String targetPath) {
            super(sourcePath, targetPath);
        }

        @Override
        public boolean execute() throws IOException {
            copyFile(this.getSourcePath(), this.getTargetPath());
            return false;
        }

        @Override
        public String identify() {
            return String.format("Copy File %s to %s", this.getSourcePath(), this.getTargetPath());
        }
    }
}

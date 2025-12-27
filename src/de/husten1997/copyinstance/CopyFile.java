package de.husten1997.copyinstance;

import de.husten1997.actionpipeline.ActionPipelineStep;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static de.husten1997.copyinstance.FileSystemCopy.copyFile;

public class CopyFile implements ActionPipelineStep {
    private String sourcePath;
    private String targetPath;

    public CopyFile(@NotNull String sourcePath, @NotNull String targetPath) {
        this.targetPath = targetPath;
        this.sourcePath = sourcePath;
    }

    @Override
    public boolean execute() throws IOException {
        copyFile(this.sourcePath, this.targetPath);
        return false;
    }

    @Override
    public String identify() {
        return String.format("Copy File %s to %s", this.sourcePath, this.targetPath);
    }
}

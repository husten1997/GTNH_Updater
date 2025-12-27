package de.husten1997.copyinstance;

import de.husten1997.actionpipeline.ActionPipelineStep;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static de.husten1997.copyinstance.FileSystemCopy.copyDirectory;

public class CopyFolder implements ActionPipelineStep {
    private String sourcePath;
    private String targetPath;

    public CopyFolder(@NotNull String sourcePath, @NotNull String targetPath) {
        this.targetPath = targetPath;
        this.sourcePath = sourcePath;
    }

    @Override
    public boolean execute() throws IOException {
        copyDirectory(this.sourcePath, this.targetPath);
        return false;
    }

    @Override
    public String identify() {
        return String.format("Copy Folder %s to %s", this.sourcePath, this.targetPath);
    }
}

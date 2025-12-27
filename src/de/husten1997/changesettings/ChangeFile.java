package de.husten1997.changesettings;

import de.husten1997.actionpipeline.ActionPipelineStep;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class ChangeFile implements ActionPipelineStep {
    private File file;
    private String settingName;
    private String settingValue;

    public ChangeFile(@NotNull String filePath, @NotNull String settingName, String settingValue) {
        this.file = new File(filePath);
        this.settingName = settingName;
        this.settingValue = settingValue;
    }

    public boolean execute() {
        return false;
    }

    @Override
    public String identify() {
        return String.format("Change file {0}, such that {1} = {2}", new Object[]{this.file, this.settingName, this.settingValue});
    }
}

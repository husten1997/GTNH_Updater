package de.husten1997.changesettings;

import de.husten1997.actionpipeline.ActionPipelineStep;
import de.husten1997.main.ApplicationContext;
import org.jetbrains.annotations.NotNull;

public class ChangeSettingsExecuter implements ActionPipelineStep {
    private final ChangeStepHandler changeSettingsHandler;
    private final String sourceInstance;
    private final String targetInstance;

//    private static final Logger LOGGER = setupLogger( ChangeSettingsExecuter.class.getName() );

    public ChangeSettingsExecuter(@NotNull ApplicationContext config) {
        this.sourceInstance = config.getOldGtnhFolderPath().endsWith("/") ? config.getOldGtnhFolderPath() : config.getOldGtnhFolderPath() + "/";
        this.targetInstance = config.getNewGtnhFolderPath().endsWith("/") ? config.getNewGtnhFolderPath() : config.getNewGtnhFolderPath() + "/";
        this.changeSettingsHandler = new ChangeStepHandler(config.getChangeSettingsBatch());
    }

    @Override
    public boolean execute() {
        return changeSettingsHandler.execute(this.targetInstance);
    }

    @Override
    public String identify() {
        return "Overall change settings action";
    }
}

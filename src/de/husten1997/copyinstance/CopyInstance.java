package de.husten1997.copyinstance;

import de.husten1997.actionpipeline.ActionPipelineStep;
import de.husten1997.gui.Gui;
import static de.husten1997.main.Log.setupLogger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.*;

import static de.husten1997.copyinstance.RelevantFolder.RELEVANT_FILES;
import static de.husten1997.copyinstance.RelevantFolder.RELEVANT_FOLDERS;


public class CopyInstance implements ActionPipelineStep {
    private String sourceInstance;
    private String targetInstance;

    private ActionPipelineStep[] actionBatch;

    private static final Logger LOGGER = setupLogger( CopyInstance.class.getName() );

    public CopyInstance(@NotNull String sourceInstance, @NotNull String targetInstance) {
        this.sourceInstance = sourceInstance.endsWith("/") ? sourceInstance : sourceInstance + "/";;
        this.targetInstance = targetInstance.endsWith("/") ? targetInstance : targetInstance + "/";;

        this.actionBatch = createActionBatch(this.sourceInstance, this.targetInstance);
    }

    private ActionPipelineStep[] createActionBatch(String sourceInstance, String targetInstance) {
        ArrayList<ActionPipelineStep> tmpCopyActions = new ArrayList<ActionPipelineStep>();

        for (String relevantFolder: RELEVANT_FOLDERS) {
            tmpCopyActions.add(new CopyFolder(sourceInstance + relevantFolder, targetInstance + relevantFolder));
        }

        for (String relevantFolder: RELEVANT_FILES) {
            tmpCopyActions.add(new CopyFile(sourceInstance + relevantFolder, targetInstance + relevantFolder));
        }

        return tmpCopyActions.toArray(new ActionPipelineStep[0]);
    }


    @Override
    public boolean execute() {
        LOGGER.log(Level.FINE, "Start copy of instance -----------------------");
        for (ActionPipelineStep step: this.actionBatch) {
            LOGGER.log(Level.FINE, String.format("Copy instance folder: %s", step.identify()));
            try {
                step.execute();
                LOGGER.log(Level.FINE, "\t...Done");
            } catch (IOException e) {
                LOGGER.log(Level.FINE, String.format("\t...Failed because %s", e.getClass().getName()));
            }
        }
        LOGGER.log(Level.FINE, "Finished copy of instance -----------------------");
        return false;
    }

    @Override
    public String identify() {
        return "Overall copy of instance";
    }
}

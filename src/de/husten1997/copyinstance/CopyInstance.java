package de.husten1997.copyinstance;

import de.husten1997.actionpipeline.ActionPipelineStep;
import static de.husten1997.main.Log.setupLogger;

import de.husten1997.main.ApplicationContext;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.*;

public class CopyInstance implements ActionPipelineStep {
    private final String sourceInstance;
    private final String targetInstance;

    private final CopyPlan[] planBatch;
    private final CopyStep[] actionBatch;

    private static final Logger LOGGER = setupLogger( CopyInstance.class.getName() );

    public CopyInstance(@NotNull ApplicationContext config) {
        this.sourceInstance = config.getOldGtnhFolderPath().endsWith("/") ? config.getOldGtnhFolderPath() : config.getOldGtnhFolderPath() + "/";
        this.targetInstance = config.getNewGtnhFolderPath().endsWith("/") ? config.getNewGtnhFolderPath() : config.getNewGtnhFolderPath() + "/";
        this.planBatch = config.getCopyPlanBatch();
        this.actionBatch = getCopyActionBatch(this.planBatch, this.sourceInstance, this.targetInstance);
    }

    public CopyStep[] getCopyActionBatch(CopyPlan[] copyPlanBatch, String sourceInstance, String targetInstance) {
        ArrayList<CopyStep> tmpActionBatch = new ArrayList<>();

        for (CopyPlan step: copyPlanBatch) {
            if (step.isActive()) {
                tmpActionBatch.add(step.getAction(sourceInstance, targetInstance));
            }
        }
        return tmpActionBatch.toArray(new CopyStep[0]);
    }

    @Override
    public boolean execute() {
        LOGGER.log(Level.FINE, "Start copy of instance -----------------------");
        for (CopyStep step: this.actionBatch) {
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

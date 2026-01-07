package de.husten1997.actionpipeline;

import java.io.IOException;

public interface ActionPipelineStep {
    public boolean execute() throws IOException;
    public String identify();
}

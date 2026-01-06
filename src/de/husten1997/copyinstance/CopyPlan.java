package de.husten1997.copyinstance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
    @JsonSubTypes.Type(value = CopyPlan.CopyPlanFile.class, name = "File"),
    @JsonSubTypes.Type(value = CopyPlan.CopyPlanFolder.class, name = "Folder")
})

public abstract class CopyPlan {
    private String name;
    private String target;
    private boolean active;

    public CopyPlan(String name, String target, boolean active) {
        this.name = name;
        this.target = target;
        this.active = active;
    }

    public CopyPlan() {
        this.name = "";
        this.target = "";
        this.active = false;
    }

    public abstract CopyStep getAction(String sourceInstance, String targetInstance);
    public abstract String getDisplayName();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public static class CopyPlanFolder extends CopyPlan {
        public CopyPlanFolder(String name, String target, boolean active) {
            super(name, target, active);
        }

        public CopyPlanFolder() {}

        @Override
        public CopyStep getAction(String sourceInstance, String targetInstance) {
            return new CopyStep.CopyFolder(sourceInstance + this.getTarget(), targetInstance + this.getTarget());
        }

        @Override
        public String getDisplayName() {
            return "./" + this.getName() + "/*";
        }
    }

    public static class CopyPlanFile extends CopyPlan {
        public CopyPlanFile(String name, String target, boolean active) {
            super(name, target, active);
        }

        public CopyPlanFile() {}

        @Override
        public CopyStep getAction(String sourceInstance, String targetInstance) {
            return new CopyStep.CopyFile(sourceInstance + this.getTarget(), targetInstance + this.getTarget());
        }

        @Override
        public String getDisplayName() {
            return "./" + this.getName();
        }
    }
}

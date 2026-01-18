package de.husten1997.main;

import de.husten1997.changesettings.ChangeStepFile;
import de.husten1997.copyinstance.CopyPlan;
import de.husten1997.utils.FsIo;

import java.util.ArrayList;

import static de.husten1997.copyinstance.RelevantFolder.RELEVANT_FILES;
import static de.husten1997.copyinstance.RelevantFolder.RELEVANT_FOLDERS;

public class ApplicationContext {
    private int windowWidth;
    private int windowHeight;

    private String oldGtnhFolderPath;
    private String newGtnhFolderPath;

    private CopyPlan[] copyPlanBatch;
    private ChangeStepFile[] changeSettingsBatch;


    public ApplicationContext(
            int windowWidth, int windowHeight,
            String oldGtnhFolderPath,
            String newGtnhFolderPath,
            CopyPlan[] copyPlanBatch,
            ChangeStepFile[] changeSettingsBatch
    ) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.oldGtnhFolderPath = oldGtnhFolderPath;
        this.newGtnhFolderPath = newGtnhFolderPath;
        this.copyPlanBatch = copyPlanBatch;
        this.changeSettingsBatch = changeSettingsBatch;
    }

    public ApplicationContext() {
        this.windowWidth = 800;
        this.windowHeight = 600;

        this.oldGtnhFolderPath = "";
        this.newGtnhFolderPath = "";
        this.copyPlanBatch = createDefaultCopyPlanBatch();
        this.changeSettingsBatch = new ChangeStepFile[0];
    }

    // Application GUI Config
    public int getWindowWidth() {
        return windowWidth;
    }

    public void setWindowWidth(int windowWidth) {
        this.windowWidth = windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public void setWindowHeight(int windowHeight) {
        this.windowHeight = windowHeight;
    }

    // GTNH Paths
    public String getOldGtnhFolderPath() {
        return oldGtnhFolderPath;
    }

    public void setOldGtnhFolderPath(String oldGtnhFolderPath) {
        this.oldGtnhFolderPath = oldGtnhFolderPath;
    }

    public String getNewGtnhFolderPath() {
        return newGtnhFolderPath;
    }

    public void setNewGtnhFolderPath(String newGtnhFolderPath) {
        this.newGtnhFolderPath = newGtnhFolderPath;
    }


    // GTNH Settings
    public ChangeStepFile[] getChangeSettingsBatch() {
        return changeSettingsBatch;
    }

    public void setChangeSettingsBatch(ChangeStepFile[] changeSettingsBatch) {
        this.changeSettingsBatch = changeSettingsBatch;
    }


    // GTNH Instance Copy
    private CopyPlan[] createDefaultCopyPlanBatch() {
        ArrayList<CopyPlan> tmpCopyPlan = new ArrayList<>();

        for (String relevantFolder: RELEVANT_FOLDERS) {
            tmpCopyPlan.add(new CopyPlan.CopyPlanFolder(relevantFolder, relevantFolder, true));
        }

        for (String relevantFile: RELEVANT_FILES) {
            tmpCopyPlan.add(new CopyPlan.CopyPlanFile(relevantFile, relevantFile, true));
        }

        return tmpCopyPlan.toArray(new CopyPlan[0]);
    }

    public CopyPlan[] getCopyPlanBatch() {
        return copyPlanBatch;
    }

    public void setCopyPlanBatch(CopyPlan[] copyPlanBatch) {
        this.copyPlanBatch = copyPlanBatch;
    }


    // Checks
    public boolean canCopy() {
        // Check source Path
        if (this.getOldGtnhFolderPath() == null) {
            return false;
        }
        if (this.getOldGtnhFolderPath().isEmpty()) {
            return false;
        }

        // Check destination Path
        if (this.getNewGtnhFolderPath() == null) {
            return false;
        }
        if (this.getNewGtnhFolderPath().isEmpty()) {
            return false;
        }

        // Check Copy Plan
        if (this.getCopyPlanBatch() == null) {
            return false;
        }
        if (this.getCopyPlanBatch().length == 0) {
            return false;
        }

        return true;
    }

    public boolean testValidNewGtnhPath() {
        FsIo.CheckPathResponse response = FsIo.checkPath( this.newGtnhFolderPath );

        System.out.println(response);

        if ( response == FsIo.CheckPathResponse.VALID_PATH ) { return true; }
        return false;
    }

    public boolean testValidOldGtnhPath() {
        FsIo.CheckPathResponse response = FsIo.checkPath( this.oldGtnhFolderPath );

        if ( response == FsIo.CheckPathResponse.VALID_PATH ) { return true; }
        return false;
    }
}

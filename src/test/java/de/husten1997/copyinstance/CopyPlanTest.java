package de.husten1997.copyinstance;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CopyPlanTest {

    // CopyPlanFolder tests
    @Test
    void testCopyPlanFolder_constructorWithParameters() {
        CopyPlan.CopyPlanFolder folder = new CopyPlan.CopyPlanFolder("saves", "/saves", true);

        assertThat(folder.getName()).isEqualTo("saves");
        assertThat(folder.getTarget()).isEqualTo("/saves");
        assertThat(folder.isActive()).isTrue();
    }

    @Test
    void testCopyPlanFolder_defaultConstructor() {
        CopyPlan.CopyPlanFolder folder = new CopyPlan.CopyPlanFolder();

        assertThat(folder.getName()).isEmpty();
        assertThat(folder.getTarget()).isEmpty();
        assertThat(folder.isActive()).isFalse();
    }

    @Test
    void testCopyPlanFolder_setters() {
        CopyPlan.CopyPlanFolder folder = new CopyPlan.CopyPlanFolder();

        folder.setName("config");
        folder.setTarget("/config");
        folder.setActive(true);

        assertThat(folder.getName()).isEqualTo("config");
        assertThat(folder.getTarget()).isEqualTo("/config");
        assertThat(folder.isActive()).isTrue();
    }

    @Test
    void testCopyPlanFolder_getDisplayName() {
        CopyPlan.CopyPlanFolder folder = new CopyPlan.CopyPlanFolder("saves", "/saves", true);

        assertThat(folder.getDisplayName()).isEqualTo("./saves/*");
    }

    @Test
    void testCopyPlanFolder_getAction() {
        CopyPlan.CopyPlanFolder folder = new CopyPlan.CopyPlanFolder("saves", "/saves", true);

        CopyStep action = folder.getAction("/source/instance", "/target/instance");

        assertThat(action).isInstanceOf(CopyStep.CopyFolder.class);
        assertThat(action.getSourcePath()).isEqualTo("/source/instance/saves");
        assertThat(action.getTargetPath()).isEqualTo("/target/instance/saves");
    }

    // CopyPlanFile tests
    @Test
    void testCopyPlanFile_constructorWithParameters() {
        CopyPlan.CopyPlanFile file = new CopyPlan.CopyPlanFile("options.txt", "/options.txt", true);

        assertThat(file.getName()).isEqualTo("options.txt");
        assertThat(file.getTarget()).isEqualTo("/options.txt");
        assertThat(file.isActive()).isTrue();
    }

    @Test
    void testCopyPlanFile_defaultConstructor() {
        CopyPlan.CopyPlanFile file = new CopyPlan.CopyPlanFile();

        assertThat(file.getName()).isEmpty();
        assertThat(file.getTarget()).isEmpty();
        assertThat(file.isActive()).isFalse();
    }

    @Test
    void testCopyPlanFile_setters() {
        CopyPlan.CopyPlanFile file = new CopyPlan.CopyPlanFile();

        file.setName("settings.cfg");
        file.setTarget("/config/settings.cfg");
        file.setActive(true);

        assertThat(file.getName()).isEqualTo("settings.cfg");
        assertThat(file.getTarget()).isEqualTo("/config/settings.cfg");
        assertThat(file.isActive()).isTrue();
    }

    @Test
    void testCopyPlanFile_getDisplayName() {
        CopyPlan.CopyPlanFile file = new CopyPlan.CopyPlanFile("options.txt", "/options.txt", true);

        assertThat(file.getDisplayName()).isEqualTo("./options.txt");
    }

    @Test
    void testCopyPlanFile_getAction() {
        CopyPlan.CopyPlanFile file = new CopyPlan.CopyPlanFile("options.txt", "/options.txt", true);

        CopyStep action = file.getAction("/source/instance", "/target/instance");

        assertThat(action).isInstanceOf(CopyStep.CopyFile.class);
        assertThat(action.getSourcePath()).isEqualTo("/source/instance/options.txt");
        assertThat(action.getTargetPath()).isEqualTo("/target/instance/options.txt");
    }

    @Test
    void testCopyPlanFile_getActionWithSubdirectory() {
        CopyPlan.CopyPlanFile file = new CopyPlan.CopyPlanFile("server.properties", "/config/server.properties", true);

        CopyStep action = file.getAction("/home/user/minecraft", "/home/user/minecraft-new");

        assertThat(action.getSourcePath()).isEqualTo("/home/user/minecraft/config/server.properties");
        assertThat(action.getTargetPath()).isEqualTo("/home/user/minecraft-new/config/server.properties");
    }

    @Test
    void testCopyPlanFolder_getActionWithSubdirectory() {
        CopyPlan.CopyPlanFolder folder = new CopyPlan.CopyPlanFolder("mods", "/config/mods", true);

        CopyStep action = folder.getAction("/home/user/minecraft", "/home/user/minecraft-new");

        assertThat(action.getSourcePath()).isEqualTo("/home/user/minecraft/config/mods");
        assertThat(action.getTargetPath()).isEqualTo("/home/user/minecraft-new/config/mods");
    }

    @Test
    void testCopyPlan_activeToggle() {
        CopyPlan folder = new CopyPlan.CopyPlanFolder("test", "/test", true);

        assertThat(folder.isActive()).isTrue();

        folder.setActive(false);

        assertThat(folder.isActive()).isFalse();
    }
}

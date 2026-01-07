package de.husten1997.gui;

import de.husten1997.main.ApplicationContext;

import javax.swing.*;

public class GtnhUpdaterGuiComponent extends JPanel {
    private String frameName;
    private ApplicationContext applicationContext;

    public GtnhUpdaterGuiComponent(String frameName, ApplicationContext applicationContext) {
        super();

        this.frameName = frameName;
        this.applicationContext = applicationContext;
    }

    public String getFrameName() {
        return frameName;
    }

    private void setFrameName(String frameName) {
        this.frameName = frameName;
    }

    public ApplicationContext getApplicationConfig() {
        return applicationContext;
    }

    private void setApplicationConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}

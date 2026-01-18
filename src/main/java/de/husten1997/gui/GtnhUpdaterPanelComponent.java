package de.husten1997.gui;

import de.husten1997.main.ApplicationContext;

import javax.swing.*;

public abstract class GtnhUpdaterPanelComponent extends JPanel implements GtnhUpdaterLocalizedComponent {
    private ApplicationContext applicationContext;

    public GtnhUpdaterPanelComponent(ApplicationContext applicationContext) {
        super();

        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationConfig() {
        return applicationContext;
    }

    private void setApplicationConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}

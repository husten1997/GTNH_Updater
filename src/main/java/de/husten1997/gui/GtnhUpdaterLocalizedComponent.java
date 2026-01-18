package de.husten1997.gui;

import de.husten1997.main.I18nManager;

public interface GtnhUpdaterLocalizedComponent {
    I18nManager i18nManager = I18nManager.getInstance();

    void updateLabels();
}

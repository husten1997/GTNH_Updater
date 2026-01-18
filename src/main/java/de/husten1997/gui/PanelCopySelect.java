package de.husten1997.gui;

import de.husten1997.copyinstance.CopyPlan;
import de.husten1997.main.ApplicationContext;
import de.husten1997.main.Log;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class PanelCopySelect extends GtnhUpdaterPanelComponent {
    // Logger
    private final Logger LOGGER = Log.setupLogger( PanelCopySelect.class.getName() );

    // UI Components
    private final GtnhUpdaterComponentBorder titledBorder;

    // Data Components
    private final CopyPlan[] copyPlans;
    private final JCheckBox[] copySourceSelect;

    public PanelCopySelect(ApplicationContext applicationContext) {
        super(applicationContext);

        setLayout(new GridLayout(-1, 3));
        this.titledBorder = new GtnhUpdaterComponentBorder(i18nManager.get("app.copySelection.border.title"));
        setBorder(titledBorder);

        this.copyPlans = applicationContext.getCopyPlanBatch();
        this.copySourceSelect = new JCheckBox[copyPlans.length];

        for (int i = 0; i < copyPlans.length; i++) {
            this.copySourceSelect[i] = new JCheckBox(this.copyPlans[i].getDisplayName(), this.copyPlans[i].isActive());
            add(this.copySourceSelect[i]);
        }
    }

    @Override
    public void updateLabels() {
        // Border Title
        this.titledBorder.setTitle(i18nManager.get("app.copySelection.border.title"));
        this.setToolTipText(i18nManager.get("app.copySelection.tooltip.title"));
        repaint();

    }

    public CopyPlan[] getCopyPlans() {
        for (int i = 0; i < this.copySourceSelect.length; i++) {
            this.copyPlans[i].setActive(this.copySourceSelect[i].isSelected());
        }
        return this.copyPlans;
    }
}

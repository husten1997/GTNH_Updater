package de.husten1997.gui;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class GtnhUpdaterComponentBorder extends CompoundBorder{
    private final TitledBorder titledBorder;

    public GtnhUpdaterComponentBorder(String title) {
        super();

        this.titledBorder = new TitledBorder(title);
        this.titledBorder.setTitleColor(Color.GRAY);
        this.titledBorder.setTitleFont(new Font("SansSerif", Font.PLAIN, 14));
        this.outsideBorder = this.titledBorder;

        this.insideBorder = BorderFactory.createEmptyBorder(8, 8, 8, 8);
    }

    public void setTitle(String title) {
        this.titledBorder.setTitle(title);
    }
}

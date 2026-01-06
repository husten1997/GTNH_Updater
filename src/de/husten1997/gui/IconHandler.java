package de.husten1997.gui;

import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.swing.FontIcon;

public class IconHandler {
    public IconHandler() {
        
    }

    public static FontIcon fetchIcon(Ikon icon) {
        return fetchIcon(icon, 16);
    }

    public static FontIcon fetchIcon(Ikon icon, int size) {
        FontIcon fontIcon = FontIcon.of(icon);
        fontIcon.setIconSize(size);

        return fontIcon;
    }
}

package de.husten1997.gui;

import de.husten1997.main.ApplicationContext;
import de.husten1997.main.I18nManager;
import de.husten1997.main.Log;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Locale;
import java.util.logging.Logger;

public class PanelHeader extends GtnhUpdaterPanelComponent implements GtnhUpdaterLocalizedComponent {
    // Logger
    private final Logger LOGGER = Log.setupLogger( PanelGtnhFolder.class.getName() );

    // Ui Components
    private final JLabel header;
    private final JLabel introduction;
    private final JButton languageSelection;
    private final JButton bugReport;
    private final JButton documentation;
    private final JPopupMenu languagePopup;
    private final JRadioButtonMenuItem[] languageItem;

    public PanelHeader(ApplicationContext applicationContext) {
        super(applicationContext);

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 50, 10));
        GridBagConstraints gbc = new GridBagConstraints();

        this.header = new JLabel();
        this.header.setFont(new Font("SansSerif", Font.PLAIN, 20));
        this.introduction = new JLabel();

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1));
        this.languageSelection = new JButton(IconHandler.fetchIcon(BootstrapIcons.GLOBE2));
        this.languageSelection.addActionListener(this::onClickLanguage);
        this.documentation = new JButton(IconHandler.fetchIcon(BootstrapIcons.QUESTION));
        this.bugReport = new JButton(IconHandler.fetchIcon(BootstrapIcons.BUG));

        this.languagePopup = new JPopupMenu();
        ButtonGroup languageButtonGroup = new ButtonGroup();
        int numSupportedLanguages = I18nManager.SUPPORTED_LOCALES.length;
        this.languageItem = new JRadioButtonMenuItem[numSupportedLanguages];

        for (int i = 0; i < numSupportedLanguages; i++) {
            Locale locale = I18nManager.SUPPORTED_LOCALES[i];
            this.languageItem[i] = new JRadioButtonMenuItem(i18nManager.getDisplayName(locale));
            this.languageItem[i].addActionListener(e -> i18nManager.setLocale(locale));

            languageButtonGroup.add(this.languageItem[i]);
            languagePopup.add(this.languageItem[i]);
        }

        gbc.gridx = 0; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.9; gbc.weighty = 0;
        add(this.header, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.9; gbc.weighty = 0;
        add(this.introduction, gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.VERTICAL; gbc.weightx = 0; gbc.weighty = 1;

        buttonPanel.add(this.languageSelection);
        buttonPanel.add(this.documentation);
        buttonPanel.add(this.bugReport);
        add(buttonPanel, gbc);
    }

    private void onClickLanguage(ActionEvent e) {
        // Update Language Selection when opening the menu
        int numSupportedLanguages = I18nManager.SUPPORTED_LOCALES.length;
        for (int i = 0; i < numSupportedLanguages; i++) {
            Locale locale = I18nManager.SUPPORTED_LOCALES[i];
            JRadioButtonMenuItem item = this.languageItem[i];
            item.setSelected(locale.equals(i18nManager.getCurrentLocale()));
        }

        this.languagePopup.show(this.languageSelection, this.languageSelection.getX(), this.languageSelection.getY());
    }

    @Override
    public void updateLabels() {
        this.header.setText(i18nManager.get("app.header.label.title"));
        this.introduction.setText(i18nManager.get("app.header.label.introduction"));

        this.bugReport.setToolTipText(i18nManager.get("app.header.tooltip.bugReport"));
        this.documentation.setToolTipText(i18nManager.get("app.header.tooltip.documentation"));
        this.languageSelection.setToolTipText(i18nManager.get("app.header.tooltip.languageSelection"));
    }
}

package de.husten1997.main;

import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class I18nManager {
    private final Logger LOGGER = Log.setupLogger( I18nManager.class.getName() );

    private static I18nManager instance;

    private ResourceBundle bundle;
    private Locale currentLocale;
    private final List<Consumer<Locale>> localeChangeListeners = new ArrayList<>();

    // Available languages
    public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
    public static final Locale[] SUPPORTED_LOCALES = {
            Locale.ENGLISH,
            Locale.GERMAN,
//            Locale.FRENCH,
//            new Locale("es")  // Spanish
    };

    private I18nManager() {
        // Default to system locale, fall back to English
        Locale systemLocale = Locale.getDefault();
        if (isSupportedLocale(systemLocale)) {
            setLocale(systemLocale);
        } else {
            setLocale(DEFAULT_LOCALE);
        }
    }

    public static synchronized I18nManager getInstance() {
        if (instance == null) {
            instance = new I18nManager();
        }
        return instance;
    }

    public void setLocale(Locale locale) {
        LOGGER.fine("Change Locale to " + locale.getLanguage());
        this.currentLocale = new Locale(locale.getLanguage());
        this.bundle = ResourceBundle.getBundle("i18n.messages", locale);
        notifyListeners();
    }

    public Locale getCurrentLocale() {
        return currentLocale;
    }

    public String get(String key, boolean isHTML) {
        String returnValue;
        try {
            returnValue = bundle.getString(key);
        } catch (MissingResourceException e) {
            returnValue = "KEY NOT FOUND: " + key;  // Makes missing keys visible
        }

        if (isHTML) {
            returnValue = "<html>" + returnValue + "</html>";
        }

        return returnValue;
    }

    public String get(String key) {
        return this.get(key, true);
    }

    public String get(String key, Object... args) {
        return String.format(get(key), args);
    }

    public void addLocaleChangeListener(Consumer<Locale> listener) {
        localeChangeListeners.add(listener);
    }

    public void removeLocaleChangeListener(Consumer<Locale> listener) {
        localeChangeListeners.remove(listener);
    }

    private void notifyListeners() {
        for (Consumer<Locale> listener : localeChangeListeners) {
            listener.accept(currentLocale);
        }
    }

    private boolean isSupportedLocale(Locale locale) {
        for (Locale supported : SUPPORTED_LOCALES) {
            if (supported.getLanguage().equals(locale.getLanguage())) {
                return true;
            }
        }
        return false;
    }

    public String getDisplayName(Locale locale) {
        return locale.getDisplayLanguage(locale);  // Shows name in its own language
    }
}

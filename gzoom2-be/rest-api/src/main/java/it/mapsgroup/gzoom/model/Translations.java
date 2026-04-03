package it.mapsgroup.gzoom.model;

import java.util.Map;

/**
 * Translations of UI labels and messages.
 *
 * @author Fabio G. Strozzi
 */
public class Translations {
    private String language;
    private Map<String, String> translations;

    public Translations(String language, Map<String, String> translations) {
        this.language = language;
        this.translations = translations;
    }

    public String getLanguage() {
        return language;
    }

    public Map<String, String> getTranslations() {
        return translations;
    }
}

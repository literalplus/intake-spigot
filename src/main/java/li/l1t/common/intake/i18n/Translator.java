package li.l1t.common.intake.i18n;

import java.util.Locale;

/**
 * Translates message keys into messages with optional arguments.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-11
 */
public interface Translator {
    void setLocale(Locale locale);

    String translate(String key, Object... arguments);

    boolean hasTranslationFor(String key);
}

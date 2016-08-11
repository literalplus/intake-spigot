package li.l1t.common.intake.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Translates message keys into messages from a resource bundle.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-11
 */
public class ResourceBundleTranslator implements Translator {
    public static final String MESSAGE_BUNDLE_NAME = "intake-messages";
    private ResourceBundle messages;

    @Override
    public void setLocale(Locale locale) {
        setMessages(ResourceBundle.getBundle(MESSAGE_BUNDLE_NAME, locale));
    }

    public void setMessages(ResourceBundle messages) {
        this.messages = messages;
    }

    public ResourceBundle getMessages() {
        return messages;
    }

    @Override
    public String translate(String key, Object... arguments) {
        if (hasTranslationFor(key)) {
            return new MessageFormat(messages.getString(key), messages.getLocale())
                    .format(arguments, new StringBuffer(), null).toString();
        } else {
            return key;
        }
    }

    @Override
    public boolean hasTranslationFor(String key) {
        return key != null && messages.containsKey(key);
    }
}

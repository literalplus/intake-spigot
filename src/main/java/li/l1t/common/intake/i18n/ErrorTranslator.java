package li.l1t.common.intake.i18n;

import li.l1t.common.intake.CommandsManager;
import li.l1t.common.intake.i18n.translator.ExceptionTranslator;
import li.l1t.common.intake.i18n.translator.ExceptionTranslatorMap;
import li.l1t.common.intake.i18n.translator.impl.DefaultTranslators;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Translates English errors produced by Intake to another locale.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-24
 */
public class ErrorTranslator {
    public static final String MESSAGE_BUNDLE_NAME = "intake-messages";
    private final CommandsManager manager;
    private ResourceBundle messages;
    private ExceptionTranslatorMap translatorMap;

    public ErrorTranslator(CommandsManager manager) {
        this.manager = manager;
        this.translatorMap = new ExceptionTranslatorMap(this);
        this.messages = ResourceBundle.getBundle(MESSAGE_BUNDLE_NAME);
        DefaultTranslators.registerAllWith(this);
    }

    public String translateAndLogIfNecessary(Exception exception, String commandLine) {
        return translatorMap.translateAndLogIfNecessary(exception, commandLine);
    }

    public CommandsManager getManager() {
        return manager;
    }

    public <T extends Exception> ErrorTranslator withTranslator(Class<? extends T> exceptionType,
                                                                ExceptionTranslator<T> translator) {
        translatorMap.putTypesafe(exceptionType, translator);
        return this;
    }

    public void setLocale(Locale locale) {
        setMessages(ResourceBundle.getBundle(MESSAGE_BUNDLE_NAME, locale));
    }

    public void setMessages(ResourceBundle messages) {
        this.messages = messages;
    }

    public ResourceBundle getMessages() {
        return messages;
    }

    public String translate(String key, Object... arguments) {
        if (messages.containsKey(key)) {
            return new MessageFormat(messages.getString(key), messages.getLocale())
                    .format(arguments, new StringBuffer(), null).toString();
        } else {
            return key;
        }
    }
}

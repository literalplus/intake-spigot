package li.l1t.common.intake.i18n;

import li.l1t.common.intake.i18n.translator.ExceptionTranslator;
import li.l1t.common.intake.i18n.translator.ExceptionTranslatorMap;
import li.l1t.common.intake.i18n.translators.DefaultTranslators;

/**
 * Translates English errors produced by Intake to another locale.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-24
 */
public class ErrorTranslator {
    private ExceptionTranslatorMap translatorMap;
    private Translator translator;

    public ErrorTranslator(Translator translator) {
        this.translator = translator;
        this.translatorMap = new ExceptionTranslatorMap(this);
        DefaultTranslators.registerAllWith(this);
    }

    public String translateAndLogIfNecessary(Exception exception, String commandLine) {
        return translatorMap.translateAndLogIfNecessary(exception, commandLine);
    }

    public <T extends Exception> ErrorTranslator withTranslator(Class<? extends T> exceptionType,
                                                                ExceptionTranslator<T> translator) {
        translatorMap.putTypesafe(exceptionType, translator);
        return this;
    }

    public Translator getTranslator() {
        return translator;
    }
}

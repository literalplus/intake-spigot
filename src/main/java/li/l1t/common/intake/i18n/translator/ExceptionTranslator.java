package li.l1t.common.intake.i18n.translator;

import li.l1t.common.intake.i18n.ErrorTranslator;

/**
 * Translates exceptions into localized messages.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-24
 */
public interface ExceptionTranslator<E extends Exception> {
    String translate(E exception, ErrorTranslator root, String commandLine);

    Class<? extends E> getExceptionType();

    boolean needsLogging(E exception);
}

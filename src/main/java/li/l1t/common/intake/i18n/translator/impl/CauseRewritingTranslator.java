package li.l1t.common.intake.i18n.translator.impl;

import li.l1t.common.intake.i18n.ErrorTranslator;
import li.l1t.common.intake.i18n.translator.AbstractExceptionTranslator;

/**
 * Rewrites translation to the cause of an exception, causing the translation chain to be invoked
 * for the cause.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-25
 */
public class CauseRewritingTranslator<E extends Exception> extends AbstractExceptionTranslator<E> {
    public CauseRewritingTranslator(Class<? extends E> exceptionType) {
        super(exceptionType);
    }

    @Override
    public String translate(E exception, ErrorTranslator root, String commandLine) {
        Throwable cause = exception.getCause();
        if(cause instanceof Exception) {
            return root.translateAndLogIfNecessary((Exception) cause, commandLine);
        } else {
            return root.translate(
                    "Internal Error With Cause",
                    exception.getClass().getSimpleName(),
                    cause.getClass().getSimpleName()
            );
        }
    }

    @Override
    public boolean needsLogging(E exception) {
        return !(exception.getCause() instanceof Exception); //null is never instance of anything
    }
}

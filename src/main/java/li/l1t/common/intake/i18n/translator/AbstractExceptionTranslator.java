package li.l1t.common.intake.i18n.translator;

import li.l1t.common.intake.i18n.ErrorTranslator;

/**
 * Abstract base class for exception translators.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-24
 */
public abstract class AbstractExceptionTranslator<E extends Exception> implements ExceptionTranslator<E> {
    private final Class<? extends E> exceptionType;
    private final boolean needsLogging;

    public AbstractExceptionTranslator(Class<? extends E> exceptionType) {
        this(exceptionType, false);
    }

    public AbstractExceptionTranslator(Class<? extends E> exceptionType, boolean needsLogging) {
        this.exceptionType = exceptionType;
        this.needsLogging = needsLogging;
    }

    @Override
    public String translate(E exception, ErrorTranslator root, String commandLine) {
        return root.translate("InternalError:withMessage", exception.getClass().getSimpleName());
    }

    @Override
    public Class<? extends E> getExceptionType() {
        return exceptionType;
    }

    @Override
    public boolean needsLogging(E exception) {
        return needsLogging;
    }

    public void registerWith(ErrorTranslator root) {
        root.withTranslator(getExceptionType(), this);
    }
}

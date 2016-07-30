package li.l1t.common.intake.i18n.translator;

import li.l1t.common.intake.i18n.ErrorTranslator;
import li.l1t.common.intake.i18n.translator.generic.FallbackTranslator;

import java.util.HashMap;
import java.util.logging.Level;

/**
 * Maps classes to their exception translators.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-24
 */
public class ExceptionTranslatorMap
        extends HashMap<Class<? extends Exception>, ExceptionTranslator<? extends Exception>> {
    private final ErrorTranslator root;
    private FallbackTranslator fallbackTranslator;

    public ExceptionTranslatorMap(ErrorTranslator root) {
        this.root = root;
        putFallbackTranslator();
    }

    private void putFallbackTranslator() {
        fallbackTranslator = new FallbackTranslator();
        putTypesafe(Exception.class, fallbackTranslator);
    }

    @SuppressWarnings("unchecked")
    public String translateAndLogIfNecessary(Exception exception, String commandLine) {
        ExceptionTranslator translator = ((ExceptionTranslator) getTranslator(exception.getClass()));
        if(translator.needsLogging(exception)) {
            root.getManager().getPlugin().getLogger().log(
                    Level.WARNING,
                    root.translate("Misc.ExceptionLogMessage", commandLine),
                    exception
            );
        }
        return translator.translate(exception, root, commandLine);
    }

    @SuppressWarnings("unchecked")
    public <T extends Exception> ExceptionTranslator<T> getTypesafe(Class<T> key) {
        return (ExceptionTranslator<T>) super.get(key);
    }

    public <T extends Exception> ExceptionTranslator<? super T> getTranslator(Class<T> exceptionType) {
        Class<?> currentType = exceptionType;
        while (true) {
            @SuppressWarnings("unchecked")
            ExceptionTranslator<? super T> translator = (ExceptionTranslator<? super T>) get(currentType);
            if (translator != null) {
                return translator;
            }
            currentType = currentType.getSuperclass();
            if (currentType == null) {
                return fallbackTranslator; //this should not happen at all, but
                // let's prevent infinite loops like a responsible adults
            }
        }
    }

    @Override
    public ExceptionTranslator<? extends Exception> put(
            Class<? extends Exception> key, ExceptionTranslator<? extends Exception> value
    ) {
        if (!key.isAssignableFrom(value.getExceptionType())) {
            throw new UnsupportedOperationException(String.format(
                    "key must be assignable from value type: %s, %s",
                    key.getName(), value.getExceptionType()
            ));
        }
        return super.put(key, value);
    }

    public <T extends Exception> ExceptionTranslator<? extends Exception> putTypesafe(
            Class<? extends T> key, ExceptionTranslator<T> value
    ) {
        return put(key, value);
    }

    public ErrorTranslator getRoot() {
        return root;
    }
}

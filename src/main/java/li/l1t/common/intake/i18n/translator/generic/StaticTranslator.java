package li.l1t.common.intake.i18n.translator.generic;

import li.l1t.common.intake.i18n.ErrorTranslator;
import li.l1t.common.intake.i18n.translator.AbstractExceptionTranslator;

import java.util.function.Function;

/**
 * Translates exceptions to a static message defined in the message bundle.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-24
 */
public class StaticTranslator<E extends Exception> extends AbstractExceptionTranslator<E> {
    private static Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    private final String messageKey;
    private Function<? super E, Object[]> argumentFunction;

    public StaticTranslator(Class<? extends E> clazz, String messageKey, boolean needsLogging) {
        super(clazz, needsLogging);
        this.messageKey = messageKey;
        this.argumentFunction = this::emptyArguments;
    }

    @Override
    public String translate(E exception, ErrorTranslator root, String commandLine) {
        return root.translate(messageKey, argumentFunction.apply(exception));
    }

    public StaticTranslator<E> withArgumentFunction(Function<? super E, Object[]> argumentFunction) {
        this.argumentFunction = argumentFunction;
        return this;
    }

    public StaticTranslator<E> withArgument(Function<? super E, Object> function) {
        this.argumentFunction = e -> new Object[]{function.apply(e)};
        return this;
    }

    public StaticTranslator<E> withTwoArguments(Function<? super E, Object> function1,
                                                      Function<? super E, Object> function2) {

        this.argumentFunction = e -> new Object[]{function1.apply(e), function2.apply(e)};
        return this;
    }

    private Object[] emptyArguments(E exception) {
        return EMPTY_OBJECT_ARRAY;
    }
}

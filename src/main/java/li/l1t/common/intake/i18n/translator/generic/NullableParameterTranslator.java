package li.l1t.common.intake.i18n.translator.generic;

import com.sk89q.intake.Parameter;
import li.l1t.common.intake.i18n.ErrorTranslator;
import li.l1t.common.intake.i18n.translator.AbstractExceptionTranslator;

import java.util.function.Function;

/**
 * Translates exceptions. Those may have a parameter specified, but need not.
 * <p>
 * Exceptions with parameters are translated to {@code baseKey}.forParameter and arguments
 * original message, parameter name.
 * </p>
 * <p>
 * Exceptions without parameters are translated to {@code baseKey}.unspecified and argument
 * original message.
 * </p>
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-30
 */
public class NullableParameterTranslator<E extends Exception> extends AbstractExceptionTranslator<E> {
    private String baseKey;
    private Function<E, Parameter> parameterMapper = anyParam -> null;
    private PatternBasedMessageTranslator<? super E> messageTranslator;

    public NullableParameterTranslator(Class<? extends E> exceptionType) {
        super(exceptionType, false);
    }

    public NullableParameterTranslator<E> withBaseKey(String baseKey) {
        this.baseKey = baseKey;
        return this;
    }

    public NullableParameterTranslator<E> withParameterMapper(Function<E, Parameter> mapper) {
        this.parameterMapper = mapper;
        return this;
    }

    public NullableParameterTranslator<E> withMessageTranslator(
            PatternBasedMessageTranslator<? super E> translator
    ) {
        this.messageTranslator = translator.withBaseKey(baseKey);
        return this;
    }

    @Override
    public String translate(E exception, ErrorTranslator root, String commandLine) {
        Parameter parameter = parameterMapper.apply(exception);
        if (hasParameterSet(parameter)) {
            return translateWithParameter(root, exception, parameter);
        } else {
            return translateWithoutParameter(root, exception);
        }
    }

    private String translateWithoutParameter(ErrorTranslator root, E exception) {
        return root.getTranslator().translate(
                baseKey + ":unspecified",
                translateMessage(root, exception)
        );
    }

    private String translateWithParameter(ErrorTranslator root, E exception, Parameter parameter) {
        return root.getTranslator().translate(
                baseKey + ":forParameter",
                translateMessage(root, exception),
                parameter.getName()
        );
    }

    private String translateMessage(ErrorTranslator root, E exception) {
        if(messageTranslator == null) {
            return root.getTranslator().translate(baseKey + ":other", exception.getMessage());
        } else {
            return messageTranslator.translateMessage(root, exception.getMessage());
        }
    }

    private boolean hasParameterSet(Parameter parameter) {
        return parameter != null;
    }
}

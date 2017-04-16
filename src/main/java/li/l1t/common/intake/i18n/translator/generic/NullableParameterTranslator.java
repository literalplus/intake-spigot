/*
 * Intake-Spigot, a Spigot bridge for the Intake command framework.
 * Copyright (C) Philipp Nowak (Literallie)
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package li.l1t.common.intake.i18n.translator.generic;

import com.sk89q.intake.Parameter;
import li.l1t.common.i18n.Message;
import li.l1t.common.intake.i18n.translator.AbstractExceptionTranslator;

import java.util.function.Function;

/**
 * Translates exceptions. Those may have a parameter specified, but need not.
 * <p>
 * Exceptions with parameters are translated to {@code baseKey}:forParameter and arguments
 * original message, parameter name.
 * </p>
 * <p>
 * Exceptions without parameters are translated to {@code baseKey}:unspecified and argument
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
    public Message translate(E exception, String commandLine) {
        Parameter parameter = parameterMapper.apply(exception);
        if (hasParameterSet(parameter)) {
            return translateWithParameter(exception, parameter);
        } else {
            return translateWithoutParameter(exception);
        }
    }

    private Message translateWithoutParameter(E exception) {
        return Message.of(
                baseKey + ":unspecified",
                translateMessage(exception)
        );
    }

    private Message translateWithParameter(E exception, Parameter parameter) {
        return Message.of(
                baseKey + ":forParameter",
                translateMessage(exception),
                parameter.getName()
        );
    }

    private Message translateMessage(E exception) {
        if(messageTranslator == null) {
            return Message.of(baseKey + ":other", exception.getMessage());
        } else {
            return messageTranslator.translateMessage(exception.getMessage());
        }
    }

    private boolean hasParameterSet(Parameter parameter) {
        return parameter != null;
    }
}

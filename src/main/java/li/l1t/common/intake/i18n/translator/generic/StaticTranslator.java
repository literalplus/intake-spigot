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
        return root.getTranslator().translate(messageKey, argumentFunction.apply(exception));
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

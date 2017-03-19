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

package li.l1t.common.intake.i18n.translator;

import com.google.common.base.Preconditions;
import li.l1t.common.intake.i18n.ErrorTranslator;
import li.l1t.common.intake.i18n.Message;

/**
 * Abstract base class for exception translators.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-24
 */
public abstract class AbstractExceptionTranslator<E extends Exception> implements ExceptionTranslator<E> {
    private final Class<? extends E> exceptionType;
    private final boolean needsLogging;

    public AbstractExceptionTranslator(Class<? extends E> exceptionType, boolean needsLogging) {
        this.exceptionType = exceptionType;
        this.needsLogging = needsLogging;
    }

    @Override
    public Message translate(E exception, String commandLine) {
        return Message.of("InternalError:withMessage", exception.getClass().getSimpleName());
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
        Preconditions.checkNotNull(root, "root");
        root.withTranslator(getExceptionType(), this);
    }
}

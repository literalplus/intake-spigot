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
import li.l1t.common.i18n.Message;
import li.l1t.common.intake.i18n.translator.AbstractExceptionTranslator;

/**
 * Rewrites translation to the cause of an exception, causing the translation chain to be invoked
 * for the cause.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-25
 */
public class CauseRewritingTranslator<E extends Exception> extends AbstractExceptionTranslator<E> {
    private final ErrorTranslator root;

    public CauseRewritingTranslator(Class<? extends E> exceptionType, ErrorTranslator root) {
        super(exceptionType, false);
        this.root = root;
    }

    @Override
    public Message translate(E exception, String commandLine) {
        Throwable cause = exception.getCause();
        if(cause instanceof Exception) {
            return root.translateAndLogIfNecessary((Exception) cause, commandLine);
        } else {
            return Message.of(
                    "InternalError:withMessageAndCause",
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

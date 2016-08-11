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

/**
 * Translates exceptions, using its localized message as parameter. If the message is null, the
 * cause's class name and message are used, and if that is null, the exception class name is used.
 * <p>
 * If cause and message are both null, the exception is also logged to allow further
 * diagnostics. Without any message, it would be impossible to say what kind of error the
 * user encountered. If the message is non-null, and the exception has a cause, the full stack
 * trace is logged for the same reason.
 * </p>
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-06
 */
public class MessageAwareTranslator<E extends Exception> extends StaticTranslator<E> {
    public MessageAwareTranslator(String messageKey, Class<? extends E> exceptionType) {
        super(exceptionType, messageKey, false);
        initArgumentProvider();
    }

    private void initArgumentProvider() {
        withArgument(e -> {
            if (e.getLocalizedMessage() != null) {
                return e.getLocalizedMessage();
            } else if (e.getCause() != null) {
                return e.getCause().getClass().getName() + ": " + e.getCause().getLocalizedMessage();
            } else {
                return e.getClass().getName();
            }
        });
    }

    @Override
    public boolean needsLogging(E exception) {
        return (exception.getLocalizedMessage() == null) && (exception.getCause() == null) ||
                (exception.getCause() != null);
    }
}

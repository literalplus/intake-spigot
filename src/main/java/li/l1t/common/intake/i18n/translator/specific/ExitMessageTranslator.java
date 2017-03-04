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

package li.l1t.common.intake.i18n.translator.specific;

import li.l1t.common.intake.exception.CommandExitMessage;
import li.l1t.common.intake.i18n.Message;
import li.l1t.common.intake.i18n.translator.AbstractExceptionTranslator;

/**
 * Translates exit messages.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-04
 */
public class ExitMessageTranslator extends AbstractExceptionTranslator<CommandExitMessage> {
    public ExitMessageTranslator() {
        super(CommandExitMessage.class, false);
    }

    @Override
    public Message translate(CommandExitMessage exception, String commandLine) {
        return exception.getIntakeMessage();
    }
}

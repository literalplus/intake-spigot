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

import com.sk89q.intake.InvalidUsageException;
import li.l1t.common.intake.i18n.ErrorTranslator;
import li.l1t.common.intake.i18n.Message;
import li.l1t.common.intake.i18n.translator.AbstractExceptionTranslator;
import li.l1t.common.intake.i18n.translator.generic.CauseRewritingTranslator;
import li.l1t.common.intake.i18n.translator.generic.PatternBasedMessageTranslator;

/**
 * Handles the special case of InvalidUsageException. If the exception has a cause, it is
 * rewritten to that, mimicking {@link CauseRewritingTranslator} behaviour. If it doesn't, it is
 * mapped to its message like in {@link PatternBasedMessageTranslator}.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-30
 */
public class InvalidUsageTranslator extends AbstractExceptionTranslator<InvalidUsageException> {
    private final PatternBasedMessageTranslator<InvalidUsageException> messageTranslator;
    private final CauseRewritingTranslator<InvalidUsageException> causeRewritingTranslator;

    public InvalidUsageTranslator(ErrorTranslator root) {
        super(InvalidUsageException.class, false);
        causeRewritingTranslator = new CauseRewritingTranslator<>(InvalidUsageException.class, root);
        messageTranslator = new PatternBasedMessageTranslator<>(InvalidUsageException.class, false)
        .withBaseKey("InvalidUsage");
    }

    @Override
    public Message translate(InvalidUsageException exception, String commandLine) {
        if(exception.getCause() != null) {
            return rewriteToCause(exception, commandLine);
        } else {
            return mapToMessage(exception, commandLine);
        }
    }

    private Message mapToMessage(InvalidUsageException exception, String commandLine) {
        return messageTranslator.translate(exception, commandLine);
    }

    private Message rewriteToCause(InvalidUsageException exception, String commandLine) {
        return causeRewritingTranslator.translate(exception, commandLine);
    }
}

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

import li.l1t.common.intake.i18n.Message;
import li.l1t.common.intake.i18n.translator.AbstractExceptionTranslator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Translates exception messages based on a map of patterns to corresponding message keys.
 * <p>
 * Keys are read from the message bundle at {@code baseKey}{@code KEY} (no separator included),
 * or {@code baseKey}:{@code message} if no pattern matches.
 * </p>
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-30
 */
public class PatternBasedMessageTranslator<E extends Exception> extends AbstractExceptionTranslator<E> {
    private Map<Pattern, String> messagePatterns;
    private String baseKey;

    public PatternBasedMessageTranslator(Class<? extends E> exceptionType, boolean needsLogging) {
        super(exceptionType, needsLogging);
    }

    public PatternBasedMessageTranslator<E> withMessagePatterns(Map<Pattern, String> patterns) {
        this.messagePatterns = patterns;
        return this;
    }

    public PatternBasedMessageTranslator<E> withPattern(Pattern pattern, String keyExtension) {
        getMessagePatterns().put(pattern, keyExtension);
        return this;
    }

    public PatternBasedMessageTranslator<E> withBaseKey(String baseKey) {
        this.baseKey = baseKey;
        return this;
    }

    @Override
    public Message translate(E exception, String commandLine) {
        return translateMessage(exception.getMessage());
    }

    public Message translateMessage(String message) {
        for (Map.Entry<Pattern, String> entry : getMessagePatterns().entrySet()) {
            Matcher matcher = entry.getKey().matcher(message);
            if (matcher.matches()) {
                return Message.of(baseKey, findGroupValues(matcher));
            }
        }
        return Message.of(baseKey + ":" + message)
                .orElse(Message.of(baseKey + ":other", message));
    }

    private Object[] findGroupValues(Matcher matcher) {
        Object[] params = new Object[matcher.groupCount()];
        for (int i = 1; i <= matcher.groupCount(); i++) {
            params[i - 1] = matcher.group(i);
        }
        return params;
    }

    public Map<Pattern, String> getMessagePatterns() {
        if (messagePatterns == null) {
            messagePatterns = new HashMap<>();
        }
        return messagePatterns;
    }
}

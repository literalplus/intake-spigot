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

package li.l1t.common.intake.i18n;

import java.util.Arrays;

/**
 * Represents a message that may be translatable or static.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-04
 */
public class Message {
    private final String staticText;
    private final String key;
    private final Object[] arguments;
    private Message fallback;

    private Message(String staticText, String key, Object... arguments) {
        this.key = key;
        this.arguments = arguments;
        this.staticText = staticText;
    }

    /**
     * @return whether this message is static and does not need translation
     *
     * @see #toString() for the static text if the message is static
     */
    public boolean isStatic() {
        return staticText != null;
    }

    /**
     * @return the translation key of this message, or null if it is static
     */
    public String getKey() {
        return key;
    }

    /**
     * @return the arguments array of this message, or always an empty array if it is static
     */
    public Object[] getArguments() {
        return arguments;
    }

    /**
     * @param message the new {@link #getFallback() fallback message}
     *
     * @return this message
     */
    public Message orElse(Message message) {
        this.fallback = message;
        return this;
    }

    /**
     * @return whether this message has a {@link #getFallback() fallback message}
     */
    public boolean hasFallback() {
        return fallback != null;
    }

    /**
     * @return the fallback message that is to be used if translation of this message fails
     */
    public Message getFallback() {
        return fallback;
    }

    @Override
    public String toString() {
        return isStatic() ? staticText : (key + Arrays.toString(arguments));
    }

    /**
     * Creates a new dynamic message.
     *
     * @param key       the translation key
     * @param arguments the arguments to pass to the translator
     *
     * @return the created message
     */
    public static Message of(String key, Object... arguments) {
        return new Message(null, key, arguments);
    }

    /**
     * Creates a new static message.
     *
     * @param staticText the static message text
     *
     * @return the created message
     */
    public static Message ofText(String staticText) {
        return new Message(staticText, null);
    }
}

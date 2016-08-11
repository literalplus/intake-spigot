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

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Translates message keys into messages from a resource bundle.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-11
 */
public class ResourceBundleTranslator implements Translator {
    public static final String MESSAGE_BUNDLE_NAME = "intake-messages";
    private ResourceBundle messages;

    @Override
    public void setLocale(Locale locale) {
        setMessages(ResourceBundle.getBundle(MESSAGE_BUNDLE_NAME, locale));
    }

    public void setMessages(ResourceBundle messages) {
        this.messages = messages;
    }

    public ResourceBundle getMessages() {
        return messages;
    }

    @Override
    public String translate(String key, Object... arguments) {
        if (hasTranslationFor(key)) {
            return new MessageFormat(messages.getString(key), messages.getLocale())
                    .format(arguments, new StringBuffer(), null).toString();
        } else {
            return key;
        }
    }

    @Override
    public boolean hasTranslationFor(String key) {
        return key != null && messages.containsKey(key);
    }
}

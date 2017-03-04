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

import com.google.common.base.Preconditions;
import org.bukkit.command.CommandSender;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;

/**
 * Translates message keys into messages from a resource bundle.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-11
 */
public class ResourceBundleTranslator implements Translator {
    public static final String MESSAGE_BUNDLE_NAME = "intake-messages";
    private Map<Locale, ResourceBundle> bundles = new HashMap<>();
    private LocaleSelectionProvider selectionProvider = any -> Locale.getDefault();

    @Override
    public void setLocale(Locale locale) {
        setSelectionProvider(any -> locale);
    }

    @Override
    public void setSelectionProvider(LocaleSelectionProvider selectionProvider) {
        Preconditions.checkNotNull(selectionProvider, "selectionProvider");
        this.selectionProvider = selectionProvider;
    }

    @Override
    public LocaleSelectionProvider getSelectionProvider() {
        return selectionProvider;
    }

    @Override
    public String translate(CommandSender sender, Message message) {
        Locale locale = selectionProvider.findLocale(sender);
        if (message.isStatic() || noTranslationPossible(locale, message)) {
            return message.toString();
        } else {
            return new MessageFormat(findMessage(locale, message.getKey()), locale)
                    .format(message.getArguments(), new StringBuffer(), null).toString();
        }
    }

    private String findMessage(Locale locale, String key) {
        return findBundle(locale).getString(key);
    }

    private ResourceBundle findBundle(Locale locale) {
        return bundles.computeIfAbsent(locale, this::createBundle);
    }

    private ResourceBundle createBundle(Locale locale) {
        return ResourceBundle.getBundle(MESSAGE_BUNDLE_NAME, locale);
    }

    @Override
    public Function<Message, String> translationFunctionFor(CommandSender sender) {
        return message -> translate(sender, message);
    }

    private boolean noTranslationPossible(Locale locale, Message message) {
        return !hasTranslationFor(locale, message.getKey());
    }

    public boolean hasTranslationFor(Locale locale, String key) {
        return key != null && findBundle(locale).containsKey(key);
    }
}

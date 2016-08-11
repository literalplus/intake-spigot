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

package li.l1t.common.intake.provider;

import com.google.common.collect.ImmutableList;
import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.ArgumentParseException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.Provider;
import com.sk89q.intake.parametric.ProvisionException;
import com.sk89q.intake.parametric.annotation.Validate;
import li.l1t.common.intake.provider.annotation.Colored;
import li.l1t.common.intake.provider.annotation.Merged;
import net.md_5.bungee.api.ChatColor;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.List;


/**
 * Provides string arguments that may contain spaces. Must occur as last argument.
 * <p>
 * This required the {@link Merged} annotation by default.
 * </p>
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-06
 */
public class MergedTextProvider implements Provider<String> {
    @Override
    public boolean isProvided() {
        return false;
    }

    @Nullable
    @Override
    public String get(CommandArgs args, List<? extends Annotation> modifiers) throws ArgumentException, ProvisionException {
        StringBuilder textBuilder = new StringBuilder(args.next());
        while (args.hasNext()) {
            textBuilder.append(" ").append(args.next());
        }
        String text = textBuilder.toString();
        return processText(text, modifiers);
    }

    private String processText(String text, List<? extends Annotation> modifiers) throws ArgumentParseException, ProvisionException {
        validateRegExIfPresent(text, modifiers);
        text = processColoredFor(text, modifiers);
        return text;
    }

    private String processColoredFor(String text, List<? extends Annotation> modifiers) throws ProvisionException {
        Colored annotation = getAnnotationFrom(modifiers, Colored.class);
        if (annotation != null) {
            text = ChatColor.translateAlternateColorCodes('&', text);
        }
        return text;
    }

    //Inspired by from Intake StringProvider - https://github.com/sk89q/Intake is licensed under the LGPL (See /LICENSE.txt)
    protected void validateRegExIfPresent(String text, List<? extends Annotation> modifiers) throws ArgumentParseException, ProvisionException {
        Validate validate = getAnnotationFrom(modifiers, Validate.class);
        if (validate != null && !validate.regex().isEmpty() && !text.matches(validate.regex())) {
            throw new ArgumentParseException(
                    String.format(
                            "The given text doesn't match the right format (technically speaking, the 'format' is %s)",
                            validate.regex()));
        }
    }
    //End Inspired by

    @SuppressWarnings("unchecked")
    private <A extends Annotation> A getAnnotationFrom(List<? extends Annotation> modifiers,
                                                       Class<? extends A> clazz) throws ProvisionException {
        return modifiers.stream()
                .filter(mod -> clazz.isAssignableFrom(mod.getClass()))
                .map(mod -> (A) mod)
                .findFirst().orElse(null);
    }

    @Override
    public List<String> getSuggestions(String prefix) {
        return ImmutableList.of();
    }
}

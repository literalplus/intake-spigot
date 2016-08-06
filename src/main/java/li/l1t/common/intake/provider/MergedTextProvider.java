package li.l1t.common.intake.provider;

import com.google.common.collect.ImmutableList;
import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.ArgumentParseException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.Provider;
import com.sk89q.intake.parametric.ProvisionException;
import com.sk89q.intake.parametric.annotation.Validate;
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
        text = processMergedAnnotationFeaturesFor(text, modifiers);
        return text;
    }

    private String processMergedAnnotationFeaturesFor(String text, List<? extends Annotation> modifiers) throws ProvisionException {
        Merged annotation = getMergedAnnotationFrom(modifiers);
        if (annotation.translateColors()) {
            text = ChatColor.translateAlternateColorCodes('&', text);
        }
        return text;
    }

    private Merged getMergedAnnotationFrom(List<? extends Annotation> modifiers) throws ProvisionException {
        return modifiers.stream()
                .filter(mod -> mod instanceof Merged)
                .map(mod -> (Merged) mod)
                .findFirst()
                .orElseThrow(() -> new ProvisionException("Missing @Merged"));
    }

    //Adapted from Intake StringProvider - https://github.com/sk89q/Intake is licensed under the LGPL (See /LICENSE.txt)
    protected static void validateRegExIfPresent(String text, List<? extends Annotation> modifiers) throws ArgumentParseException {
        for (Annotation modifier : modifiers) {
            if (modifier instanceof Validate) {
                Validate validate = (Validate) modifier;
                if (!validate.regex().isEmpty() && !text.matches(validate.regex())) {
                    throw new ArgumentParseException(
                            String.format(
                                    "The given text doesn't match the right format (technically speaking, the 'format' is %s)",
                                    validate.regex()));
                }
            }
        }
    }
    //End Adapted from

    @Override
    public List<String> getSuggestions(String prefix) {
        return ImmutableList.of();
    }
}

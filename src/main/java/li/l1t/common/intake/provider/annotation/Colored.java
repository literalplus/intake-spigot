package li.l1t.common.intake.provider.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a String parameter to be translated using
 * {@link net.md_5.bungee.api.ChatColor#translateAlternateColorCodes(char, String)}. Currently only
 * processed by {@link li.l1t.common.intake.provider.MergedTextProvider}.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-24
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Colored {
}

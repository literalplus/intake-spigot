package li.l1t.common.intake.provider.annotation;

import com.sk89q.intake.parametric.annotation.Classifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a String parameter to consume all remaining arguments into a single string.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-24
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Classifier
public @interface Merged {
    /**
     * @return whether to translate Minecraft colors by passing the result through
     * {@link net.md_5.bungee.api.ChatColor#translateAlternateColorCodes(char, String)}
     */
    boolean translateColors() default false;
}

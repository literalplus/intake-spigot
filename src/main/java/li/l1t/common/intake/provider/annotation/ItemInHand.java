package li.l1t.common.intake.provider.annotation;

import com.sk89q.intake.parametric.annotation.Classifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Classifies that annotated ItemStack should be the item in the hand of the calling player.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Classifier
public @interface ItemInHand {
}

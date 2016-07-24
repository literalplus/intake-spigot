package li.l1t.common.intake.i18n.translator.impl;

import li.l1t.common.intake.i18n.translator.AbstractExceptionTranslator;

/**
 * Translates exceptions into their localized messages.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-24
 */
public class FallbackTranslator extends AbstractExceptionTranslator<Exception> {
    public FallbackTranslator() {
        super(Exception.class);
    }
}

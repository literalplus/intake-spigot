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

import li.l1t.common.intake.i18n.translator.ExceptionTranslator;
import li.l1t.common.intake.i18n.translator.ExceptionTranslatorMap;
import li.l1t.common.intake.i18n.translators.DefaultTranslators;

/**
 * Translates English errors produced by Intake to another locale.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-24
 */
public class ErrorTranslator {
    private ExceptionTranslatorMap translatorMap;
    private Translator translator;

    public ErrorTranslator(Translator translator) {
        this.translator = translator;
        this.translatorMap = new ExceptionTranslatorMap(this);
        DefaultTranslators.registerAllWith(this);
    }

    public li.l1t.common.i18n.Message translateAndLogIfNecessary(Exception exception, String commandLine) {
        return translatorMap.translateAndLogIfNecessary(exception, commandLine);
    }

    public <T extends Exception> ErrorTranslator withTranslator(Class<? extends T> exceptionType,
                                                                ExceptionTranslator<T> translator) {
        translatorMap.putTypesafe(exceptionType, translator);
        return this;
    }

    public Translator getTranslator() {
        return translator;
    }

    public void setTranslator(Translator translator) {
        this.translator = translator;
    }
}

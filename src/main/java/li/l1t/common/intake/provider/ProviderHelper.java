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

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;

/**
 * Provides static utility methods to make writing providers easier.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2018-01-15
 */
public final class ProviderHelper {
    private ProviderHelper() {

    }

    /**
     * Finds an annotation from a list that is an instance of the provided class.
     *
     * @param <A>       the type of the annotation to find
     * @param modifiers the list to search in
     * @param clazz     the class to search for (subclasses included)
     * @return an optional containing the first annotation that is an instance of given class, or an empty optional if
     * there is no such annotation.
     */
    @SuppressWarnings("unchecked")
    public static <A extends Annotation> Optional<A> findAnnotationIn(List<? extends Annotation> modifiers,
                                                                      Class<? extends A> clazz) {
        return modifiers.stream()
                .filter(mod -> clazz.isAssignableFrom(mod.getClass()))
                .map(mod -> (A) mod)
                .findFirst();
    }
}

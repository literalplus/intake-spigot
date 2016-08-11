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

package li.l1t.common.intake.lib;

import com.google.common.base.Charsets;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Helps dealing with UUIDs.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 4.8.14
 */
public class UUIDHelper {
    /**
     * A Pattern that matches valid Java UUIDs.
     */
    public static final Pattern UUID_PATTERN = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}");

    /**
     * Performs a match using {@link #UUID_PATTERN} to check whether {@code input} is a valid UUID as accepted by the
     * Java {@link UUID} impl.
     *
     * @param input Input string to check
     * @return Whether the input string is a valid Java UUID.
     */
    public static boolean isValidUUID(String input) {
        return UUID_PATTERN.matcher(input).matches();
    }

    /**
     * Creates an "offline" UUID as Minecraft would use for "cracked" players.
     *
     * @param offlineName The offline player's name, case-sensitive.
     * @return the offline UUID for given name.
     */
    public static UUID getOfflineUUID(String offlineName) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + offlineName).getBytes(Charsets.UTF_8));
    }

    /**
     * Gets an UUID from a String. The input may or may not contain the dashes used by Java, since Mojang's API returns
     * UUIDs without dashes at some points.
     *
     * @param input the input to convert
     * @return an UUID corresponding to the input or NULL if the input is invalid
     */
    public static UUID getFromString(String input) {
        if (input == null) {
            return null;
        } else if (isValidUUID(input)) {
            return UUID.fromString(input);
        } else {
            if (input.length() == 32) {
                String s1 = input.substring(0, 8) + "-" + input.substring(8, 12) + "-" + input.substring(12, 16) + "-" + input.substring(16, 20) + "-" + input.substring(20, 32);

                if (isValidUUID(s1)) {
                    return UUID.fromString(s1);
                }
            }

            return null;
        }
    }
}

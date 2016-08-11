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

package li.l1t.common.intake;

import com.google.common.base.Preconditions;
import com.sk89q.intake.argument.Namespace;
import com.sk89q.intake.util.auth.Authorizer;
import org.bukkit.command.CommandSender;

/**
 * Authorizes command execution based on whether the command sender in the namespace has a
 * permission.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-23
 */
public class CommandSenderAuthorizer implements Authorizer {
    @Override
    public boolean testPermission(Namespace namespace, String permission) {
        CommandSender sender = namespace.get(CommandSender.class);
        Preconditions.checkNotNull(sender, "No command sender available for authorization");
        return sender.hasPermission(permission);
    }
}

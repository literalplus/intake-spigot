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

import li.l1t.common.intake.CommandsManager;
import li.l1t.common.intake.lib.UUIDHelper;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-05-04
 */
public abstract class OnlineProvider<T extends CommandSender> extends NamespaceAwareProvider<T> {
    protected final Server server;

    protected OnlineProvider(CommandsManager commandsManager) {
        this.server = commandsManager.getPlugin().getServer();
    }

    @Override
    public boolean isProvided() {
        return false;
    }

    protected Player getPlayerFromUUIDOrNull(String input) {
        if (UUIDHelper.isValidUUID(input)) {
            UUID uuid = UUIDHelper.getFromString(input);
            Player player = server.getPlayer(uuid);
            throwLocalizedIf(player == null, "NoPlayerOnline:uuid", input);
            return player;
        }
        return null;
    }
}

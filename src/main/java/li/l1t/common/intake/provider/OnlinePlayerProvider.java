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

import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.ProvisionException;
import li.l1t.common.intake.CommandsManager;
import li.l1t.common.intake.lib.UUIDHelper;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * Provides the executing command sender instance to commands.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-24
 */
public class OnlinePlayerProvider extends NamespaceAwareProvider<Player> {
    private final Server server;

    public OnlinePlayerProvider(CommandsManager manager) {
        super();
        this.server = manager.getPlugin().getServer();
    }

    @Override
    public boolean isProvided() {
        return false;
    }

    @Nullable
    @Override
    public Player get(CommandArgs arguments, List<? extends Annotation> modifiers) throws
            ArgumentException, ProvisionException {
        String input = arguments.next();
        Player player = getPlayerFromUUIDOrNull(input);
        if (player == null) {
            player = server.getPlayerExact(input);
            throwLocalizedIf(player == null, "NoPlayerOnline:name", input);
        }
        return player;
    }

    private Player getPlayerFromUUIDOrNull(String input) {
        if (UUIDHelper.isValidUUID(input)) {
            UUID uuid = UUIDHelper.getFromString(input);
            Player player = server.getPlayer(uuid);
            throwLocalizedIf(player == null, "NoPlayerOnline:uuid", input);
            return player;
        }
        return null;
    }

    @Override
    public List<String> getSuggestions(String prefix) {
        return server.getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(name -> name.startsWith(prefix))
                .collect(Collectors.toList());
    }
}

/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.intake.provider;

import com.google.common.collect.ImmutableList;
import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.Provider;
import com.sk89q.intake.parametric.ProvisionException;
import li.l1t.common.intake.exception.CommandExitMessage;
import li.l1t.common.intake.lib.UUIDHelper;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.UUID;


/**
 * Provides the executing command sender instance to commands.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-24
 */
public class OnlinePlayerProvider implements Provider<Player> {
    private final Server server;

    public OnlinePlayerProvider(Server server) {
        this.server = server;
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
            if (player == null) {
                throw new CommandExitMessage(String.format(
                        "§c$lFehler: §cEs ist kein Spieler mit dem Namen '%s' online.", input
                ));
            }
        }
        return player;
    }

    protected Player getPlayerFromUUIDOrNull(String input) {
        if (UUIDHelper.isValidUUID(input)) {
            UUID uuid = UUIDHelper.getFromString(input);
            Player player = server.getPlayer(uuid);
            if (player == null) {
                throw new CommandExitMessage(String.format(
                        "§c$lFehler: §cEs ist kein Spieler mit der UUID '%s' online.", uuid
                ));
            } else {
                return player;
            }
        }
        return null;
    }

    @Override
    public List<String> getSuggestions(String prefix) {
        return ImmutableList.of();
    }
}

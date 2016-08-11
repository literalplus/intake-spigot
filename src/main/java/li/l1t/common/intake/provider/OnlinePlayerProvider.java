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
        super(manager.getTranslator());
        this.server = manager.getPlugin().getServer();
    }

    @Override
    public boolean isProvided() {
        return true;
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

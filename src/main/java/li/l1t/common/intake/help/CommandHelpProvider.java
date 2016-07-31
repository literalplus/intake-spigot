package li.l1t.common.intake.help;

import li.l1t.common.chat.ComponentSender;
import li.l1t.common.intake.CommandsManager;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides usage information for commands.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-01
 */
public class CommandHelpProvider {
    private final CommandsManager manager;
    private final Map<String, Collection<BaseComponent[]>> cachedUsages = new HashMap<>();

    public CommandHelpProvider(CommandsManager manager) {
        this.manager = manager;
    }

    public void sendHelpOfTo(CommandSender sender, String argLine) {
        Collection<BaseComponent[]> messages = cachedUsages.get(argLine);
        if (messages == null) {
            messages = fetchHelpOf(argLine);
        }
        messages.forEach(components -> ComponentSender.sendTo(components, sender));
    }

    private Collection<BaseComponent[]> fetchHelpOf(String argLine) {
        Collection<BaseComponent[]> result;
        CommandHelpExtractor extractor = new CommandHelpExtractor(
                manager, new ArrayDeque<>(Arrays.asList(argLine.split(" ")))
        );
        result = extractor.build().getMessages();
        if (extractor.isFound()) {
            cachedUsages.put(argLine, result);
        }
        return result;
    }
}

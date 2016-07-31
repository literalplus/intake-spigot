package li.l1t.common.intake.help;

import com.sk89q.intake.CommandMapping;
import com.sk89q.intake.Description;
import com.sk89q.intake.dispatcher.Dispatcher;
import li.l1t.common.intake.CommandsManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.math.RandomUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;

/**
 * Extracts usage information from command annotations and creates clickable JSON components from
 * it.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 15.8.14
 */
class CommandHelpExtractor {
    private final CommandsManager manager;
    private final Deque<String> argStack;
    private final Collection<BaseComponent[]> messages = new ArrayList<>();
    private String argLine = "/";
    private Dispatcher currentDispatcher;
    private boolean found = true;

    public CommandHelpExtractor(CommandsManager manager, Deque<String> argStack) {
        this.manager = manager;
        this.argStack = argStack;
    }

    public boolean isFound() {
        return found;
    }

    public Collection<BaseComponent[]> getMessages() {
        return messages;
    }

    public CommandHelpExtractor build() {
        discoverRootDispatcher();
        appendSelectedSubCommands();
        appendHoverHintMessage();
        return this;
    }

    private void discoverRootDispatcher() {
        currentDispatcher = manager.getCommandGraph().commands().getDispatcher();
    }

    private void appendHoverHintMessage() {
        if (!messages.isEmpty() && RandomUtils.nextBoolean()) {
            messages.add(
                    new ComponentBuilder("Tipp: §eBewege deine Maus über die Befehle! :)")
                            .color(ChatColor.GOLD).create()
            );
        }
    }

    private void appendSelectedSubCommands() {
        if (argStack.isEmpty()) {
            appendAllSubCommands();
        } else {
            appendSelectedSubCommandAndChildren();
        }
    }

    private void appendSelectedSubCommandAndChildren() {
        String nextAlias = proceedInArgStack();
        CommandMapping currentMapping = findCurrentMapping(nextAlias);
        if (currentMapping == null) {
            appendNoSuchCommandError();
            return;
        }
        appendDirectDescription(currentMapping);
        if (currentMapping.getCallable() instanceof Dispatcher) {
            currentDispatcher = (Dispatcher) currentMapping.getCallable();
            appendSelectedSubCommands();
        }
    }

    private CommandMapping findCurrentMapping(String nextAlias) {
        return currentDispatcher.get(nextAlias.toLowerCase());
    }

    private String proceedInArgStack() {
        String nextAlias = argStack.poll();
        addToArgLine(nextAlias);
        return nextAlias;
    }

    private void appendNoSuchCommandError() {
        found = false;
        messages.add(TextComponent.fromLegacyText(
                manager.getErrorTranslator().translate("Help.NoSuchCommand", argLine)
        ));
    }

    private void appendAllSubCommands() {
        currentDispatcher.getCommands().forEach(this::appendDescriptionAndChildren);
    }

    private void appendDescriptionAndChildren(CommandMapping mapping) {
        appendDirectDescription(mapping);
        if (mapping.getCallable() instanceof Dispatcher) {
            String previousArgLine = argLine;
            addToArgLine(mapping.getPrimaryAlias());
            currentDispatcher = (Dispatcher) mapping.getCallable();
            appendAllSubCommands();
            argLine = previousArgLine;
        }
    }

    private void addToArgLine(String alias) {
        argLine += alias + " ";
    }

    private void appendDirectDescription(CommandMapping mapping) {
        Description description = mapping.getDescription();
        ComponentBuilder builder = startUsageMessage(mapping, description);
        appendShortDescriptionIfAvailable(description, builder);
        appendTooltipHintIfAvailable(description, builder);
        messages.add(builder.create());
    }

    private ComponentBuilder startUsageMessage(CommandMapping mapping, Description description) {
        String argLine = argStack + mapping.getPrimaryAlias() + " ";
        ClickEvent clickEvent = createUsageSuggestEvent(argLine);
        HoverEvent hoverEvent = createUsageSuggestTooltip(argLine);
        return new ComponentBuilder(argLine + description.getUsage() + " ")
                .color(ChatColor.GOLD)
                .event(clickEvent)
                .event(hoverEvent);
    }

    private HoverEvent createUsageSuggestTooltip(String argLine) {
        return new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("Hier klicken zum Kopieren:\n" + argLine).create()
        );
    }

    private ClickEvent createUsageSuggestEvent(String argLine) {
        return new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, argLine);
    }

    private void appendHelpTooltipIfAvailable(Description description, ComponentBuilder builder) {
        if (description.getHelp() != null) {
            appendHelpTooltip(description, builder);
        }
    }

    private void appendHelpTooltip(Description description, ComponentBuilder builder) {
        ComponentBuilder tooltipBuilder = new ComponentBuilder(description.getHelp())
                .color(ChatColor.GOLD);
        builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, tooltipBuilder.create()));
    }

    private void appendShortDescriptionIfAvailable(Description description, ComponentBuilder builder) {
        if (description.getShortDescription() != null) {
            builder.append(description.getShortDescription()).color(ChatColor.YELLOW);
            appendHelpTooltipIfAvailable(description, builder);
        }
    }

    private void appendTooltipHintIfAvailable(Description description, ComponentBuilder builder) {
        if (description.getHelp() != null) {
            builder.append(" " + manager.getErrorTranslator().translate("Help.ShowMore")).italic(true);
            appendHelpTooltip(description, builder);
        }
    }
}

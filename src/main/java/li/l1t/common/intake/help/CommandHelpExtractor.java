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

package li.l1t.common.intake.help;

import com.sk89q.intake.CommandMapping;
import com.sk89q.intake.Description;
import com.sk89q.intake.dispatcher.Dispatcher;
import li.l1t.common.intake.CommandsManager;
import li.l1t.common.intake.i18n.Message;
import net.md_5.bungee.api.ChatColor;
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
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2014-08-15
 */
class CommandHelpExtractor {
    private final CommandsManager manager;
    private final Deque<String> argStack;
    private final Collection<TranslatableUsage> messages = new ArrayList<>();
    private String argLine = "/";
    private Dispatcher currentDispatcher;

    CommandHelpExtractor(CommandsManager manager, Deque<String> argStack) {
        this.manager = manager;
        this.argStack = argStack;
    }

    public Collection<TranslatableUsage> getMessages() {
        return messages;
    }

    CommandHelpExtractor build() {
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
                    translator ->
                            new ComponentBuilder(translator.translateBuiltIn(
                                    Message.of("Help.HoverHint")
                            )).color(ChatColor.GOLD).create()
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
        //appendDirectDescription(currentMapping);
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
        messages.add(translator -> TextComponent.fromLegacyText(
                translator.translateBuiltIn(Message.of("Help.NoSuchCommand", argLine))
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
        messages.add(translator -> {
            ComponentBuilder builder = startUsageMessage(mapping, description, translator);
            appendShortDescriptionIfAvailable(description, builder, translator);
            appendTooltipHintIfAvailable(description, builder, translator);
            return builder.create();
        });
    }

    private ComponentBuilder startUsageMessage(CommandMapping mapping, Description description, UsageTranslator translator) {
        String argLine = this.argLine + mapping.getPrimaryAlias() + " ";
        ClickEvent clickEvent = createUsageSuggestEvent(argLine);
        HoverEvent hoverEvent = createUsageSuggestTooltip(argLine, translator);
        return new ComponentBuilder(argLine + translator.translateMeta(description.getUsage()) + " ")
                .color(ChatColor.GOLD)
                .event(clickEvent)
                .event(hoverEvent);
    }

    private HoverEvent createUsageSuggestTooltip(String argLine, UsageTranslator translator) {
        return new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(translator.translateBuiltIn(
                        Message.of("Help.ClickToCopy", argLine))
                ).create()
        );
    }

    private ClickEvent createUsageSuggestEvent(String argLine) {
        return new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, argLine);
    }

    private void appendShortDescriptionIfAvailable(Description description, ComponentBuilder builder, UsageTranslator translator) {
        if (description.getShortDescription() != null) {
            builder.append(translator.translateMeta(description.getShortDescription()))
                    .color(ChatColor.YELLOW);
        }
    }

    private void appendTooltipHintIfAvailable(Description description, ComponentBuilder builder, UsageTranslator translator) {
        if (description.getHelp() != null) {
            builder.append(" " + translator.translateBuiltIn(Message.of("Help.ShowMore"))).italic(true);
            appendHelpTooltip(description, builder, translator);
        }
    }

    private void appendHelpTooltip(Description description, ComponentBuilder builder, UsageTranslator translator) {
        ComponentBuilder tooltipBuilder = new ComponentBuilder(
                translator.translateMeta(description.getHelp())
        ).color(ChatColor.GOLD);
        builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, tooltipBuilder.create()));
    }
}

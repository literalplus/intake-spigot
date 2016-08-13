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

import com.sk89q.intake.Intake;
import com.sk89q.intake.argument.Namespace;
import com.sk89q.intake.fluent.CommandGraph;
import com.sk89q.intake.parametric.ParametricBuilder;
import com.sk89q.intake.parametric.binder.BindingBuilder;
import com.sk89q.intake.parametric.provider.PrimitivesModule;
import li.l1t.common.CommandRegistrationManager;
import li.l1t.common.intake.help.CommandHelpProvider;
import li.l1t.common.intake.i18n.ErrorTranslator;
import li.l1t.common.intake.i18n.ResourceBundleTranslator;
import li.l1t.common.intake.i18n.Translator;
import li.l1t.common.intake.provider.CommandSenderProvider;
import li.l1t.common.intake.provider.ItemInHandProvider;
import li.l1t.common.intake.provider.MergedTextProvider;
import li.l1t.common.intake.provider.OnlinePlayerProvider;
import li.l1t.common.intake.provider.PlayerSenderProvider;
import li.l1t.common.intake.provider.annotation.ItemInHand;
import li.l1t.common.intake.provider.annotation.Merged;
import li.l1t.common.intake.provider.annotation.OnlinePlayer;
import li.l1t.common.intake.provider.annotation.Sender;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages commands registered with Intake.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-23
 */
public class CommandsManager {
    final CommandRegistrationManager commandRegistrationManager = new CommandRegistrationManager();
    private final Plugin plugin;
    private final Map<Object, Object> namespaceTemplate = new HashMap<>();
    private final Map<String, CommandBuilder> commandBuilders = new HashMap<>();
    private final ParametricBuilder builder = new ParametricBuilder(Intake.createInjector());
    private final CommonInjectorModule injectorModule;
    private final CommandGraph commandGraph = new CommandGraph().builder(builder);
    private final CommandHelpProvider helpProvider = new CommandHelpProvider(this);
    private ErrorTranslator errorTranslator;
    private Translator translator;

    public CommandsManager(Plugin plugin) {
        this.plugin = plugin;
        this.translator = new ResourceBundleTranslator();
        this.errorTranslator = new ErrorTranslator(translator);
        builder.getInjector().install(injectorModule = new CommonInjectorModule());
        builder.getInjector().install(new PrimitivesModule());
        builder.setAuthorizer(new CommandSenderAuthorizer());
        bindDefaultInjections();
        putIntoNamespace(CommandsManager.class, this);
    }

    private void bindDefaultInjections() {
        bindPluginToClass(Plugin.class);
        bindPluginToClass(plugin.getClass());
        injectorModule.bind(CommandsManager.class)
                .toInstance(this);
        injectorModule.bind(Server.class)
                .toInstance(plugin.getServer());
        injectorModule.bind(Player.class)
                .annotatedWith(OnlinePlayer.class)
                .toProvider(new OnlinePlayerProvider(this));
        injectorModule.bind(Player.class)
                .annotatedWith(Sender.class)
                .toProvider(new PlayerSenderProvider(getTranslator()));
        injectorModule.bind(CommandSender.class)
                .toProvider(new CommandSenderProvider(getTranslator()));
        injectorModule.bind(String.class)
                .annotatedWith(Merged.class)
                .toProvider(new MergedTextProvider());
        injectorModule.bind(ItemStack.class)
                .annotatedWith(ItemInHand.class)
                .toProvider(new ItemInHandProvider(getTranslator()));
    }

    @SuppressWarnings("unchecked")
    private <T extends Plugin> void bindPluginToClass(Class<?> key) {
        injectorModule.bind((Class<T>) key)
                .toInstance((T) plugin);
    }

    public void putIntoNamespace(Object key, Object value) {
        namespaceTemplate.put(key, value);
        commandBuilders.values().forEach(builder -> builder.putIntoNamespaceIfAvailable(key, value));
    }

    public <T> BindingBuilder<T> bind(Class<T> clazz) {
        return injectorModule.bind(clazz);
    }

    public void applyNamespaceTemplateTo(Namespace namespace) {
        for (Map.Entry<Object, Object> entry : namespaceTemplate.entrySet()) {
            namespace.put(entry.getKey(), entry.getValue());
        }
    }

    public void registerCommand(Object handler, String name, String... aliases) {
        getBuilderFor(name)
                .withAliases(aliases)
                .withDispatcherFor(handler)
                .create()
                .register();
    }

    public CommandBuilder getBuilderFor(String commandName) {
        return commandBuilders.computeIfAbsent(
                commandName,
                name -> new CommandBuilder(this).withName(commandName)
        );
    }

    public IntakeCommand getCommand(String commandName) {
        CommandBuilder builder = commandBuilders.get(commandName);
        return builder == null ? null : builder.getCommand();
    }

    public ErrorTranslator getErrorTranslator() {
        return errorTranslator;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public ParametricBuilder getBuilder() {
        return builder;
    }

    public CommonInjectorModule getInjectorModule() {
        return injectorModule;
    }

    public CommandGraph getCommandGraph() {
        return commandGraph;
    }

    public CommandHelpProvider getHelpProvider() {
        return helpProvider;
    }

    public Translator getTranslator() {
        return translator;
    }
}

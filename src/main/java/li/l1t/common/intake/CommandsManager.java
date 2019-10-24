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
import com.sk89q.intake.Intake;
import com.sk89q.intake.argument.Namespace;
import com.sk89q.intake.fluent.CommandGraph;
import com.sk89q.intake.parametric.ParametricBuilder;
import com.sk89q.intake.parametric.binder.BindingBuilder;
import com.sk89q.intake.parametric.provider.PrimitivesModule;
import li.l1t.common.CommandRegistrationManager;
import li.l1t.common.intake.help.CommandHelpProvider;
import li.l1t.common.intake.i18n.ErrorTranslator;
import li.l1t.common.intake.i18n.LocaleSelectionProvider;
import li.l1t.common.intake.i18n.ResourceBundleTranslator;
import li.l1t.common.intake.i18n.Translator;
import li.l1t.common.intake.provider.CommandSenderProvider;
import li.l1t.common.intake.provider.ItemInHandProvider;
import li.l1t.common.intake.provider.MergedTextProvider;
import li.l1t.common.intake.provider.OnlinePlayerProvider;
import li.l1t.common.intake.provider.OnlineSenderProvider;
import li.l1t.common.intake.provider.PlayerSenderProvider;
import li.l1t.common.intake.provider.UUIDProvider;
import li.l1t.common.intake.provider.annotation.ItemInHand;
import li.l1t.common.intake.provider.annotation.Merged;
import li.l1t.common.intake.provider.annotation.OnlinePlayer;
import li.l1t.common.intake.provider.annotation.OnlineSender;
import li.l1t.common.intake.provider.annotation.Sender;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

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
    private final CommonInjectorModule injectorModule = new CommonInjectorModule();
    private final CommandGraph commandGraph = new CommandGraph().builder(builder);
    private final CommandHelpProvider helpProvider = new CommandHelpProvider(this);
    private final List<CommandExceptionListener> exceptionListeners = new ArrayList<>();
    private String fallbackPrefix;
    private ErrorTranslator errorTranslator;
    private Translator translator;

    public CommandsManager(Plugin plugin) {
        this.plugin = plugin;
        this.translator = new ResourceBundleTranslator();
        this.errorTranslator = new ErrorTranslator(translator);
        builder.getInjector().install(injectorModule);
        builder.getInjector().install(new PrimitivesModule());
        builder.setAuthorizer(new CommandSenderAuthorizer());
        bindDefaultInjections();
        putIntoNamespace(CommandsManager.class, this);
        this.fallbackPrefix = findFallbackPrefix(plugin);
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
        injectorModule.bind(CommandSender.class)
                .annotatedWith(OnlineSender.class)
                .toProvider(new OnlineSenderProvider(this));
        injectorModule.bind(Player.class)
                .annotatedWith(Sender.class)
                .toProvider(new PlayerSenderProvider());
        injectorModule.bind(CommandSender.class)
                .toProvider(new CommandSenderProvider());
        injectorModule.bind(String.class)
                .annotatedWith(Merged.class)
                .toProvider(new MergedTextProvider());
        injectorModule.bind(ItemStack.class)
                .annotatedWith(ItemInHand.class)
                .toProvider(new ItemInHandProvider());
        injectorModule.bind(UUID.class)
                .toProvider(new UUIDProvider());
    }

    @SuppressWarnings("unchecked")
    private <T extends Plugin> void bindPluginToClass(Class<?> key) {
        injectorModule.bind((Class<T>) key)
                .toInstance((T) plugin);
    }

    public void putIntoNamespace(Object key, Object value) {
        namespaceTemplate.put(key, value);
        commandBuilders.values().forEach(commandBuilder -> commandBuilder.putIntoNamespaceIfAvailable(key, value));
    }

    private String findFallbackPrefix(Plugin plugin) {
        PluginDescriptionFile description = plugin.getDescription();
        if (description.getPrefix() != null) {
            return description.getPrefix().toLowerCase().replace(" ", "-");
        } else {
            return description.getName().toLowerCase();
        }
    }

    /**
     * Sets the global override locale, which is used for every command sender.
     *
     * @param locale the locale to set
     */
    public void setLocale(Locale locale) {
        Preconditions.checkNotNull(locale, "locale");
        setLocale(any -> locale);
    }

    /**
     * Sets the global locale selection provider, which is used to translate messages for command senders.
     *
     * @param selectionProvider the selection provider
     */
    public void setLocale(LocaleSelectionProvider selectionProvider) {
        Preconditions.checkNotNull(selectionProvider, "selectionProvider");
        translator.setSelectionProvider(selectionProvider);
    }

    public <T> BindingBuilder<T> bind(Class<T> clazz) {
        return injectorModule.bind(clazz);
    }

    public void applyNamespaceTemplateTo(Namespace namespace) {
        for (Map.Entry<Object, Object> entry : namespaceTemplate.entrySet()) {
            namespace.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Registers a plain Intake command. The handler may be any object with {@link com.sk89q.intake.Command} methods.
     *
     * @param handler the handler for the command
     * @param name    the primary alias the command is called with, e.g. "test" for calling with /test
     * @param aliases the aliases that can also be used to call the command (optional)
     * @see #startRegistration(Object, String, String...) for more sophisticated setups
     */
    public void registerCommand(Object handler, String name, String... aliases) {
        startRegistration(handler, name, aliases)
                .create()
                .register();
    }

    /**
     * Starts the registration of an Intake command. The handler may be any object with {@link com.sk89q.intake.Command}
     * methods. This does <b>not</b> actually register the command, but instead just creates a builder that may be
     * configured for more sophisticated setups, for example
     * {@link CommandBuilder#withSubHandler(Object, String...) detached sub-commands}.
     * Make sure to call {@link CommandBuilder#create()} and {@link CommandBuilder#register()} when you're done!
     * <p><b>Note:</b> This does register the builder for the given command name, so subsequent calls for the same
     * name will return that builder, regardless of whether it was registered or not.</p>
     *
     * @param handler the handler for the command
     * @param name    the primary alias the command is called with, e.g. "test" for calling with /test
     * @param aliases the aliases that can also be used to call the command (optional)
     * @return the new, unfinished command builder
     * @see #registerCommand(Object, String, String...) for simpler use cases
     */
    public CommandBuilder startRegistration(Object handler, String name, String... aliases) {
        return getBuilderFor(name)
                .withAliases(aliases)
                .withDispatcherFor(handler);
    }

    /**
     * Gets the command builder for given command name. If there is none yes, creates it.
     * <p><b>Note:</b> This is not for command registration. Use {@link #registerCommand(Object, String, String...)} or
     * {@link #startRegistration(Object, String, String...)} for that.</p>
     *
     * @param commandName the name of the command to get a builder for
     * @return the builder for given command name
     */
    public CommandBuilder getBuilderFor(String commandName) {
        return commandBuilders.computeIfAbsent(
                commandName,
                name -> new CommandBuilder(this).withName(commandName)
        );
    }

    public IntakeCommand getCommand(String commandName) {
        CommandBuilder commandBuilder = commandBuilders.get(commandName);
        return commandBuilder == null ? null : commandBuilder.getCommand();
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

    public void setTranslator(Translator translator) {
        Preconditions.checkNotNull(translator, "translator");
        LocaleSelectionProvider selectionProvider = this.translator.getSelectionProvider();
        this.translator = translator;
        this.translator.setSelectionProvider(selectionProvider);
        this.errorTranslator.setTranslator(translator);
    }

    /**
     * Registers an exception listener that will be called when a command execution throws an exception. Note that
     * listeners are called strictly in registration order.
     *
     * @param listener the listener to register
     */
    public void addExceptionListener(CommandExceptionListener listener) {
        Preconditions.checkNotNull(listener, "listener");
        exceptionListeners.add(listener);
    }

    /**
     * Calls the registered exception listeners in order of registration, until one of them cancels the default
     * exception handling by returning false, or there are no more listeners.
     *
     * @param argLine   the argument line that generated the exception
     * @param sender    the command sender who send the argument line
     * @param exception the exception that was caught
     * @return whether any of the listeners cancelled the default exception handling
     * @see CommandExceptionListener#handle(String, CommandSender, Exception)
     */
    boolean callExceptionListeners(String argLine, CommandSender sender, Exception exception) {
        for (CommandExceptionListener listener : exceptionListeners) {
            if (!listener.handle(argLine, sender, exception)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return the fallback prefix used by this manager for new registrations
     * @see CommandRegistrationManager#registerCommand(Command, String)
     */
    public String getFallbackPrefix() {
        return fallbackPrefix;
    }

    /**
     * Sets this manager's fallback prefix. The default prefix is the prefix from {@code plugin.yml}, if set, and the
     * plugin's name otherwise.
     *
     * @param newPrefix the fallback prefix to use for future command registrations
     * @see CommandRegistrationManager#registerCommand(Command, String)
     */
    public void setFallbackPrefix(String newPrefix) {
        Preconditions.checkNotNull(newPrefix, "newPrefix");
        this.fallbackPrefix = newPrefix;
    }
}

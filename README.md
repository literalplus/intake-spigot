[//]: # (                                                                    )
[//]: # (                                                                    )
[//]: # (  ooooooooo.   oooooooooooo       .o.       oooooooooo.             )
[//]: # (  `888   `Y88. `888'     `8      .888.      `888'   `Y8b            )
[//]: # (   888   .d88'  888             .8"888.      888      888           )
[//]: # (   888ooo88P'   888oooo8       .8' `888.     888      888           )
[//]: # (   888`88b.     888    "      .88ooo8888.    888      888           )
[//]: # (   888  `88b.   888       o  .8'     `888.   888     d88'           )
[//]: # (  o888o  o888o o888ooooood8 o88o     o8888o o888bood8P'             )
[//]: # (                                                                    )
[//]: # (               READ THIS BEFORE EDITING                             )
[//]: # (                                                                    )
[//]: # ( At every release, /README.md is overwritten with                   )
[//]: # ( templates/README.md! Any changes not reflected                     )
[//]: # ( there will be void at the next release! See                        )    
[//]: # ( templates/README.md for details.                                   )
[//]: # (                                                                    )
[//]: # ( If you, for whatever reason, need to generate the file             )
[//]: # ( manually, use: [with the latest release version]                   )
[//]: # (     mvn -DreadmeVersion=x.x.x.x \                                  )
[//]: # (        resources:copy-resources@update-readme-version              )                                                      

# intake-spigot
A Spigot bridge for [sk89q/Intake](https://github.com/sk89q/Intake). 
Delivered as standalone plugin (`intake-spigot-4.2.35-plugin.jar`) or simple Maven dependency (if you prefer the shades (⌐■_■)).

The standalone plugin currently comes with a [custom fork of Intake](https://github.com/xxyy/Intake) 
that has some fixes necessary for some features to work. However, there are pending 
[pull](https://github.com/sk89q/Intake/pull/26) [requests](https://github.com/sk89q/Intake/pull/25) 
to upstream.
Once upstream accepts the changes or introduces comparable features, the dependency will be 
changed back.
Namely, upstream doesn't allow more than a single `@Classifier` annotation per parameter type and 
their `TextProvider` is broken. Also they don't support default commands using empty aliases.

# Features

When I first wanted to try Intake, I found out the hard way that it needs quite a bit of boilerplate code when
used with Bukkit, especially compared to the old 
[sk89q-command-framework](https://github.com/OvercastNetwork/sk89q-command-framework).

To save others (and my future self) from having to go through this pain, I created a more-or-less 
abstract library for what I needed. This includes:

* basic registration of Intake commands **without messing with `plugin.yml`**
* automatic creation of **helpful help messages** from command metadata with **JSON tooltips** and `<command> help` subcommands
* extensible **error handling**
* custom unchecked message exceptions (`CommandExitMessage`)
* **automatic translation** of Intake error messages to other languages (German is included, feel free to PR more!)
* **parameter providers** for common things used in commands 
(such as `@ItemInHand ItemStack`, `@Merged  @Colored String`, `CommandSender`, `@Sender Player`, 
`@OnlinePlayer Player`)
* a intuitive builder framework
* detached nested commands! (yay separation)
* all that without having to shade anything 
[(⌐■_■)](https://www.youtube.com/watch?v=X2LTL8KgKv8)

# Installation

Installing this as a server owner is as easy as dropping the
[current plugin jar](https://ci.l1t.li/job/public~intake-spigot/lastRelease/)
into your server's plugins folder.

Note that there's two artifacts: `intake-spigot.jar`,
which includes only the API, and `intake-spigot-4.2.35-plugin.jar`, which
can be used as a standalone plugin that other plugins can depend on. Try not to confuse
these two, because doing so usually causes errors and stack traces, and nobody wants that
to happen. This artifact requires that [`XYC`](https://github.com/xxyy/xyc) is also
installed as a plugin. If none of your other plugins depend on XYC, you can also use the
special `intake-spigot-4.2.35-plugin-with-xyc.jar` which includes XYC.

Installing this as a developer is slightly more complicated, since this 
project isn't being deployed into Maven Central.

The latest release is `4.2.35`. The latest
git commit included in that version is
[this one](https://github.com/xxyy/intake-spigot/releases/tag/intake-spigot-4.2.35).

## Maven

````xml
<repositories>
  <repository>
    <id>xxyy-repo</id>
    <url>https://repo.l1t.li/xxyy-public/</url>
  </repository>
</repositories>
<dependencies>
  <dependency>
    <groupId>li.l1t.common</groupId>
    <artifactId>intake-spigot</artifactId>
    <version>4.2.35</version>
  </dependency>
</dependencies>
````

## Gradle

````groovy
repositories {
    maven { url "https://repo.l1t.li/xxyy-public/" }
}

dependencies {
    compile group: 'li.l1t.common', name: 'intake-spigot', version: '4.2.35'
}
````

# Examples

Some plugins using this library are [the Expvp minigame](https://github.com/xxyy/expvp)
(full-blown Dependency Injection, service layer, custom everything) and
[my test plugin](https://github.com/xxyy/intake-spigot-test). If you have written a
Free Software and/or open-source plugin that you think could be listed here, 
open an issue on GitHub.

Here's a simple command with some custom injections:

````java
public class MyCommand {
    @Command(aliases = "setname", min = 2,
            desc = "Changes the name",
            usage = "[id] [name...]")
    @Require("your.permission")
    public void editName(YourService service, CommandSender sender,
                         YourThing thing,
                         @Merged @Colored String name)
            throws IOException {
        String previousName = thing.getName();
        thing.setName(name);
        sender.sendMessage("This is an awesome message");
    }
}
````

Here's the code required to make XYC-Intake class and inject that:

````java
//best time to call this is on enable
private void registerCommands() {
    commandsManager = new CommandsManager(plugin);
    commandsManager.setLocale(sender -> /* your command sender to locale logic */);
    commandsManager.setLocale(Locale.ENGLISH); // use the same locale for everyone
    // if you don't set any locale, the system default is used
    registerInjections();
    commandsManager.registerCommand(new MyCommand(), "mc", "mcalias");
}

private void registerInjections() {
    commandsManager.bind(YourService.class).toInstance(new YourService());
    commandsManager.bind(YourThing.class).toProvider(new YourThingProvider(thingService));
}
````

The `registerCommand()` method automatically takes care of registering your command with Spigot.
Sadly, there is no public API for that, so it uses some internals for that, that may not be
compatible with certain nasty plugins which exchange the Spigot command map with their own version -
it should be fine for most applications though.

Note that you need to remove the command definitions from your `plugin.yml` before Intake-Spigot
can register them properly!

Providers work like in Vanilla Intake. Here's an example for sake of completeness:

````java
public class YourThingProvider implements Provider<YourThing> {
    private final YourService service;

    public SkillProvider(YourService service) {
        this.service = service;
    }

    @Override
    public boolean isProvided() {
        return false;
    }

    @Nullable
    @Override
    public Skill get(CommandArgs arguments, List<? extends Annotation> modifiers) throws ArgumentException, ProvisionException {
        String thingId = arguments.next();
        return service.getThingOrFail(skillId);
    }

    @Override
    public List<String> getSuggestions(String prefix) { //tab-complete
        return service.getAllThings().stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
    }
}
````

If you want to see this plugin used in an actual project, take
a look at [Expvp](https://github.com/xxyy/Expvp). (Note however
that it makes heavy use of dependency injection and therefore
your own usage might differ, with some more repetitive code)

(Note that tab-complete for providers won't work until upstream pulls 
[this PR](https://github.com/sk89q/Intake/pull/23).

# Screenshots

Here's some screenshots that demonstrate how generated messages look ingame.

The reason that it's all in German is obviously for demonstrating that it's multi-language and 
not that I was too lazy to change the language.

Here's some help generated by the library, complete with a JSON tooltip:

![some generated help with JSON tooltip](https://github.com/xxyy/intake-spigot/raw/master/assets/img/help.png)

Here's an Intake error message that was translated by the plugin:

![translated Intake error](https://github.com/xxyy/intake-spigot/raw/master/assets/img/intake-translated.png)

Here's another translated message:

![why is this a separate screenshot](https://github.com/xxyy/intake-spigot/raw/master/assets/img/translated-messages.png)

# Contributing

All contributions welcome, including further translations! 

This project uses standard IntelliJ code style. Format your code with `Ctrl+Alt+L`. 

I recommend that you read ['Clean Code' by the awesome Robert C. Martin](https://www.google.at/webhp?q=clean+code+pdf#newwindow=1&q=clean+code+pdf).

## Updating the Readme

When editing the readme, you need to change the template in `docs-templates/README.md` and then
update the actual `README.md` using:

````bash
mvn -DreadmeVersion=4.2.35 resources:copy-resources@update-readme-version
````

# License

This project is licensed under LGPL, mainly because it includes two lines from Intake that I had to copy 
(y u make everything package-private).

See the included `LICENSE.txt` file for details.

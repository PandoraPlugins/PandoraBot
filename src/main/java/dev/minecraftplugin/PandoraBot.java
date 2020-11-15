package dev.minecraftplugin;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import dev.minecraftplugin.commands.HelpCommand;
import dev.minecraftplugin.commands.PingCommand;
import dev.minecraftplugin.commands.ShutdownCommand;
import dev.minecraftplugin.commands.announce.AnnounceCommand;
import dev.minecraftplugin.commands.category.AdministrativeCategory;
import dev.minecraftplugin.configuration.BotSettings;
import dev.minecraftplugin.dialogue.DialogueManager;
import dev.minecraftplugin.lib.config.Config;
import dev.minecraftplugin.lib.config.ConfigManager;
import dev.minecraftplugin.listener.WelcomeQuitListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;
import java.util.Scanner;

public class PandoraBot {
    private final ConfigManager configManager;
    private final Config<BotSettings> botConfig;
    private final DialogueManager dialogueManager;
    private JDA jda;

    public JDA getJda() {
        return jda;
    }

    public PandoraBot() throws InterruptedException {
        configManager = new ConfigManager();
        dialogueManager = new DialogueManager(this);
        // Load in our global settings
        botConfig = configManager.loadConfig("/data/settings", new BotSettings());

        // JDA-Utilities stuff
        CommandClientBuilder commandClientBuilder = new CommandClientBuilder();

        setupBuilder(commandClientBuilder);

        // Setting the bots discord status
        commandClientBuilder.setActivity(Activity.playing(botConfig.getConfiguration().status));
        commandClientBuilder.setStatus(OnlineStatus.ONLINE);

        // Bot can not start until we have the correct token.
        boolean successful = false;
        // Infinite loop until token is correct.
        while (!successful) {

            if (botConfig.getConfiguration().token == null) {
                // first start
                Scanner s = new Scanner(System.in);
                System.out.print("Please input your bot token: ");
                botConfig.getConfiguration().token = s.nextLine();
            }

            // Build the JDA
            JDABuilder builder = JDABuilder.createDefault(botConfig.getConfiguration().token)
                    .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES);
            builder.setMemberCachePolicy(MemberCachePolicy.ONLINE);

            // Setting the bot to auto reconnect if it gets disconnected
            builder.setAutoReconnect(true);

            // This is where we add the JDA-Utilities stuff to jda itself.
            CommandClient client = commandClientBuilder.build();
            builder.addEventListeners(client);

            addListener(builder);
            builder.addEventListeners(dialogueManager);
            try {
                // We try and connect, if it throws an login error there was something wrong with token and as such
                // We try again.
                jda = builder.build();
                successful = true;
            } catch (LoginException e) {
                e.printStackTrace();
                // Means invalid token, we should null our token that was saved to allow for it to input another one.
                if (e.getMessage().trim().equalsIgnoreCase("The provided token is invalid!")) {
                    botConfig.getConfiguration().token = null;
                }
                // We sleep for 1 second before trying to login again
                Thread.sleep(1000);
            }
        }
        botConfig.saveConfig();
    }

    public static void main(String[] args) throws InterruptedException {
        new PandoraBot();
    }

    public Config<BotSettings> getBotConfig() {
        return botConfig;
    }

    public ConfigManager getManager() {
        return configManager;
    }

    public DialogueManager getDialogueManager() {
        return dialogueManager;
    }

    private void setupBuilder(CommandClientBuilder builder) {
        String[] emojis = botConfig.getConfiguration().emojis;
        builder.setEmojis(emojis[0], emojis[1], emojis[2]);

        builder.setPrefix(botConfig.getConfiguration().commandPrefix);
        builder.setAlternativePrefix(botConfig.getConfiguration().alternativeCommandPrefix);
        builder.setOwnerId(botConfig.getConfiguration().ownerID);
        builder.setCoOwnerIds(botConfig.getConfiguration().admins);

        builder.useHelpBuilder(true);
        builder.setHelpConsumer(new HelpCommand(botConfig));

        Command.Category admin = new AdministrativeCategory(emojis[2] + " You do not have permission to use this command!", botConfig);

        builder.addCommands(
                new PingCommand(admin),
                new AnnounceCommand(this, admin),
                new ShutdownCommand(this, admin));
    }

    private void addListener(JDABuilder builder) {
        builder.addEventListeners(new WelcomeQuitListener(botConfig));
    }
}

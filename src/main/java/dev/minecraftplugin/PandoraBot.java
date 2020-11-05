package dev.minecraftplugin;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import dev.minecraftplugin.commands.HelpCommand;
import dev.minecraftplugin.commands.PingCommand;
import dev.minecraftplugin.commands.ShutdownCommand;
import dev.minecraftplugin.commands.category.AdministrativeCategory;
import dev.minecraftplugin.configuration.BotSettings;
import dev.minecraftplugin.lib.config.Config;
import dev.minecraftplugin.lib.config.ConfigManager;
import dev.minecraftplugin.listener.WelcomeQuitListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.util.Scanner;

public class PandoraBot {
    private final ConfigManager manager;
    private final Config<BotSettings> botConfig;

    public Config<BotSettings> getBotConfig() {
        return botConfig;
    }

    public ConfigManager getManager() {
        return manager;
    }

    public PandoraBot() throws InterruptedException {
        manager = new ConfigManager();
        // Load in our global settings
        botConfig = manager.loadConfig("/data/settings", new BotSettings());

        // Bot can not start until we have the correct token.
        boolean successful = false;
        // Infinite loop until token is correct.
        while (!successful) {

            if (botConfig.getConfiguration().token == null) {
                // first start
                Scanner s = new Scanner(System.in);
                System.out.print("Please input your bot token: ");
                botConfig.getConfiguration().token = s.nextLine();
                botConfig.saveConfig();
            }

            // Build the JDA
            JDABuilder builder = JDABuilder.createDefault(botConfig.getConfiguration().token)
                    .enableIntents(GatewayIntent.GUILD_MEMBERS);


            // JDA-Utilities stuff
            CommandClientBuilder commandClientBuilder = new CommandClientBuilder();

            setupBuilder(commandClientBuilder);

            // Setting the bots discord status
            commandClientBuilder.setActivity(Activity.playing("PandoraPvP | !help"));
            commandClientBuilder.setStatus(OnlineStatus.ONLINE);

            // Setting the bot to auto reconnect if it gets disconnected
            builder.setAutoReconnect(true);

            // This is where we add the JDA-Utilities stuff to jda itself.
            CommandClient client = commandClientBuilder.build();
            builder.addEventListeners(client);

            addListener(builder);
            try {
                // We try and connect, if it throws an login error there was something wrong with token and as such
                // We try again.
                builder.build();
                successful = true;
            } catch (LoginException e) {
                e.printStackTrace();
                // Means invalid token, we should null our token that was saved to allow for it to input another one.
                if (e.getMessage().trim().equalsIgnoreCase("The provided token is invalid!")) {
                    botConfig.getConfiguration().token = null;
                    botConfig.saveConfig();
                }
                // We sleep for 1 second before trying to login again
                Thread.sleep(1000);
            }
        }
    }

    private void setupBuilder(CommandClientBuilder builder) {
        builder.setEmojis("<a:good:773731817248653323>", "<a:medium:773731817718677576>", "<a:bad:773731817420750898>");

        builder.setPrefix(botConfig.getConfiguration().commandPrefix);
        builder.setOwnerId("315146866268569601");
        builder.setCoOwnerIds(botConfig.getConfiguration().admins);

        builder.useHelpBuilder(true);
        builder.setHelpConsumer(new HelpCommand(botConfig));

        Command.Category admin = new AdministrativeCategory("<a:bad:773731817420750898> You do not have permission to use this command!", botConfig);

        builder.addCommands(
                new PingCommand(admin),
                new ShutdownCommand(this, admin));
    }

    private void addListener(JDABuilder builder) {
        builder.addEventListeners(new WelcomeQuitListener(botConfig));
    }

    public static void main(String[] args) throws InterruptedException {
        new PandoraBot();
    }
}

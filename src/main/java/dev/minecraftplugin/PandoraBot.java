package dev.minecraftplugin;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import dev.minecraftplugin.configuration.BotSettings;
import dev.minecraftplugin.lib.config.Config;
import dev.minecraftplugin.lib.config.ConfigManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.util.Scanner;

public class PandoraBot {
    private final ConfigManager manager;
    private final Config<BotSettings> botConfig;
    private JDA jda;

    public PandoraBot() {
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
            JDABuilder builder = JDABuilder.createDefault(botConfig.getConfiguration().token);


            CommandClientBuilder commandClientBuilder = new CommandClientBuilder();
            commandClientBuilder.setPrefix(botConfig.getConfiguration().commandPrefix);
            commandClientBuilder.setOwnerId("315146866268569601");
            commandClientBuilder.setCoOwnerIds(botConfig.getConfiguration().admins);

            builder.setActivity(Activity.playing("PandoraPvP"));
            builder.setStatus(OnlineStatus.ONLINE);
            builder.setAutoReconnect(true);

            CommandClient client = commandClientBuilder.build();
            builder.addEventListeners(client);
            try {
                jda = builder.build();
                successful = true;
            } catch (LoginException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new PandoraBot();
    }

    public ConfigManager getManager() {
        return manager;
    }

    public Config<BotSettings> getBotConfig() {
        return botConfig;
    }

    public JDA getJda() {
        return jda;
    }
}

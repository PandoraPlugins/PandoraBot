package dev.minecraftplugin.listener;

import com.sun.media.jfxmedia.logging.Logger;
import dev.minecraftplugin.configuration.BotSettings;
import dev.minecraftplugin.lib.config.Config;
import dev.minecraftplugin.lib.util.Color;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.logging.Level;

public class WelcomeQuitListener extends ListenerAdapter {
    private final Config<BotSettings> config;

    public WelcomeQuitListener(Config<BotSettings> config) {
        this.config = config;
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        TextChannel channel = event.getGuild().getTextChannelById(config.getConfiguration().joinMessageChannel);
        if (channel == null) {
            Logger.logMsg(Level.WARNING.intValue(), "Welcome Listener Channel is not configured correctly! Could not find channel!");
            return;
        }
        channel.sendMessage(
                new EmbedBuilder()
                        .setTitle(event.getUser().getName())
                        .setAuthor("Welcome to PandoraPvP", "https://discord.gg/ERgVCjw", event.getGuild().getIconUrl())
                        .setFooter("PandoraPvP")
                        .setTimestamp(Instant.now())
                        .setDescription("**IP:** play.pandorapvp.co\n**Members:** " + event.getGuild().getMemberCount() + "\n\n\n\n")
                        .setThumbnail(event.getUser().getEffectiveAvatarUrl())
                        .setColor(Color.color(config.getConfiguration().joinMessageColor))
                        .build()).queue();
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        super.onGuildMemberRemove(event);
    }
}

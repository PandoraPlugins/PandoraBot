package dev.minecraftplugin.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import dev.minecraftplugin.configuration.BotSettings;
import dev.minecraftplugin.lib.config.Config;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class HelpCommand implements Consumer<CommandEvent> {
    private final Config<BotSettings> config;

    public HelpCommand(Config<BotSettings> config) {
        this.config = config;
    }

    @Override
    public void accept(CommandEvent commandEvent) {
        commandEvent.getMessage().delete().queue();
        commandEvent.replyInDm(new EmbedBuilder()
                .setAuthor("PandoraPVP", "", commandEvent.getSelfUser().getEffectiveAvatarUrl())
                .setFooter("Help Message")
                .setColor(Color.decode(config.getConfiguration().helpColor.replace("#", "")))
                .setDescription(formatCommands(commandEvent.getClient().getCommands()))
                .build());
    }

    private String formatCommands(List<Command> commandList) {
        // todo: do this
        return "Work In Progress (Please try again later)";
    }
}

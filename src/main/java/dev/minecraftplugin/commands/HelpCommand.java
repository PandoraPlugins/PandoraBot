package dev.minecraftplugin.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import dev.minecraftplugin.configuration.BotSettings;
import dev.minecraftplugin.lib.config.Config;
import dev.minecraftplugin.lib.util.Color;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class HelpCommand implements Consumer<CommandEvent> {
    private final Config<BotSettings> config;

    public HelpCommand(Config<BotSettings> config) {
        this.config = config;
    }

    @Override
    public void accept(CommandEvent commandEvent) {
        if (commandEvent.isFromType(ChannelType.TEXT))
            commandEvent.getMessage().delete().queue();
        commandEvent.replyInDm(new EmbedBuilder()
                .setAuthor("PandoraPVP", "https://discord.gg/ERgVCjw", commandEvent.getSelfUser().getEffectiveAvatarUrl())
                .setFooter("Help Message")
                .setColor(Color.color(config.getConfiguration().helpColor))
                .setDescription(formatCommands(commandEvent.getClient().getCommands(), commandEvent))
                .build(), success -> {
        }, fail -> commandEvent.replyWarning("Could not send you help because you are blocking Direct Messages."));
    }

    private String formatCommands(List<Command> commands, CommandEvent event) {
        StringBuilder builder = new StringBuilder();


        Command.Category category = null;
        for (Command command : commands) {
            if (!command.isHidden() && (!command.isOwnerCommand() || event.isOwner())) {
                if (!Objects.equals(category, command.getCategory())) {
                    category = command.getCategory();
                    builder.append("\n\n  __").append(category == null ? "No Category" : category.getName()).append("__:\n");
                }
                builder.append("\n`").append(event.getClient().getTextualPrefix()).append(event.getClient().getPrefix() == null ? " " : "").append(command.getName())
                        .append(command.getArguments() == null ? "`" : " " + command.getArguments() + "`")
                        .append(" - ").append(command.getHelp());
            }
        }
        return builder.toString();
    }
}

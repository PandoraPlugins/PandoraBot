package dev.minecraftplugin.commands.category;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import dev.minecraftplugin.configuration.BotSettings;
import dev.minecraftplugin.lib.config.Config;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;

import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AdministrativeCategory extends Command.Category {
    public AdministrativeCategory(String noPermission, Config<BotSettings> config) {
        super("Administrative", noPermission, createPredicate(config));
    }


    private static Predicate<CommandEvent> createPredicate(Config<BotSettings> config) {
        return commandEvent -> {
            if (commandEvent.isOwner())
                return true;
            if ((commandEvent.getGuild().getId().equals("727632535185785012")
                    && (commandEvent.getMember().getPermissions().contains(Permission.ADMINISTRATOR) ||
                    commandEvent.getMember().getRoles().stream().map(Role::getName)
                            .collect(Collectors.toList()).contains(config.getConfiguration().administrativeRole))))
                return true;
            return false;
        };
    }
}

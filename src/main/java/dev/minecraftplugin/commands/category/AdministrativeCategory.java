package dev.minecraftplugin.commands.category;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sun.media.jfxmedia.logging.Logger;
import dev.minecraftplugin.configuration.BotSettings;
import dev.minecraftplugin.lib.config.Config;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
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
            Guild pandora = commandEvent.getJDA().getGuildById("727632535185785012");
            if (pandora == null) {
                Logger.logMsg(Logger.ERROR, "Could not find pandora discord! Was I kicked?");
                return false;
            }
            Member m = pandora.getMember(commandEvent.getAuthor());
            System.out.println(pandora.getMembers());
            if (m == null) {
                m = pandora.retrieveMember(commandEvent.getAuthor()).complete();
            }

            if (m == null) {
                return false;
            }
            if (m.getPermissions().contains(Permission.ADMINISTRATOR) || m.getRoles().stream().map(Role::getName)
                    .collect(Collectors.toList()).contains(config.getConfiguration().administrativeRole)) return true;
            return false;
        };
    }
}

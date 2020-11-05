package dev.minecraftplugin.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import dev.minecraftplugin.PandoraBot;

public class ShutdownCommand extends Command {
    private final PandoraBot pandoraBot;

    public ShutdownCommand(PandoraBot pandoraBot, Category category)
    {
        this.category = category;
        this.pandoraBot = pandoraBot;
        this.name = "shutdown";
        this.help = "Bot go brrrrrr";
        this.guildOnly = false;
        this.ownerCommand = true;
    }

    /**
     * The main body method of a {@link Command Command}.
     * <br>This is the "response" for a successful
     * {@link Command#run(CommandEvent) #run(CommandEvent)}.
     *
     * @param event The {@link CommandEvent CommandEvent} that
     *              triggered this Command
     */
    @Override
    protected void execute(CommandEvent event) {
        event.reactSuccess();
        pandoraBot.getBotConfig().saveConfig();
        event.getJDA().shutdown();
        System.exit(0);
    }
}

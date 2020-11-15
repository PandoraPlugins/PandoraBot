package dev.minecraftplugin.commands.announce;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import dev.minecraftplugin.PandoraBot;

public class AnnounceCommand extends Command {
    private final PandoraBot pandoraBot;

    public AnnounceCommand(PandoraBot pandoraBot, Category category) {
        this.category = category;
        this.pandoraBot = pandoraBot;
        this.name = "announce";
        this.guildOnly = true;
        this.help = "Announce shit";
    }

    @Override
    protected void execute(CommandEvent event) {
        AnnounceDialogue dialogue = new AnnounceDialogue(event.getMember().getUser().getId(), event.getTextChannel().getId(), pandoraBot);
        pandoraBot.getDialogueManager().addDialogue(dialogue);
        event.reply(dialogue.getNextQuestion());
    }
}

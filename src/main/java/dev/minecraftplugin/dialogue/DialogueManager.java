package dev.minecraftplugin.dialogue;

import com.sun.media.jfxmedia.logging.Logger;
import dev.minecraftplugin.PandoraBot;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;


public class DialogueManager extends ListenerAdapter {
    private final Map<String, Dialogue> userDialogueMap;
    private final PandoraBot bot;

    public DialogueManager(PandoraBot bot) {
        userDialogueMap = new HashMap<>();
        this.bot = bot;
    }

    public Map<String, Dialogue> getUserDialogueMap() {
        return userDialogueMap;
    }

    public void addDialogue(Dialogue dialogue) {
        userDialogueMap.put(dialogue.getUserID(), dialogue);
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getGuild().getId().equals(bot.getBotConfig().getConfiguration().guildID)) {
            // We get and then check for null, checking if the map contains an item is more "pretty" but takes
            // 2x the processing power (however little that may be) as you're checking doing the same thing below
            // when you call hasKey, and then you get it again.
            Dialogue dialogue = getUserDialogueMap().get(event.getAuthor().getId());
            if (dialogue != null && dialogue.getChannelID().equals(event.getChannel().getId())) {
                // Map has a dialogue of this user.
                if (dialogue.getCancelWord().equals(event.getMessage().getContentDisplay())) {
                    getUserDialogueMap().remove(event.getAuthor().getId());
                    event.getChannel().sendMessage("Cancelled action.").queue();
                    return;
                }
                Question<?> question = dialogue.getCurrentQuestion();
                QuestionEvent<?> questionEvent = new QuestionEvent<>(event, event.getMessage().getContentRaw(), question);
                if (question.isValid(questionEvent)) {
                    question.success(questionEvent);
                    TextChannel channel = event.getJDA().getTextChannelById(question.getDialogue().getChannelID());
                    if (channel != null) {
                        if (dialogue.hasNext())
                            channel.sendMessage(question.getDialogue().getNextQuestion()).queue();
                        else getUserDialogueMap().remove(event.getAuthor().getId());
                    } else Logger.logMsg(Level.WARNING.intValue(), "Could not respond to user for dialogue.");
                } else question.failure(questionEvent);
            }
        }
    }
}

package dev.minecraftplugin.commands.announce;

import dev.minecraftplugin.PandoraBot;
import dev.minecraftplugin.dialogue.Dialogue;
import dev.minecraftplugin.dialogue.Question;
import dev.minecraftplugin.dialogue.QuestionEvent;
import dev.minecraftplugin.dialogue.group.EmbedGroup;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class AnnounceDialogue extends Dialogue {
    private final PandoraBot bot;

    public AnnounceDialogue(String userID, String channelID, PandoraBot bot) {
        super(userID, channelID, "cancel", "Announcement Creator");
        this.bot = bot;
        super.questions.addAll(new EmbedGroup().getQuestions(this));

        questions.add(new Question<TextChannel>(event -> {
            event.getQuestion().setAnswer(getTextChannel(event.getValue(), event));
            sendAnnouncement();
        }, event -> event.getEvent().getChannel().sendMessage("That is not a valid a channel!").queue(),
                "Where would you like to send this announcement?",
                "It can be a channel ID, or a channel mention",
                event -> isTextChannel(event.getValue(), event), this));
    }

    public void sendAnnouncement() {
        TextChannel channel = (TextChannel) questions.get(4).getAnswer();
        Color color = (Color) questions.get(0).getAnswer();
        String author = (String) questions.get(1).getAnswer();
        String title = (String) questions.get(2).getAnswer();
        String description = (String) questions.get(3).getAnswer();
        channel.sendMessage(new EmbedBuilder()
                .setAuthor(author, bot.getBotConfig().getConfiguration().discordInvite,
                        bot.getJda().getSelfUser().getEffectiveAvatarUrl())
                .setColor(color)
                .setTitle(title)
                .setDescription(description)
                .build()).queue();

    }

    private boolean isTextChannel(String string, QuestionEvent<?> event) {
        string = string.replaceAll("[^0-9]", "");
        return event.getEvent().getJDA().getTextChannelById(string) != null;
    }

    private TextChannel getTextChannel(String string, QuestionEvent<?> event) {
        string = string.replaceAll("[^0-9]", "");
        return event.getEvent().getJDA().getTextChannelById(string);
    }
}

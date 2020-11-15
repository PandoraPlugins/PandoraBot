package dev.minecraftplugin.dialogue;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.ArrayList;
import java.util.List;

public class Dialogue {
    protected final String userID;
    protected final String channelID;
    protected final String cancelWord;
    protected final String dialogueName;
    protected int questionID;
    protected List<Question<?>> questions;

    public Dialogue(String userID, String channelID, String cancelWord, String dialogueName) {
        this.userID = userID;
        this.channelID = channelID;
        this.cancelWord = cancelWord;
        // We start at -1 as they have to do getNextQuestion which would bring it to 0, the first question.
        questionID = -1;
        questions = new ArrayList<>();
        this.dialogueName = dialogueName;
    }

    public MessageEmbed getNextQuestion() {
        questionID++;
        Question<?> question = questions.get(questionID);
        return new EmbedBuilder()
                .setAuthor(dialogueName)
                .setTitle(question.getQuestion())
                .setDescription(question.getDescription())
                .build();
    }

    public Question<?> getCurrentQuestion() {
        return questions.get(questionID);
    }

    public String getUserID() {
        return userID;
    }

    public String getChannelID() {
        return channelID;
    }

    public int getQuestionID() {
        return questionID;
    }

    public String getCancelWord() {
        return cancelWord;
    }

    public List<Question<?>> getQuestions() {
        return questions;
    }

    public boolean hasNext() {
        return questionID+1 < questions.size();
    }
}

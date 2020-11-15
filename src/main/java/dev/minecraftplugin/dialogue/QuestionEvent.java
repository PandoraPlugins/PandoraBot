package dev.minecraftplugin.dialogue;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;


public class QuestionEvent<T> {
    private final GuildMessageReceivedEvent event;
    private final String value;
    private final Question<T> question;

    public QuestionEvent(GuildMessageReceivedEvent event, String value, Question<T> question) {
        this.event = event;
        this.value = value;
        this.question = question;
    }

    public GuildMessageReceivedEvent getEvent() {
        return event;
    }

    public String getValue() {
        return value;
    }

    public Question<T> getQuestion() {
        return question;
    }
}

package dev.minecraftplugin.dialogue.group;

import dev.minecraftplugin.dialogue.Dialogue;
import dev.minecraftplugin.dialogue.Question;
import dev.minecraftplugin.dialogue.QuestionGroup;
import dev.minecraftplugin.lib.util.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * A group of questions used to make embeds.
 */
public class EmbedGroup implements QuestionGroup {
    @Override
    public List<Question<?>> getQuestions(Dialogue dialogue) {
        List<Question<?>> questions = new ArrayList<>();
        questions.add(new Question<java.awt.Color>(colorQuestionEvent -> {
            colorQuestionEvent.getQuestion().setAnswer(Color.color(colorQuestionEvent.getValue()));
        },
                colorQuestionEvent ->
                        colorQuestionEvent.getEvent().getChannel().sendMessage("That is not a valid color! Try again!").queue()
                , "What would you like the color of the embed to be?", "Please choose a hex color code",
                colorQuestionEvent -> {
                    try {
                        Color.color(colorQuestionEvent.getValue());
                        return true;
                    } catch (Exception e) {
                        return false;
                    }
                }, dialogue));
        questions.add(new Question<String>(stringQuestionEvent -> {
            stringQuestionEvent.getQuestion().setAnswer(stringQuestionEvent.getValue());
        }, stringQuestionEvent -> stringQuestionEvent.getEvent().getChannel().sendMessage("That is not a valid string! Try again!").queue(),
                "What do you wish for the author field to be?", "Choose anything.", stringQuestionEvent -> true, dialogue));
        questions.add(new Question<String>(stringQuestionEvent -> {
            stringQuestionEvent.getQuestion().setAnswer(stringQuestionEvent.getValue());
        }, stringQuestionEvent -> stringQuestionEvent.getEvent().getChannel().sendMessage("That is not a valid string! Try again!").queue(),
                "What do you wish for the title field to be?", "Choose anything.", stringQuestionEvent -> true, dialogue));
        questions.add(new Question<>(stringQuestionEvent -> {
            stringQuestionEvent.getQuestion().setAnswer(stringQuestionEvent.getValue());
        }, stringQuestionEvent -> stringQuestionEvent.getEvent().getChannel().sendMessage("That is not a valid string! Try again!").queue(),
                "What do you wish for the description field to be?", "Choose anything.", stringQuestionEvent -> true, dialogue));
        return questions;
    }
}

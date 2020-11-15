package dev.minecraftplugin.dialogue;

import java.util.function.Consumer;
import java.util.function.Predicate;


public class Question<T> {
    protected final Consumer<QuestionEvent<T>> success, failure;
    protected final String question;
    protected final String description;
    protected final Predicate<QuestionEvent<T>> isValid;
    protected final Dialogue dialogue;
    protected T answer;

    public Question(Consumer<QuestionEvent<T>> success, Consumer<QuestionEvent<T>> failure, String question, String description, Predicate<QuestionEvent<T>> isValid, Dialogue dialogue) {
        this.success = success;
        this.failure = failure;
        this.question = question;
        this.description = description;
        this.isValid = isValid;
        this.dialogue = dialogue;
    }

    public String getDescription() {
        return description;
    }

    public void success(QuestionEvent<?> event) {
        success.accept((QuestionEvent<T>) event);
    }

    public void failure(QuestionEvent<?> event) {
        failure.accept((QuestionEvent<T>) event);
    }

    public String getQuestion() {
        return question;
    }

    public boolean isValid(QuestionEvent<?> event) {
        return isValid.test((QuestionEvent<T>) event);
    }

    public Dialogue getDialogue() {
        return dialogue;
    }

    public T getAnswer() {
        return answer;
    }

    public void setAnswer(T answer) {
        this.answer = answer;
    }
}

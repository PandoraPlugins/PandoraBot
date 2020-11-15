package dev.minecraftplugin.dialogue;

import java.util.List;

public interface QuestionGroup {
    List<Question<?>> getQuestions(Dialogue dialogue);
}

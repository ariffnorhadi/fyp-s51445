package com.fyp.application.classes.subjectTeacher.topic.questions.choice;

import java.util.Optional;

public interface ChoiceService {
  void save(Choice choice);

  Optional<Choice> findById(Long id);
}

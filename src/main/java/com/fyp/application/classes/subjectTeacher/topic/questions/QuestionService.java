package com.fyp.application.classes.subjectTeacher.topic.questions;

import com.fyp.application.classes.subjectTeacher.topic.Topic;
import com.fyp.application.classes.subjectTeacher.topic.questions.choice.CreateQuestionParameter;

import java.util.List;
import java.util.Optional;

public interface QuestionService {

  List<Question> findByTopic(Topic topic);

  void save(CreateQuestionParameter parameter);

  Optional<Question> getOne(Long id);

  void deleteById(Long id);

}

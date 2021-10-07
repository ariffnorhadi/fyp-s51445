package com.fyp.application.classes.subjectTeacher.topic.questions.answer;

import com.fyp.application.classes.subjectTeacher.topic.questions.Question;
import com.fyp.application.classes.subjectTeacher.topic.questions.choice.Choice;
import com.fyp.application.user.User;

import java.util.List;

public interface AnswerService {

  void saveAnswer(Answer answer);

  void saveAnswer(Choice choice, Question question, User user);

  List<Answer> findByQuestion(Question question);

  boolean existsByQuestion(Question question);
}

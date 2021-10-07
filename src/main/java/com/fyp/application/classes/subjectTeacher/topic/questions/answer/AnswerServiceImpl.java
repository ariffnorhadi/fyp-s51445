package com.fyp.application.classes.subjectTeacher.topic.questions.answer;

import com.fyp.application.classes.subjectTeacher.topic.questions.Question;
import com.fyp.application.classes.subjectTeacher.topic.questions.choice.Choice;
import com.fyp.application.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerServiceImpl implements AnswerService {

  private final AnswerRepository repository;

  public AnswerServiceImpl(AnswerRepository repository) {
    this.repository = repository;
  }

  @Override
  public void saveAnswer(Answer answer) {
    this.repository.save(answer);
  }

  @Override
  public void saveAnswer(Choice choice, Question question, User user) {
    Answer answer = new Answer(choice, question, user);
    this.repository.save(answer);
  }

  @Override
  public List<Answer> findByQuestion(Question question) {
    return this.repository.findByQuestion(question);
  }

  @Override
  public boolean existsByQuestion(Question question) {
    return this.repository.existsByQuestion(question);
  }
}

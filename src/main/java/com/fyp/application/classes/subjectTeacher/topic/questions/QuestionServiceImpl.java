package com.fyp.application.classes.subjectTeacher.topic.questions;

import com.fyp.application.classes.subjectTeacher.topic.Topic;
import com.fyp.application.classes.subjectTeacher.topic.questions.choice.Choice;
import com.fyp.application.classes.subjectTeacher.topic.questions.choice.ChoiceRepository;
import com.fyp.application.classes.subjectTeacher.topic.questions.choice.CreateQuestionParameter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class QuestionServiceImpl implements QuestionService {

  private final QuestionRepository repository;
  private final ChoiceRepository choiceRepository;

  public QuestionServiceImpl(QuestionRepository repository,
                             ChoiceRepository choiceRepository) {
    this.repository = repository;
    this.choiceRepository = choiceRepository;
  }

  @Override
  public List<Question> findByTopic(Topic topic) {
    return repository.findByTopic(topic);
  }

  @Override
  public void save(CreateQuestionParameter parameter) {
    Topic topic = parameter.getTopic();
    String questionName = parameter.getName();
    Question question = new Question();
    question.setName(questionName);
    question.setTopic(topic);
    Choice answer = parameter.getAnswer();
    choiceRepository.save(answer);
    question.setChoice(answer);
    answer.setQuestion(question);
    this.repository.save(question);

    Choice choice1 = new Choice(parameter.getChoice1().getName(), question);
    Choice choice2 = new Choice(parameter.getChoice2().getName(), question);
    Choice choice3 = new Choice(parameter.getChoice3().getName(), question);
    Choice choice4 = new Choice(parameter.getChoice4().getName(), question);
    Set<Choice> choices = new java.util.HashSet<>(Set.of(choice1, choice2, choice3, choice4));
    choices.removeIf(choice -> choice.getName().equals(answer.getName()));
    for (Choice choice : choices) {
      choiceRepository.save(choice);
    }
  }

  @Override
  public Optional<Question> getOne(Long id) {
    return this.repository.findById(id);
  }

  @Override
  public void deleteById(Long id) {
    this.repository.deleteById(id);
  }
}

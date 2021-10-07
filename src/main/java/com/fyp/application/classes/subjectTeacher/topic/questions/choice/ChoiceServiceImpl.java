package com.fyp.application.classes.subjectTeacher.topic.questions.choice;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChoiceServiceImpl implements ChoiceService {

  private final ChoiceRepository choiceRepository;

  public ChoiceServiceImpl(ChoiceRepository choiceRepository) {
    this.choiceRepository = choiceRepository;
  }

  @Override
  public void save(Choice choice) {
    this.choiceRepository.save(choice);
  }

  @Override
  public Optional<Choice> findById(Long id) {
    return this.choiceRepository.findById(id);
  }
}

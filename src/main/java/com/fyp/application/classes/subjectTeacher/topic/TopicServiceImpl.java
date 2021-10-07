package com.fyp.application.classes.subjectTeacher.topic;

import com.fyp.application.classes.subjectTeacher.SubjectTeacher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TopicServiceImpl implements TopicService {

  private final TopicRepository repository;

  public TopicServiceImpl(TopicRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<Topic> findAllBySubjectTeacher(SubjectTeacher subjectTeacher) {
    return this.repository.findAllBySubjectTeacher(subjectTeacher);
  }

  @Override
  public List<Topic> getAll() {
    return this.repository.findAll();
  }

  @Override
  public Optional<Topic> getOne(Long id) {
    return this.repository.findById(id);
  }

  @Override
  public boolean existsByNameAndSubjectTeacher(String name, SubjectTeacher subjectTeacher) {
    return this.repository.existsByNameAndSubjectTeacher(name, subjectTeacher);
  }

  @Override
  public void save(CreateTopicParameters parameters) {
    Topic newTopic = new Topic(parameters.getName(), parameters.getSubjectTeacher());
    this.repository.save(newTopic);

  }
}

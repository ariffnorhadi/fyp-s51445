package com.fyp.application.classes.subjectTeacher.topic.questions.answer.performance;

import com.fyp.application.classes.subjectTeacher.topic.Topic;
import com.fyp.application.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerformanceServiceImpl implements PerformanceService {

  private final PerformanceRepository repository;

  public PerformanceServiceImpl(PerformanceRepository repository) {
    this.repository = repository;
  }

  @Override
  public void savePerformance(Performance performance) {
    this.repository.save(performance);
  }

  @Override
  public List<Performance> findByTopic(Topic topic) {
    return this.repository.findByTopic(topic);
  }

  @Override
  public List<Performance> findByTopicAndStudent(Topic topic, User user) {
    return this.repository.findByTopicAndStudent(topic, user);
  }

  @Override
  public List<Performance> findByStudent(User student) {
    return this.repository.findByStudent(student);
  }

  @Override
  public List<Performance> findByStudentAndTopic(User student, Topic topic) {
    return this.repository.findByStudentAndTopic(student, topic);
  }
}

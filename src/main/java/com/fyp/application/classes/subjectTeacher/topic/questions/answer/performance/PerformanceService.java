package com.fyp.application.classes.subjectTeacher.topic.questions.answer.performance;

import com.fyp.application.classes.subjectTeacher.topic.Topic;
import com.fyp.application.user.User;

import java.util.List;

public interface PerformanceService {

  void savePerformance(Performance performance);

  List<Performance> findByTopic(Topic topic);

  List<Performance> findByTopicAndStudent(Topic topic, User user);

  List<Performance> findByStudent(User student);

  List<Performance> findByStudentAndTopic(User student, Topic topic);
}

package com.fyp.application.classes.subjectTeacher.topic.questions.answer.performance;

import com.fyp.application.classes.subjectTeacher.topic.Topic;
import com.fyp.application.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long>, CrudRepository<Performance, Long> {
  List<Performance> findByTopic(Topic topic);

  List<Performance> findByTopicAndStudent(Topic topic, User user);

  List<Performance> findByStudent(User student);

  List<Performance> findByStudentAndTopic(User student, Topic topic);
}

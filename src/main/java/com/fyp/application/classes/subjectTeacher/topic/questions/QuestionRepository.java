package com.fyp.application.classes.subjectTeacher.topic.questions;

import com.fyp.application.classes.subjectTeacher.topic.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long>, CrudRepository<Question, Long> {

  List<Question> findByTopic(Topic topic);

}

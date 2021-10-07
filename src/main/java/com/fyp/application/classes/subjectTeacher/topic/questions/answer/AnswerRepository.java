package com.fyp.application.classes.subjectTeacher.topic.questions.answer;

import com.fyp.application.classes.subjectTeacher.topic.questions.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long>, CrudRepository<Answer, Long> {

  boolean existsByQuestion(Question question);

  List<Answer> findByQuestion(Question question);


}

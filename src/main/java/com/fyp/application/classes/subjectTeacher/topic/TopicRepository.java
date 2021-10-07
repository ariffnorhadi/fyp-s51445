package com.fyp.application.classes.subjectTeacher.topic;

import com.fyp.application.classes.subjectTeacher.SubjectTeacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long>, CrudRepository<Topic, Long> {

  List<Topic> findAllBySubjectTeacher(SubjectTeacher subjectTeacher);

  boolean existsByNameAndSubjectTeacher(String name, SubjectTeacher subjectTeacher);

}

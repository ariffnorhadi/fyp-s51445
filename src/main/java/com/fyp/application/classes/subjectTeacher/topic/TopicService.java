package com.fyp.application.classes.subjectTeacher.topic;

import com.fyp.application.classes.subjectTeacher.SubjectTeacher;

import java.util.List;
import java.util.Optional;

public interface TopicService {

  List<Topic> findAllBySubjectTeacher(SubjectTeacher subjectTeacher);

  List<Topic> getAll();

  Optional<Topic> getOne(Long id);

  boolean existsByNameAndSubjectTeacher(String name, SubjectTeacher subjectTeacher);

  void save(CreateTopicParameters parameters);
}

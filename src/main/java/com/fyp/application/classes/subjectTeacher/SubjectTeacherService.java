package com.fyp.application.classes.subjectTeacher;

import com.fyp.application.classes.Classes;
import com.fyp.application.subject.Subject;

import java.util.List;
import java.util.Optional;

public interface SubjectTeacherService {

  void save(SubjectTeacher subjectTeacher);

  Optional<SubjectTeacher> getOne(Long id);

  List<SubjectTeacher> findByClasses(Classes classes);

  List<SubjectTeacher> findByClassesAndSubject(Classes classes, Subject subject);

}

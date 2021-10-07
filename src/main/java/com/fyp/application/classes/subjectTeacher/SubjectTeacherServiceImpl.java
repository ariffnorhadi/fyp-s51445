package com.fyp.application.classes.subjectTeacher;

import com.fyp.application.classes.Classes;
import com.fyp.application.subject.Subject;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectTeacherServiceImpl implements SubjectTeacherService {

  private final SubjectTeacherRepository repository;

  public SubjectTeacherServiceImpl(SubjectTeacherRepository repository) {
    this.repository = repository;
  }

  @Override
  public void save(SubjectTeacher subjectTeacher) {
    this.repository.save(subjectTeacher);
  }

  @Override
  public Optional<SubjectTeacher> getOne(Long id) {
    return this.repository.findById(id);
  }

  @Override
  public List<SubjectTeacher> findByClasses(Classes classes) {
    return this.repository.findByClasses(classes);
  }

  @Override
  public List<SubjectTeacher> findByClassesAndSubject(Classes classes, Subject subject) {
    return this.repository.findByClassesAndSubject(classes, subject);
  }
}

package com.fyp.application.subject;

import com.fyp.application.school.School;

import java.util.List;
import java.util.Optional;

public interface SubjectService {

  List<Subject> findBySchool(School school);

  void save(CreateSubjectParameter parameter);

  Optional<Subject> getOne(Long id);
}

package com.fyp.application.classes;

import com.fyp.application.school.School;
import com.fyp.application.user.User;

import java.util.List;
import java.util.Optional;

// TODO: CREATE class
public interface ClassService {

  boolean existsByNameAndSchool(String name, School school);

  List<Classes> findBySchool(School school);

  void saveClass(CreateClassParameters parameters);

  void saveClass(Classes classes, User assignedTeacher);

  Optional<Classes> getOne(Long id);
}

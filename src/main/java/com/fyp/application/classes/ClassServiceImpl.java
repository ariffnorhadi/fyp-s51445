package com.fyp.application.classes;

import com.fyp.application.school.School;
import com.fyp.application.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassServiceImpl implements ClassService {

  private final ClassRepository repository;

  public ClassServiceImpl(ClassRepository repository) {
    this.repository = repository;
  }

  @Override
  public boolean existsByNameAndSchool(String name, School school) {
    return this.repository.existsByNameAndSchool(name, school);
  }

  @Override
  public List<Classes> findBySchool(School school) {
    return this.repository.findBySchool(school);
  }

  @Override
  public void saveClass(CreateClassParameters parameters) {
    Classes newClass =
        new Classes(1L, parameters.getName(), parameters.getSchool());
    this.repository.save(newClass);
  }

  @Override
  public void saveClass(Classes classes, User assignedTeacher) {
    classes.setTeacher(assignedTeacher);
    this.repository.save(classes);

  }

  @Override
  public Optional<Classes> getOne(Long id) {
    return this.repository.findById(id);
  }
}

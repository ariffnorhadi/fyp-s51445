package com.fyp.application.subject;

import com.fyp.application.school.School;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectServiceImpl implements SubjectService {

  private final SubjectRepository repository;

  public SubjectServiceImpl(SubjectRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<Subject> findBySchool(School school) {
    return this.repository.findBySchool(school);
  }

  @Override
  public void save(CreateSubjectParameter parameter) {
    Subject subject = new Subject(parameter.getName(), parameter.getSchool());
    this.repository.save(subject);
  }

  @Override
  public Optional<Subject> getOne(Long id) {
    return this.repository.findById(id);
  }
}

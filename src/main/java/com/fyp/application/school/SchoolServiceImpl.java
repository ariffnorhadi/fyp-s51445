package com.fyp.application.school;

import com.fyp.application.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SchoolServiceImpl implements SchoolService {

  private final SchoolRepository repository;

  public SchoolServiceImpl(SchoolRepository repository) {
    this.repository = repository;
  }

  @Override
  public void saveSchool(School school) {
    this.repository.save(school);
  }

  @Override
  public void saveSchool(CreateSchoolParameters parameters) {
    School school = new School(1L, parameters.getName(),
        parameters.getDescription(), parameters.getAddress(),
        parameters.getCreatedBy(), parameters.getCreatedOn(),
        parameters.getCreatedBy());

    school.setCreatedOn(LocalDate.now());

    this.repository.save(school);
  }

  @Override
  public void editSchool(Long schoolId, EditSchoolParameters parameters) {

  }

  @Override
  public Optional<School> getOne(Long schoolId) {
    return this.repository.findById(schoolId);
  }

  @Override
  public List<School> SCHOOL_LIST() {
    return this.repository.findAll();
  }

  @Override
  public Page<School> getAllSchoolsWithPage(Pageable pageable) {
    return this.repository.findAll(pageable);
  }

  @Override
  public List<School> findSchoolsByPrincipal(User principal) {
    return this.repository.findSchoolsByPrincipal(principal);
  }

  @Override
  public School findSchoolByPeople(User user) {
    return this.repository.findSchoolByPeople(user);
  }

}

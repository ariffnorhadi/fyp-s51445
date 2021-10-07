package com.fyp.application.classes.classRegistration;

import com.fyp.application.classes.Classes;
import com.fyp.application.user.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ClassRegistrationImpl implements ClassRegistrationService {

  private final ClassRegistrationRepository repository;

  public ClassRegistrationImpl(ClassRegistrationRepository repository) {
    this.repository = repository;
  }

  @Override
  public void save(ClassRegistration registration) {
    registration.setVerification_date(LocalDate.now());
    this.repository.save(registration);
  }

  @Override
  public List<ClassRegistration> findByApplicant(User applicant) {
    return this.repository.findByApplicant(applicant);
  }

  @Override
  public Optional<ClassRegistration> getOne(Long id) {
    return this.repository.findById(id);
  }

  @Override
  public List<ClassRegistration> findByApplicantAndStatus(User applicant, String status) {
    return this.repository.findByApplicantAndStatus(applicant, status);
  }

  @Override
  public List<ClassRegistration> findByClasses(Classes classes) {
    return this.repository.findByClasses(classes);
  }

  @Override
  public List<ClassRegistration> findByClassesAndStatus(Classes classes, String status) {
    return this.repository.findByClassesAndStatus(classes, status);
  }
}

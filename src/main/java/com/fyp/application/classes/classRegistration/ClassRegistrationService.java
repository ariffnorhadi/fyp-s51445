package com.fyp.application.classes.classRegistration;

import com.fyp.application.classes.Classes;
import com.fyp.application.user.User;

import java.util.List;
import java.util.Optional;

public interface ClassRegistrationService {

  void save(ClassRegistration registration);

  List<ClassRegistration> findByApplicant(User applicant);

  Optional<ClassRegistration> getOne(Long id);

  List<ClassRegistration> findByApplicantAndStatus(User applicant, String status);

  List<ClassRegistration> findByClasses(Classes classes);

  List<ClassRegistration> findByClassesAndStatus(Classes classes, String status);
}

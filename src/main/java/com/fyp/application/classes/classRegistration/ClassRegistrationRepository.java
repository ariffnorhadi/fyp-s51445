package com.fyp.application.classes.classRegistration;

import com.fyp.application.classes.Classes;
import com.fyp.application.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRegistrationRepository extends JpaRepository<ClassRegistration, Long>,
    CrudRepository<ClassRegistration, Long> {

  List<ClassRegistration> findByApplicant(User applicant);

  List<ClassRegistration> findByApplicantAndStatus(User applicant, String status);

  List<ClassRegistration> findByClasses(Classes classes);

  List<ClassRegistration> findByClassesAndStatus(Classes classes, String status);
}

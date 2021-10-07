package com.fyp.application.school.schoolRegistration;

import com.fyp.application.school.School;
import com.fyp.application.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolRegistrationRepository extends JpaRepository<SchoolRegistration, Long>,
    CrudRepository<SchoolRegistration, Long> {

  boolean existsByApplicantAndSchool(User applicant, School school);

  boolean existsByApplicantAndSchoolAndStatus(User applicant, School school, String status);

  SchoolRegistration findSchoolRegistrationsBySchool(School school);

  SchoolRegistration findSchoolRegistrationsByStatus(String status);

  SchoolRegistration findSchoolRegistrationByApplicantAndStatus(User applicant, String status);

  List<SchoolRegistration> findSchoolRegistrationsByApplicant(User applicant);

  List<SchoolRegistration> findSchoolRegistrationsBySchoolAndStatus(School school, String status);

  List<SchoolRegistration> findSchoolRegistrationsByApplicantAndStatus(User applicant, String status);
}

package com.fyp.application.school.schoolRegistration;

import com.fyp.application.role.Role;
import com.fyp.application.school.School;
import com.fyp.application.user.User;

import java.util.List;
import java.util.Optional;

public interface SchoolRegistrationService {

  Optional<SchoolRegistration> getOne(Long id);

  List<SchoolRegistration> SCHOOL_REGISTRATION_LIST();

  void saveRequest(User applicant, School school, Role role);

  void approveRequest(SchoolRegistration request, User approvedBy);

  void approveAllRequestsBySchool(School school, User approvedBy);

  void rejectRequest(SchoolRegistration request, User rejectedBy);

  boolean existsByApplicantAndSchool(User applicant, School school);

  boolean existsByApplicantAndSchoolAndStatus(User applicant, School school, String status);

  SchoolRegistration findSchoolRegistrationsBySchool(School school);

  SchoolRegistration findSchoolRegistrationsByStatus(String status);

  SchoolRegistration findSchoolRegistrationByApplicantAndStatus(User applicant, String status);

  List<SchoolRegistration> findSchoolRegistrationsByApplicant(User applicant);

  List<SchoolRegistration> findSchoolRegistrationsBySchoolAndStatus(School school, String status);

  List<SchoolRegistration> findSchoolRegistrationsByApplicantAndStatus(User applicant, String status);
}

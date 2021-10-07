package com.fyp.application.school.schoolRegistration;

import com.fyp.application.role.Role;
import com.fyp.application.school.School;
import com.fyp.application.user.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class SchoolRegistrationServiceImpl implements SchoolRegistrationService {

  private final SchoolRegistrationRepository repository;

  public SchoolRegistrationServiceImpl(SchoolRegistrationRepository repository) {
    this.repository = repository;
  }

  @Override
  public Optional<SchoolRegistration> getOne(Long id) {
    return this.repository.findById(id);
  }

  @Override
  public List<SchoolRegistration> SCHOOL_REGISTRATION_LIST() {
    return this.repository.findAll();
  }

  @Override
  public void saveRequest(User applicant, School school, Role role) {
    SchoolRegistration registration = new SchoolRegistration();
    registration.setApplicant(applicant);
    registration.setSchool(school);
    registration.setAppliedPost(role);
    registration.setStatus(Status.PENDING.name());
    registration.setApplicationDateTime(LocalDateTime.now());

    this.repository.save(registration);
  }

  @Override
  public void approveRequest(SchoolRegistration approvedRequest, User approvedBy) {
    approvedRequest.setStatus(Status.APPROVED.name());
    approvedRequest.setVerification_date(LocalDate.now());
    approvedRequest.setVerified_by(approvedBy.getFullName());

    User applicant = approvedRequest.getApplicant();
    Set<Role> applicantRoles = applicant.getRoles();
    Role approvedRole = approvedRequest.getAppliedPost();
    applicantRoles.add(approvedRole);
    applicant.setRoles(applicantRoles);
    applicant.setSchool(approvedRequest.getSchool());

    this.repository.save(approvedRequest);

    List<SchoolRegistration> otherRequests =
        this.findSchoolRegistrationsByApplicant(approvedRequest.getApplicant());
    otherRequests.remove(approvedRequest);
    for (SchoolRegistration requests : otherRequests) {
      requests.setStatus(Status.INVALID.name());
      requests.setVerification_date(LocalDate.now());

      this.repository.save(requests);
    }
            /*
            TODO: Figure out org.springframework.dao.InvalidDataAccessApiUsageException
            this.repository.deleteAllByApplicant(request.getApplicant());
             */
  }

  /*
  TODO: Implement front end for this method
   */
  @Override
  public void approveAllRequestsBySchool(School school, User approvedBy) {
    List<SchoolRegistration> pendingRequests =
        this.repository.findSchoolRegistrationsBySchoolAndStatus(school, Status.PENDING.name());
    for (SchoolRegistration approvedRequest : pendingRequests) {
      User applicant = approvedRequest.getApplicant();

      approvedRequest.setStatus(Status.APPROVED.name());
      approvedRequest.setVerification_date(LocalDate.now());
      approvedRequest.setVerified_by(approvedBy.getFullName());

      School approvedSchool = approvedRequest.getSchool();
      applicant.setSchool(approvedSchool);

      Set<Role> applicantRoles = applicant.getRoles();
      Role approvedRole = approvedRequest.getAppliedPost();
      applicantRoles.add(approvedRole);
      applicant.setRoles(applicantRoles);

      this.repository.save(approvedRequest);

      List<SchoolRegistration> otherApplicantRequests =
          this.repository.findSchoolRegistrationsByApplicant(applicant);
      otherApplicantRequests.remove(approvedRequest);
      for (SchoolRegistration otherRequest : otherApplicantRequests) {
        otherRequest.setStatus(Status.INVALID.name());
        otherRequest.setVerification_date(LocalDate.now());
        otherRequest.setVerified_by(approvedBy.getFullName());
        this.repository.save(otherRequest);
      }
    }
  }

  @Override
  public void rejectRequest(SchoolRegistration rejectedRequest, User rejectedBy) {
    rejectedRequest.setStatus(Status.REJECTED.name());
    rejectedRequest.setVerification_date(LocalDate.now());
    rejectedRequest.setVerified_by(rejectedBy.getFullName());

    this.repository.save(rejectedRequest);
  }

  @Override
  public boolean existsByApplicantAndSchool(User applicant, School school) {
    return this.repository.existsByApplicantAndSchool(applicant, school);
  }

  @Override
  public boolean existsByApplicantAndSchoolAndStatus(
      User applicant, School school, String status) {
    return this.repository
        .existsByApplicantAndSchoolAndStatus(applicant, school, status);
  }

  @Override
  public SchoolRegistration findSchoolRegistrationsBySchool(School school) {
    return this.repository.findSchoolRegistrationsBySchool(school);
  }

  @Override
  public SchoolRegistration findSchoolRegistrationsByStatus(String status) {
    return this.repository.findSchoolRegistrationsByStatus(status);
  }

  @Override
  public SchoolRegistration findSchoolRegistrationByApplicantAndStatus(User applicant, String status) {
    return this.repository.findSchoolRegistrationByApplicantAndStatus(applicant, status);
  }

  @Override
  public List<SchoolRegistration> findSchoolRegistrationsByApplicant(User applicant) {
    return this.repository.findSchoolRegistrationsByApplicant(applicant);
  }

  @Override
  public List<SchoolRegistration> findSchoolRegistrationsBySchoolAndStatus(
      School school, String status) {
    return this.repository
        .findSchoolRegistrationsBySchoolAndStatus(school, status);
  }

  @Override
  public List<SchoolRegistration> findSchoolRegistrationsByApplicantAndStatus(
      User applicant, String status) {
    return this.repository
        .findSchoolRegistrationsByApplicantAndStatus(applicant, status);
  }
}

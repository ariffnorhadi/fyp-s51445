package com.fyp.application.school.schoolRegistration;

import com.fyp.application.role.Role;
import com.fyp.application.school.School;
import com.fyp.application.user.User;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "school_registrations")
public class SchoolRegistration {

  @Id
  @SequenceGenerator(
      name = "school_reg_sequence",
      sequenceName = "school_reg_sequence",
      allocationSize = 1
  )
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "school_reg_sequence"
  )
  @Column(name = "school_reg_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "applicant_id", nullable = false)
  private User applicant;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "school_id", nullable = false)
  private School school;

  private String applicationDateTime;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "role_id", nullable = false)
  private Role appliedPost;

  // verified_by = put username of the person
  private String verified_by;

  private String status;
  private LocalDate verification_date;

  public SchoolRegistration() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getApplicant() {
    return applicant;
  }

  public void setApplicant(User applicant) {
    this.applicant = applicant;
  }

  public School getSchool() {
    return school;
  }

  public void setSchool(School school) {
    this.school = school;
  }

  public String getVerified_by() {
    return verified_by;
  }

  public void setVerified_by(String verified_by) {
    this.verified_by = verified_by;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public LocalDate getVerification_date() {
    return verification_date;
  }

  public void setVerification_date(LocalDate verification_date) {
    this.verification_date = verification_date;
  }

  public String getApplicationDateTime() {
    return applicationDateTime;
  }

  public void setApplicationDateTime(LocalDateTime applicationDateTime) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    this.applicationDateTime = applicationDateTime.format(formatter);
  }

  public Role getAppliedPost() {
    return appliedPost;
  }

  public void setAppliedPost(Role appliedPost) {
    this.appliedPost = appliedPost;
  }
}

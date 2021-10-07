package com.fyp.application.classes.classRegistration;

import com.fyp.application.classes.Classes;
import com.fyp.application.user.User;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "class_registrations")
public class ClassRegistration {

  @Id
  @SequenceGenerator(
      name = "class_reg_sequence",
      sequenceName = "class_reg_sequence",
      allocationSize = 1
  )
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "class_reg_sequence"
  )
  @Column(name = "class_reg_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "applicant_id", nullable = false)
  private User applicant;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "class_id", nullable = false)
  private Classes classes;

  private String applicationDateTime;

  private String status;

  private LocalDate verification_date;

  public ClassRegistration() {
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

  public Classes getClasses() {
    return classes;
  }

  public void setClasses(Classes classes) {
    this.classes = classes;
  }

  public String getApplicationDateTime() {
    return applicationDateTime;
  }

  public void setApplicationDateTime(LocalDateTime applicationDateTime) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    this.applicationDateTime = applicationDateTime.format(formatter);
  }

  public void setApplicationDateTime(String applicationDateTime) {
    this.applicationDateTime = applicationDateTime;
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
}

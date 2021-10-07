package com.fyp.application.school;

import com.fyp.application.classes.Classes;
import com.fyp.application.school.schoolRegistration.SchoolRegistration;
import com.fyp.application.subject.Subject;
import com.fyp.application.user.User;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "schools")
public class School {

  @Id
  @SequenceGenerator(
      name = "school_sequence",
      sequenceName = "school_sequence",
      allocationSize = 1
  )
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "school_sequence"
  )
  @Column(name = "school_id")
  private Long id;

  private Long version;
  private String name;
  private String description;
  private String address;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "creater_id", nullable = false)
  private User createdBy;
  private LocalDate createdOn;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owner_id")
  private User principal;

  @OneToMany(mappedBy = "school",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL)
  private Set<SchoolRegistration> schoolRegistrations;

  @OneToMany(mappedBy = "school",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL)
  private Set<Classes> classes;

  @OneToMany(cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      mappedBy = "school")
  private Set<Subject> subjects;

  @OneToMany(mappedBy = "school",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL)
  private Set<User> people;

  public School() {
  }

  public School(Long version, String name,
                String description, String address,
                User createdBy, LocalDate createdOn,
                User principal) {
    this.version = version;
    this.name = name;
    this.description = description;
    this.address = address;
    this.createdBy = createdBy;
    this.createdOn = createdOn;
    this.principal = principal;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public User getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(User createdBy) {
    this.createdBy = createdBy;
  }

  public LocalDate getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(LocalDate createdOn) {
    this.createdOn = createdOn;
  }

  public User getPrincipal() {
    return principal;
  }

  public void setPrincipal(User principal) {
    this.principal = principal;
  }

  public Set<SchoolRegistration> getSchoolRegistrations() {
    return schoolRegistrations;
  }

  public void setSchoolRegistrations(Set<SchoolRegistration> schoolRegistrations) {
    this.schoolRegistrations = schoolRegistrations;
  }

  public Set<User> getPeople() {
    return people;
  }

  public void setPeople(Set<User> students) {
    this.people = students;
  }

  public Set<Classes> getClasses() {
    return classes;
  }

  public void setClasses(Set<Classes> classes) {
    this.classes = classes;
  }

  public Set<Subject> getSubjects() {
    return subjects;
  }

  public void setSubjects(Set<Subject> subjects) {
    this.subjects = subjects;
  }
}

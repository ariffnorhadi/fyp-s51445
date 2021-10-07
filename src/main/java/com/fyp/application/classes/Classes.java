package com.fyp.application.classes;

import com.fyp.application.classes.classRegistration.ClassRegistration;
import com.fyp.application.classes.subjectTeacher.SubjectTeacher;
import com.fyp.application.school.School;
import com.fyp.application.user.User;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "classes")
public class Classes {

  @Id
  @SequenceGenerator(
      name = "class_sequence",
      sequenceName = "class_sequence",
      allocationSize = 1
  )
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "class_sequence"
  )
  @Column(name = "class_id")
  private Long id;
  private Long version;

  private String name;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "school_id", nullable = false)
  private School school;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "teacher_id")
  private User teacher;

  @OneToMany(mappedBy = "classes",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL)
  private Set<User> students;

  @OneToMany(mappedBy = "classes",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL)
  private Set<ClassRegistration> classRegistrations;

  // TODO: find doc on cascade in Spring Boot
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "classes")
  private Set<SubjectTeacher> subjectTeachers;

  public Classes() {
  }

  public Classes(Long version, String name, School school) {
    this.version = version;
    this.name = name;
    this.school = school;
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

  public School getSchool() {
    return school;
  }

  public void setSchool(School school) {
    this.school = school;
  }

  public User getTeacher() {
    return teacher;
  }

  public void setTeacher(User teacher) {
    this.teacher = teacher;
  }

  public Set<User> getStudents() {
    return students;
  }

  public void setStudents(Set<User> students) {
    this.students = students;
  }

  public Set<ClassRegistration> getClassRegistrations() {
    return classRegistrations;
  }

  public void setClassRegistrations(Set<ClassRegistration> classRegistrations) {
    this.classRegistrations = classRegistrations;
  }

  public Set<SubjectTeacher> getSubjectTeachers() {
    return subjectTeachers;
  }

  public void setSubjectTeachers(Set<SubjectTeacher> subjectTeachers) {
    this.subjectTeachers = subjectTeachers;
  }
}

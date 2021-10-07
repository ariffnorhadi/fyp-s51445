package com.fyp.application.subject;

import com.fyp.application.classes.subjectTeacher.SubjectTeacher;
import com.fyp.application.school.School;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "subjects")
public class Subject {

  @Id
  @SequenceGenerator(
      name = "subject_sequence",
      sequenceName = "subject_sequence",
      allocationSize = 1
  )
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "subject_sequence"
  )
  @Column(name = "subject_id")
  private Long id;

  private String name;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "school_id")
  private School school;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "subject")
  private Set<SubjectTeacher> subjectTeacherSet;

  public Subject() {
  }

  public Subject(String name, School school) {
    this.name = name;
    this.school = school;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public Set<SubjectTeacher> getSubjectTeacherSet() {
    return subjectTeacherSet;
  }

  public void setSubjectTeacherSet(Set<SubjectTeacher> subjectTeacherSet) {
    this.subjectTeacherSet = subjectTeacherSet;
  }
}

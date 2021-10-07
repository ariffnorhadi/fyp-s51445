package com.fyp.application.classes.subjectTeacher.topic;

import com.fyp.application.classes.subjectTeacher.SubjectTeacher;
import com.fyp.application.classes.subjectTeacher.topic.questions.Question;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "topics")
public class Topic {

  @Id
  @SequenceGenerator(
      name = "topic_sequence",
      sequenceName = "topic_sequence",
      allocationSize = 1
  )
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "topic_sequence"
  )
  @Column(name = "topic_id")
  private Long id;

  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "subject_teacher_id")
  private SubjectTeacher subjectTeacher;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "topic")
  private Set<Question> questions;

  public Topic() {
  }

  public Topic(String name) {
    this.name = name;
  }

  public Topic(String name, SubjectTeacher subjectTeacher) {
    this.name = name;
    this.subjectTeacher = subjectTeacher;
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

  public SubjectTeacher getSubjectTeacher() {
    return subjectTeacher;
  }

  public void setSubjectTeacher(SubjectTeacher subjectTeacher) {
    this.subjectTeacher = subjectTeacher;
  }

  public Set<Question> getQuestions() {
    return questions;
  }

  public void setQuestions(Set<Question> questions) {
    this.questions = questions;
  }
}

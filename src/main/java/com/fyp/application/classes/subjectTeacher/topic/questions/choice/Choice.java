package com.fyp.application.classes.subjectTeacher.topic.questions.choice;

import com.fyp.application.classes.subjectTeacher.topic.questions.Question;

import javax.persistence.*;

@Entity
@Table(name = "choices")
public class Choice {

  @Id
  @SequenceGenerator(
      name = "choice_seq",
      sequenceName = "choice_seq",
      allocationSize = 1
  )
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "choice_seq"
  )
  @Column(name = "choice_id")
  private Long id;

  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "question_id")
  private Question question;

  public Choice() {
  }

  public Choice(String name) {
    this.name = name;
  }

  public Choice(String name, Question question) {
    this.name = name;
    this.question = question;
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

  public Question getQuestion() {
    return question;
  }

  public void setQuestion(Question question) {
    this.question = question;
  }
}

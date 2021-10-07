package com.fyp.application.classes.subjectTeacher.topic.questions.answer.performance;

import com.fyp.application.classes.subjectTeacher.topic.Topic;
import com.fyp.application.user.User;

import javax.persistence.*;

@Entity
@Table(name = "performances")
public class Performance {

  @Id
  @SequenceGenerator(
      name = "pef_seq",
      sequenceName = "pef_seq",
      allocationSize = 1
  )
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "pef_seq"
  )
  @Column(name = "performance_id")
  private Long id;

  private Long attempt;

  private Long correct;

  private Long incorrect;

  private Double percentage;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "student_id")
  private User student;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "topic_id")
  private Topic topic;

  public Performance() {
  }

  public Performance(Long attempt, Long correct, Long incorrect,
                     Double percentage, User student, Topic topic) {
    this.attempt = attempt;
    this.correct = correct;
    this.incorrect = incorrect;
    this.percentage = percentage;
    this.student = student;
    this.topic = topic;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getAttempt() {
    return attempt;
  }

  public void setAttempt(Long attempt) {
    this.attempt = attempt;
  }

  public Long getCorrect() {
    return correct;
  }

  public void setCorrect(Long correct) {
    this.correct = correct;
  }

  public Long getIncorrect() {
    return incorrect;
  }

  public void setIncorrect(Long incorrect) {
    this.incorrect = incorrect;
  }

  public Double getPercentage() {
    return percentage;
  }

  public void setPercentage(Double percentage) {
    this.percentage = percentage;
  }

  public User getStudent() {
    return student;
  }

  public void setStudent(User student) {
    this.student = student;
  }

  public Topic getTopic() {
    return topic;
  }

  public void setTopic(Topic topic) {
    this.topic = topic;
  }
}

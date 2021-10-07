package com.fyp.application.classes.subjectTeacher.topic.questions.answer;

import com.fyp.application.classes.subjectTeacher.topic.questions.Question;
import com.fyp.application.classes.subjectTeacher.topic.questions.choice.Choice;
import com.fyp.application.user.User;

import javax.persistence.*;

@Entity
@Table(name = "answers")
public class Answer {

  @Id
  @SequenceGenerator(
      name = "answer_seq",
      sequenceName = "answer_seq",
      allocationSize = 1
  )
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "answer_seq"
  )
  @Column(name = "id")
  private Long id;

  private Long attempt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "answer_id")
  private Choice answer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "question_id")
  private Question question;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "student_id")
  private User student;

  public Answer() {
  }

  public Answer(Choice answer, Question question, User student) {
    this.answer = answer;
    this.question = question;
    this.student = student;
  }

  public Answer(Long attempt, Choice answer, Question question, User student) {
    this.attempt = attempt;
    this.answer = answer;
    this.question = question;
    this.student = student;
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

  public Choice getAnswer() {
    return answer;
  }

  public void setAnswer(Choice answer) {
    this.answer = answer;
  }

  public Question getQuestion() {
    return question;
  }

  public void setQuestion(Question question) {
    this.question = question;
  }

  public User getStudent() {
    return student;
  }

  public void setStudent(User student) {
    this.student = student;
  }
}

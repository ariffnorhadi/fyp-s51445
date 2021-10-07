package com.fyp.application.classes.subjectTeacher.topic.questions;

import com.fyp.application.classes.subjectTeacher.topic.Topic;
import com.fyp.application.classes.subjectTeacher.topic.questions.answer.Answer;
import com.fyp.application.classes.subjectTeacher.topic.questions.choice.Choice;

import javax.persistence.*;
import java.util.Set;

/*
TODO: Figure out how to conveniently create questions and answers for quizzes
 */
@Entity
@Table(name = "questions")
public class Question {

  @Id
  @SequenceGenerator(
      name = "question_seq",
      sequenceName = "question_seq",
      allocationSize = 1
  )
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "question_seq"
  )
  @Column(name = "question_id")
  private Long id;

  private String name;

  @OneToOne(fetch = FetchType.LAZY, optional = true)
  @JoinColumn(name = "answer_id", nullable = true)
  private Choice choice;

  @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Choice> choices;

  @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Answer> answers;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "topic_id")
  private Topic topic;

  public Question() {
  }

  public Question(String name, Choice choice, Topic topic) {
    this.name = name;
    this.choice = choice;
    this.topic = topic;
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

  public Choice getChoice() {
    return choice;
  }

  public void setChoice(Choice choice) {
    this.choice = choice;
  }

  public Topic getTopic() {
    return topic;
  }

  public void setTopic(Topic topic) {
    this.topic = topic;
  }

  public Set<Choice> getChoices() {
    return choices;
  }

  public void setChoices(Set<Choice> choices) {
    this.choices = choices;
  }

  public Set<Answer> getAnswers() {
    return answers;
  }

  public void setAnswers(Set<Answer> answers) {
    this.answers = answers;
  }
}

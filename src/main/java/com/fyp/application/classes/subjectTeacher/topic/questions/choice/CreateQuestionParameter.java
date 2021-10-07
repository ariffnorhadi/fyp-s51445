package com.fyp.application.classes.subjectTeacher.topic.questions.choice;

import com.fyp.application.classes.subjectTeacher.topic.Topic;

public class CreateQuestionParameter {

  private String name;
  private Choice choice1;
  private Choice choice2;
  private Choice choice3;
  private Choice choice4;
  private Choice answer;

  private Topic topic;

  public CreateQuestionParameter(String name, Choice choice1, Choice choice2,
                                 Choice choice3, Choice choice4, Choice answer, Topic topic) {
    this.name = name;
    this.choice1 = choice1;
    this.choice2 = choice2;
    this.choice3 = choice3;
    this.choice4 = choice4;
    this.answer = answer;
    this.topic = topic;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Choice getChoice1() {
    return choice1;
  }

  public void setChoice1(Choice choice1) {
    this.choice1 = choice1;
  }

  public Choice getChoice2() {
    return choice2;
  }

  public void setChoice2(Choice choice2) {
    this.choice2 = choice2;
  }

  public Choice getChoice3() {
    return choice3;
  }

  public void setChoice3(Choice choice3) {
    this.choice3 = choice3;
  }

  public Choice getChoice4() {
    return choice4;
  }

  public void setChoice4(Choice choice4) {
    this.choice4 = choice4;
  }

  public Choice getAnswer() {
    return answer;
  }

  public void setAnswer(Choice answer) {
    this.answer = answer;
  }

  public Topic getTopic() {
    return topic;
  }

  public void setTopic(Topic topic) {
    this.topic = topic;
  }
}

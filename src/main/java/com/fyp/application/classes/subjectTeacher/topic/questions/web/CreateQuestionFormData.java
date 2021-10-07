package com.fyp.application.classes.subjectTeacher.topic.questions.web;

import com.fyp.application.classes.subjectTeacher.topic.Topic;
import com.fyp.application.classes.subjectTeacher.topic.TopicService;
import com.fyp.application.classes.subjectTeacher.topic.questions.choice.Choice;
import com.fyp.application.classes.subjectTeacher.topic.questions.choice.CreateQuestionParameter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@NotEmptyChoiceAndAnswer
public class CreateQuestionFormData {

  @NotBlank
  private String name;
  private String choice1;
  private String choice2;
  private String choice3;
  private String choice4;
  @NotNull
  private String answer;
  private Long topicID;

  public CreateQuestionFormData() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getChoice1() {
    return choice1;
  }

  public void setChoice1(String choice1) {
    this.choice1 = choice1;
  }

  public String getChoice2() {
    return choice2;
  }

  public void setChoice2(String choice2) {
    this.choice2 = choice2;
  }

  public String getChoice3() {
    return choice3;
  }

  public void setChoice3(String choice3) {
    this.choice3 = choice3;
  }

  public String getChoice4() {
    return choice4;
  }

  public void setChoice4(String choice4) {
    this.choice4 = choice4;
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  public Long getTopicID() {
    return topicID;
  }

  public void setTopicID(Long topicID) {
    this.topicID = topicID;
  }

  public CreateQuestionParameter toParameters(TopicService topicService) {

    Optional<Topic> optionalTopic = topicService.getOne(topicID);
    if (optionalTopic.isPresent()) {

      Choice choiceOne = new Choice(choice1);
      Choice choiceTwo = new Choice(choice2);
      Choice choiceThree = new Choice(choice3);
      Choice choiceFour = new Choice(choice4);
      Choice answer = new Choice();
      switch (this.answer) {
        case "A":
          answer = choiceOne;
          break;
        case "B":
          answer = choiceTwo;
          break;
        case "C":
          answer = choiceThree;
          break;
        case "D":
          answer = choiceFour;
          break;
      }
      Topic topic = optionalTopic.get();
      return new CreateQuestionParameter(name, choiceOne, choiceTwo,
          choiceThree, choiceFour, answer, topic);
    } else {
      return null;
    }

  }
}

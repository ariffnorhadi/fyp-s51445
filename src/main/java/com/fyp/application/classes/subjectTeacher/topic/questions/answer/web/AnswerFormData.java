package com.fyp.application.classes.subjectTeacher.topic.questions.answer.web;

import com.fyp.application.classes.subjectTeacher.topic.questions.choice.Choice;

import java.util.List;

public class AnswerFormData {

  public List<Choice> choiceList;

  public AnswerFormData() {
  }

  public List<Choice> getChoiceList() {
    return choiceList;
  }

  public void setChoiceList(List<Choice> choiceList) {
    this.choiceList = choiceList;
  }
}

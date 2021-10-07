package com.fyp.application.subject;

import com.fyp.application.school.School;

public class CreateSubjectParameter {

  private String name;
  private School school;

  public CreateSubjectParameter(String name, School school) {
    this.name = name;
    this.school = school;
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
}

package com.fyp.application.classes;

import com.fyp.application.school.School;

public class CreateClassParameters {

  private String name;
  private School school;

  public CreateClassParameters(String name, School school) {
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

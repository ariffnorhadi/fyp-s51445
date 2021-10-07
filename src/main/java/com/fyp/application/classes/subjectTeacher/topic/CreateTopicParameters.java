package com.fyp.application.classes.subjectTeacher.topic;

import com.fyp.application.classes.subjectTeacher.SubjectTeacher;

public class CreateTopicParameters {

  private String name;

  private SubjectTeacher subjectTeacher;

  public CreateTopicParameters(String name, SubjectTeacher subjectTeacher) {
    this.name = name;
    this.subjectTeacher = subjectTeacher;
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
}

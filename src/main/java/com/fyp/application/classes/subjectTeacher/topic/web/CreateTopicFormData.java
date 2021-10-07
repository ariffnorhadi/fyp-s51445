package com.fyp.application.classes.subjectTeacher.topic.web;

import com.fyp.application.classes.subjectTeacher.SubjectTeacher;
import com.fyp.application.classes.subjectTeacher.SubjectTeacherService;
import com.fyp.application.classes.subjectTeacher.topic.CreateTopicParameters;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@NotExistingTopic
public class CreateTopicFormData {

  @NotBlank
  private String name;
  private Long subjectTeacherId;

  public CreateTopicFormData() {
  }

  public CreateTopicFormData(String name, Long subjectTeacherId) {
    this.name = name;
    this.subjectTeacherId = subjectTeacherId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getSubjectTeacherId() {
    return subjectTeacherId;
  }

  public void setSubjectTeacherId(Long subjectTeacherId) {
    this.subjectTeacherId = subjectTeacherId;
  }

  public CreateTopicParameters toParameters(SubjectTeacherService subjectTeacherService) {
    Optional<SubjectTeacher> optionalSubjectTeacher = subjectTeacherService.getOne(subjectTeacherId);
    return optionalSubjectTeacher.map(subjectTeacher ->
        new CreateTopicParameters(name, subjectTeacher)).orElse(null);
  }
}

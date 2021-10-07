package com.fyp.application.subject.web;

import com.fyp.application.school.School;
import com.fyp.application.school.SchoolService;
import com.fyp.application.subject.CreateSubjectParameter;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@NotExistingSubject
public class CreateSubjectFormData {

  @NotBlank
  private String name;
  private Long schoolId;

  public CreateSubjectFormData() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getSchoolId() {
    return schoolId;
  }

  public void setSchoolId(Long schoolId) {
    this.schoolId = schoolId;
  }

  public CreateSubjectParameter toParameters(SchoolService schoolService) {
    Optional<School> optionalSchool = schoolService.getOne(schoolId);
    if (optionalSchool.isPresent()) {
      return new CreateSubjectParameter(name, optionalSchool.get());
    } else {
      throw new IllegalArgumentException();
    }
  }
}

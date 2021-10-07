package com.fyp.application.classes.web;

import com.fyp.application.classes.CreateClassParameters;
import com.fyp.application.school.School;
import com.fyp.application.school.SchoolService;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@NotExistingClass
public class CreateClassFormData {

  @NotBlank
  private String name;
  private Long schoolId;

  public CreateClassFormData() {
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

  public CreateClassParameters toParameters(SchoolService schoolService) {
    Optional<School> optionalSchool = schoolService.getOne(schoolId);
    return optionalSchool.map(school -> new CreateClassParameters(name, school))
        .orElse(null);
  }
}

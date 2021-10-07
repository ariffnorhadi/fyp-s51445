package com.fyp.application.subject.web;

import com.fyp.application.school.School;
import com.fyp.application.school.SchoolService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class NotExistingSubjectValidator implements
    ConstraintValidator<NotExistingSubject, CreateSubjectFormData> {

  private final SchoolService schoolService;

  public NotExistingSubjectValidator(SchoolService schoolService) {
    this.schoolService = schoolService;
  }

  @Override
  public void initialize(NotExistingSubject constraintAnnotation) {

  }

  @Override
  public boolean isValid(CreateSubjectFormData formData,
                         ConstraintValidatorContext context) {
    Optional<School> optionalSchool = this.schoolService.getOne(formData.getSchoolId());
    if (optionalSchool.isPresent()) {
      School school = optionalSchool.get();
      if (school.getSubjects().stream()
          .anyMatch(subject -> subject.getName().equals(formData.getName()))) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("{SubjectAlreadyExisting}")
            .addPropertyNode("name")
            .addConstraintViolation();

        return false;
      }
    } else {
      return false;
    }
    return true;
  }
}

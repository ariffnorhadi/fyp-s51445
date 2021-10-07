package com.fyp.application.classes.web;

import com.fyp.application.classes.ClassService;
import com.fyp.application.school.School;
import com.fyp.application.school.SchoolService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class NotExistingClassValidator implements
    ConstraintValidator<NotExistingClass, CreateClassFormData> {

  private final ClassService classService;
  private final SchoolService schoolService;

  public NotExistingClassValidator(ClassService classService,
                                   SchoolService schoolService) {
    this.classService = classService;
    this.schoolService = schoolService;
  }

  @Override
  public void initialize(NotExistingClass constraintAnnotation) {

  }

  @Override
  public boolean isValid(CreateClassFormData formData,
                         ConstraintValidatorContext context) {
    Long schoolId = formData.getSchoolId();
    Optional<School> optionalSchool = this.schoolService.getOne(schoolId);

    if (optionalSchool.isPresent()) {
      School school = optionalSchool.get();
      if (classService.existsByNameAndSchool(formData.getName(), school)) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("{ClassAndSchoolAlreadyExisting}")
            .addPropertyNode("name")
            .addConstraintViolation();
        return false;
      }
    }

    return true;
  }
}

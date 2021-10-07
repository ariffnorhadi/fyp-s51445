package com.fyp.application.classes.subjectTeacher.topic.questions.web;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotSameChoiceValidator
    implements ConstraintValidator<NotSameChoice, CreateQuestionFormData> {
  @Override
  public void initialize(NotSameChoice constraintAnnotation) {
  }

  @Override
  public boolean isValid(CreateQuestionFormData formData,
                         ConstraintValidatorContext context) {

    if (formData.getChoice1().equals(formData.getChoice2())) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate("{NotSameChoice}")
          .addPropertyNode("choice1")
          .addConstraintViolation();
      return false;
    } else {
      boolean equals = formData.getChoice1().equals(formData.getChoice3());
      if (equals) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("{NotSameChoice}")
            .addPropertyNode("choice1")
            .addConstraintViolation();
        return false;
      } else {
        boolean equals1 = formData.getChoice1().equals(formData.getChoice4());
        if (equals1) {
          context.disableDefaultConstraintViolation();
          context.buildConstraintViolationWithTemplate("{NotSameChoice}")
              .addPropertyNode("choice1")
              .addConstraintViolation();
          return false;
        } else {
          boolean equals2 = formData.getChoice2().equals(formData.getChoice3());
          if (equals2) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{NotSameChoice}")
                .addPropertyNode("choice2")
                .addConstraintViolation();
          } else {
            boolean equals3 = formData.getChoice2().equals(formData.getChoice4());
            if (equals3) {
              context.disableDefaultConstraintViolation();
              context.buildConstraintViolationWithTemplate("{NotSameChoice}")
                  .addPropertyNode("choice2")
                  .addConstraintViolation();
              return false;
            } else {
              boolean equals4 = formData.getChoice3().equals(formData.getChoice4());
              if (equals4) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("{NotSameChoice}")
                    .addPropertyNode("choice3")
                    .addConstraintViolation();
                return false;
              }
            }
          }
        }
      }
    }

    return true;
  }
}

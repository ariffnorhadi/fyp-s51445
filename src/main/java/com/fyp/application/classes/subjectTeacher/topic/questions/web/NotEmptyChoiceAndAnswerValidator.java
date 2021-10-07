package com.fyp.application.classes.subjectTeacher.topic.questions.web;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotEmptyChoiceAndAnswerValidator implements
    ConstraintValidator<NotEmptyChoiceAndAnswer, CreateQuestionFormData> {

  @Override
  public void initialize(NotEmptyChoiceAndAnswer constraintAnnotation) {

  }

  @Override
  public boolean isValid(CreateQuestionFormData formData,
                         ConstraintValidatorContext context) {
    if (formData.getAnswer() == null) {
      return false;
    } else if (formData.getAnswer().equals("A") && formData.getChoice1().isEmpty()) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate("{EmptyChoiceForAnswer}")
          .addPropertyNode("choice1")
          .addConstraintViolation();
      return false;
    } else if (formData.getAnswer().equals("B") && formData.getChoice2().isEmpty()) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate("{EmptyChoiceForAnswer}")
          .addPropertyNode("choice2")
          .addConstraintViolation();
      return false;
    } else if (formData.getAnswer().equals("C") && formData.getChoice3().isEmpty()) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate("{EmptyChoiceForAnswer}")
          .addPropertyNode("choice3")
          .addConstraintViolation();
      return false;
    } else if (formData.getAnswer().equals("D") && formData.getChoice4().isEmpty()) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate("{EmptyChoiceForAnswer}")
          .addPropertyNode("choice4")
          .addConstraintViolation();
      return false;
    }

    return true;
  }
}

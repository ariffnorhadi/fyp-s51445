package com.fyp.application.user.web;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordNotMatchValidator implements ConstraintValidator<PasswordMatch, CreateUserFormData> {

  @Override
  public void initialize(PasswordMatch constraintAnnotation) {

  }

  @Override
  public boolean isValid(CreateUserFormData formData, ConstraintValidatorContext context) {
    if (!formData.getRepeatedPassword().equals(formData.getPassword())) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate("{PasswordsNotMatch}")
          .addPropertyNode("repeatedPassword")
          .addConstraintViolation();

      return false;
    }

    return true;
  }
}

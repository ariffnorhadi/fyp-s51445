package com.fyp.application.user.web;

import com.fyp.application.user.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotExistingUsernameValidator implements
    ConstraintValidator<NotExistingUsername, CreateUserFormData> {

  private final UserService userService;

  public NotExistingUsernameValidator(UserService userService) {
    this.userService = userService;
  }

  @Override
  public void initialize(NotExistingUsername constraintAnnotation) {

  }

  @Override
  public boolean isValid(CreateUserFormData formData,
                         ConstraintValidatorContext context) {
    if (userService.userExistsByUsername(formData.getUsername())) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate("{UsernameAlreadyExisting}")
          .addPropertyNode("username")
          .addConstraintViolation();
      return false;
    }

    return true;
  }
}

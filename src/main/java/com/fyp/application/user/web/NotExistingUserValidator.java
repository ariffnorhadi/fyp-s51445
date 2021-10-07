package com.fyp.application.user.web;

import com.fyp.application.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotExistingUserValidator implements
    ConstraintValidator<NotExistingUser, CreateUserFormData> {

  private final UserService userService;

  @Autowired
  public NotExistingUserValidator(UserService userService) { //<.>
    this.userService = userService;
  }

  public void initialize(NotExistingUser constraint) {
    // intentionally empty
  }

  // tag::isValid[]
  public boolean isValid(CreateUserFormData formData,
                         ConstraintValidatorContext context) {
    if (userService.userExistsByEmail(formData.getEmail())) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate("{UserAlreadyExisting}")
          .addPropertyNode("email")
          .addConstraintViolation();

      return false;
    }

    return true;
  }
  // end::isValid[]
}

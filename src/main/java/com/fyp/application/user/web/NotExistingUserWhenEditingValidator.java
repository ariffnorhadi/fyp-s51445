package com.fyp.application.user.web;

import com.fyp.application.user.User;
import com.fyp.application.user.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class NotExistingUserWhenEditingValidator implements
    ConstraintValidator<NotExistingUserWhenEditing, EditUserFormData> {

  private final UserService userService;

  public NotExistingUserWhenEditingValidator(UserService userService) {
    this.userService = userService;
  }

  @Override
  public void initialize(NotExistingUserWhenEditing constraintAnnotation) {

  }

  @Override
  public boolean isValid(EditUserFormData formData,
                         ConstraintValidatorContext context) {

    Optional<User> user = userService.getOne(formData.getId());
    if (user.isPresent()) {
      User editedUser = user.get();
      if (formData.getEmail().equals(editedUser.getEmail())) {
        return true;
      }
      if (userService.userExistsByEmail(formData.getEmail())) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("{UserAlreadyExistingWhenEditing}")
            .addPropertyNode("email")
            .addConstraintViolation();

        return false;
      }

    }

    return true;
  }
}

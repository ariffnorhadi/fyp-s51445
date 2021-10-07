package com.fyp.application.user.web;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) //<.>
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotExistingUsernameValidator.class)
public @interface NotExistingUsername {

  String message() default "{UsernameAlreadyExisting}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}

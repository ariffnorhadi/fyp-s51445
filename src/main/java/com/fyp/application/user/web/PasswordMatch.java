package com.fyp.application.user.web;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) //<.>
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordNotMatchValidator.class) //<.>
public @interface PasswordMatch {

  String message() default "{PasswordsNotMatch}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}

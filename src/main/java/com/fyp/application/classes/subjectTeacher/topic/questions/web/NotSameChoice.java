package com.fyp.application.classes.subjectTeacher.topic.questions.web;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotSameChoiceValidator.class)
public @interface NotSameChoice {

  String message() default "{NotSameChoice}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}

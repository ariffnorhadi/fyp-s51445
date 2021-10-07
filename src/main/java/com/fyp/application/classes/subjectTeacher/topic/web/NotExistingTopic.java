package com.fyp.application.classes.subjectTeacher.topic.web;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotExistingTopicValidator.class)
public @interface NotExistingTopic {

  String message() default "{TopicAlreadyExisting}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}

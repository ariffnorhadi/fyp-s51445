package com.fyp.application.classes.subjectTeacher.topic.web;

import com.fyp.application.classes.subjectTeacher.SubjectTeacher;
import com.fyp.application.classes.subjectTeacher.SubjectTeacherService;
import com.fyp.application.classes.subjectTeacher.topic.TopicService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class NotExistingTopicValidator implements
    ConstraintValidator<NotExistingTopic, CreateTopicFormData> {

  private final SubjectTeacherService subjectTeacherService;
  private final TopicService topicService;

  public NotExistingTopicValidator(SubjectTeacherService subjectTeacherService,
                                   TopicService topicService) {
    this.subjectTeacherService = subjectTeacherService;
    this.topicService = topicService;
  }

  @Override
  public void initialize(NotExistingTopic constraintAnnotation) {
  }

  @Override
  public boolean isValid(CreateTopicFormData formData,
                         ConstraintValidatorContext context) {
    Optional<SubjectTeacher> optionalSubjectTeacher =
        this.subjectTeacherService.getOne(formData.getSubjectTeacherId());

    if (optionalSubjectTeacher.isPresent()) {
      SubjectTeacher subjectTeacher = optionalSubjectTeacher.get();

      if (this.topicService.existsByNameAndSubjectTeacher(formData.getName(), subjectTeacher)) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("{TopicAlreadyExisting}")
            .addPropertyNode("name")
            .addConstraintViolation();
        return false;
      }
    } else {
      return false;
    }
    return true;
  }
}

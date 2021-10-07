package com.fyp.application.classes.subjectTeacher.web;

import com.fyp.application.classes.subjectTeacher.SubjectTeacher;
import com.fyp.application.classes.subjectTeacher.SubjectTeacherService;
import com.fyp.application.classes.subjectTeacher.topic.Topic;
import com.fyp.application.classes.subjectTeacher.topic.TopicService;
import com.fyp.application.classes.subjectTeacher.topic.web.CreateTopicFormData;
import com.fyp.application.user.User;
import com.fyp.application.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.fyp.application.infrastructure.web.RootController.getCurrentUser;
import static com.fyp.application.infrastructure.web.RootController.userIsPresent;

@Controller
@RequestMapping("/my-subject")
public class SubjectTeacherController {

  private final static Logger LOGGER = LoggerFactory.getLogger(SubjectTeacherController.class);

  private final UserService userService;
  private final SubjectTeacherService subjectTeacherService;
  private final TopicService topicService;

  public SubjectTeacherController(UserService userService,
                                  SubjectTeacherService subjectTeacherService,
                                  TopicService topicService) {
    this.userService = userService;
    this.subjectTeacherService = subjectTeacherService;
    this.topicService = topicService;
  }

  @GetMapping("{id}")
  @Secured({"ROLE_TEACHER", "ROLE_STUDENT"})
  public String getSubjectTopics(@PathVariable String id,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 Model model) {

    if (userDetails == null) {
      return "home";
    }

    SubjectTeacher subjectTeacher;
    List<Topic> topicList;

    if (userIsPresent(userDetails, userService)) {
      User currentUser = getCurrentUser(userDetails, userService);
      model.addAttribute("currentUser", currentUser);

      try {
        Optional<SubjectTeacher> optionalSubjectTeacher = this.subjectTeacherService.getOne(Long.parseLong(id));
        if (optionalSubjectTeacher.isPresent()) {
          subjectTeacher = optionalSubjectTeacher.get();
          User teacherInCharge = subjectTeacher.getTeacherInCharge();
          if (currentUser.equals(teacherInCharge) || subjectTeacher.getClasses().equals(currentUser.getClasses())) {
            topicList = new ArrayList<>(this.topicService.findAllBySubjectTeacher(subjectTeacher));
          } else {
            LOGGER.error(currentUser.getUsername() + " access to subject with ID "
                + optionalSubjectTeacher.get().getSubject().getId() + " at class with ID "
                + optionalSubjectTeacher.get().getClasses().getId() + " DENIED");
            return "error/403";
          }

        } else {
          LOGGER.error("Subject teacher with ID " + id + " does not exist");
          return "error/404";
        }

      } catch (NumberFormatException e) {
        LOGGER.error(id + "is not a subject teacher ID");
        return "error/404";
      }

    } else {
      LOGGER.error("Someone is trying to access /my-subject/" + id);
      return "home";
    }

    model.addAttribute("subjectTeacher", subjectTeacher);
    model.addAttribute("newTopic", new CreateTopicFormData());
    model.addAttribute("topicList", topicList);

    return "mySubjects/edit";
  }

}

package com.fyp.application.classes.subjectTeacher.topic.web;

import com.fyp.application.classes.subjectTeacher.SubjectTeacher;
import com.fyp.application.classes.subjectTeacher.SubjectTeacherService;
import com.fyp.application.classes.subjectTeacher.topic.CreateTopicParameters;
import com.fyp.application.classes.subjectTeacher.topic.TopicService;
import com.fyp.application.user.User;
import com.fyp.application.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

import static com.fyp.application.infrastructure.web.RootController.getCurrentUser;
import static com.fyp.application.infrastructure.web.RootController.userIsPresent;

@Controller
@RequestMapping("/topics")
public class TopicController {

  private final static Logger LOGGER = LoggerFactory.getLogger(TopicController.class);

  private final SubjectTeacherService subjectTeacherService;
  private final TopicService topicService;
  private final UserService userService;

  public TopicController(SubjectTeacherService subjectTeacherService,
                         TopicService topicService,
                         UserService userService) {
    this.subjectTeacherService = subjectTeacherService;
    this.topicService = topicService;
    this.userService = userService;
  }

  @PostMapping("create")
  @Secured("ROLE_TEACHER")
  public String doCreateTopic(@Validated @ModelAttribute("newTopic") CreateTopicFormData formData,
                              BindingResult bindingResult,
                              @AuthenticationPrincipal UserDetails userDetails,
                              Model model) {
    if (userDetails == null) {
      return "redirect:/";
    } else if (userIsPresent(userDetails, userService)) {
      User currentUser = getCurrentUser(userDetails, userService);

      if (bindingResult.hasErrors()) {
        Optional<SubjectTeacher> optionalSubjectTeacher =
            this.subjectTeacherService.getOne(formData.getSubjectTeacherId());

        if (optionalSubjectTeacher.isPresent()) {
          SubjectTeacher subjectTeacher = optionalSubjectTeacher.get();
          model.addAttribute("subjectTeacher", subjectTeacher);
          model.addAttribute("topicList", this.topicService.findAllBySubjectTeacher(subjectTeacher));
          LOGGER.error(currentUser.getUsername() + " attempt to create topic failed");
          return "mySubjects/edit";
        } else {
          return "redirect:/";
        }
      } else {
        CreateTopicParameters parameters = formData.toParameters(subjectTeacherService);

        if (parameters.getSubjectTeacher().getTeacherInCharge().equals(currentUser)) {
          this.topicService.save(parameters);
          LOGGER.info(currentUser.getUsername() + " add topic " + formData.getName() + " SUCCESS");
          return "redirect:/my-subject/" + formData.getSubjectTeacherId();
        } else {
          LOGGER.error(currentUser.getUsername() + " attempt to create topic " +
              parameters.getName() + " for subject "
              + parameters.getSubjectTeacher().getSubject().getName() + " at class "
              + parameters.getSubjectTeacher().getClasses().getName() + " DENIED");
          return "redirect:/";
        }
      }
    } else {
      LOGGER.error("User does not exist");
      return "redirect:/";
    }

  }

}

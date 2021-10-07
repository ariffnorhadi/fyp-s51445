package com.fyp.application.classes.subjectTeacher.topic.questions.answer.performance.web;

import com.fyp.application.classes.Classes;
import com.fyp.application.classes.subjectTeacher.topic.Topic;
import com.fyp.application.classes.subjectTeacher.topic.TopicService;
import com.fyp.application.classes.subjectTeacher.topic.questions.answer.performance.Performance;
import com.fyp.application.classes.subjectTeacher.topic.questions.answer.performance.PerformanceService;
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
import java.util.Random;

import static com.fyp.application.infrastructure.web.RootController.*;

@Controller
@RequestMapping("performance")
public class PerformanceController {

  private final static Logger LOGGER = LoggerFactory.getLogger(PerformanceController.class);

  private static final Random RANDOM = new Random(System.currentTimeMillis());

  private final UserService userService;
  private final PerformanceService performanceService;
  private final TopicService topicService;

  public PerformanceController(UserService userService,
                               PerformanceService performanceService,
                               TopicService topicService) {
    this.userService = userService;
    this.performanceService = performanceService;
    this.topicService = topicService;
  }

  @GetMapping("{topicId}")
  public String viewChart(Model model,
                          @PathVariable String topicId,
                          @AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) {
      return "home";
    }
    if (userIsPresent(userDetails, userService)) {

      User currentUser = getCurrentUser(userDetails, userService);

      try {
        Optional<Topic> topicOptional = this.topicService.getOne(Long.parseLong(topicId));

        if (topicOptional.isPresent()) {
          Topic selectedTopic = topicOptional.get();
          model.addAttribute("selectedTopic", selectedTopic);

          if (userIsStudent(userDetails, userService)) {
            model.addAttribute("chartData", getPerformanceDataForStudents(selectedTopic, currentUser));
          } else if (userIsTeacher(userDetails, userService)) {
            Classes classes = selectedTopic.getSubjectTeacher().getClasses();
            List<User> studentList = this.userService.findByClasses(classes);
            model.addAttribute("studentList", studentList);

          } else {
            LOGGER.error(currentUser + " does not have access to view performance");
            return "error/404";
          }
        }
      } catch (NumberFormatException e) {
        LOGGER.error(topicId + " is not a topic id");
        return "error/404";
      }
    } else {
      LOGGER.error("user does not exist");
      return "home";
    }


    return "performance/list";
  }

  private List<Object> getPerformanceData(Topic topic) {
    List<Object> objects = new ArrayList<>();
    List<Performance> performanceList = this.performanceService.findByTopic(topic);
    for (Performance performance : performanceList) {
      objects.add(List.of(performance.getAttempt().toString(), performance.getPercentage() * 100));
    }
    return objects;
  }

  private List<Object> getPerformanceDataForStudents(Topic topic, User student) {
    List<Object> objects = new ArrayList<>();
    List<Performance> performanceList = this.performanceService.findByTopicAndStudent(topic, student);
    for (Performance performance : performanceList) {
      objects.add(List.of(performance.getAttempt().toString(), performance.getPercentage() * 100));
    }
    return objects;

  }

  @GetMapping("{topicId}/{userId}")
  @Secured({"ROLE_TEACHER"})
  public String getPerformanceForStudentsFromTeacher(@PathVariable String topicId,
                                                     @PathVariable String userId,
                                                     @AuthenticationPrincipal UserDetails userDetails,
                                                     Model model) {
    if (userDetails == null) {
      return "home";
    }

    Optional<Topic> optionalTopic = this.topicService.getOne(Long.parseLong(topicId));
    Optional<User> optionalUser = this.userService.getOne(Long.parseLong(userId));

    if (optionalTopic.isPresent() && optionalUser.isPresent()) {
      User selectedStudent = optionalUser.get();
      Topic selectedTopic = optionalTopic.get();

      model.addAttribute("selectedTopic", selectedTopic);

      model.addAttribute("chartData", getPerformanceDataForStudents(selectedTopic, selectedStudent));
      return "performance/listForTeacher";

    } else {
      return "home";
    }


  }


  @GetMapping("testHistogram")
  public String testHistrogram() {
    return "performance/testHistogram";
  }


}

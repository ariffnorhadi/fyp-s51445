package com.fyp.application.classes.subjectTeacher.topic.questions.web;

import com.fyp.application.classes.subjectTeacher.topic.Topic;
import com.fyp.application.classes.subjectTeacher.topic.TopicService;
import com.fyp.application.classes.subjectTeacher.topic.questions.Question;
import com.fyp.application.classes.subjectTeacher.topic.questions.QuestionService;
import com.fyp.application.classes.subjectTeacher.topic.questions.answer.Answer;
import com.fyp.application.classes.subjectTeacher.topic.questions.answer.AnswerService;
import com.fyp.application.classes.subjectTeacher.topic.questions.answer.performance.Performance;
import com.fyp.application.classes.subjectTeacher.topic.questions.answer.performance.PerformanceService;
import com.fyp.application.classes.subjectTeacher.topic.questions.answer.web.AnswerFormData;
import com.fyp.application.classes.subjectTeacher.topic.questions.choice.Choice;
import com.fyp.application.classes.subjectTeacher.topic.questions.choice.ChoiceService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.fyp.application.infrastructure.web.RootController.getCurrentUser;
import static com.fyp.application.infrastructure.web.RootController.userIsPresent;

@Controller
@RequestMapping("")
public class QuestionController {

  private final static Logger LOGGER = LoggerFactory.getLogger(QuestionController.class);

  private final TopicService topicService;
  private final QuestionService questionService;
  private final ChoiceService choiceService;
  private final AnswerService answerService;
  private final PerformanceService performanceService;
  private final UserService userService;

  public QuestionController(TopicService topicService,
                            QuestionService questionService,
                            ChoiceService choiceService,
                            AnswerService answerService,
                            PerformanceService performanceService,
                            UserService userService) {
    this.topicService = topicService;
    this.questionService = questionService;
    this.choiceService = choiceService;
    this.answerService = answerService;
    this.performanceService = performanceService;
    this.userService = userService;
  }


  // RETRIEVE OPERATION
  @GetMapping("topics/{id}")
  @Secured({"ROLE_TEACHER", "ROLE_STUDENT"})
  public String list(@AuthenticationPrincipal UserDetails userDetails,
                     Model model, @PathVariable String id) {

    if (userDetails == null) {
      return "home";
    }

    User currentUser;
    Topic selectedTopic = null;
    List<Question> questionList = null;
    AnswerFormData answerFormData = new AnswerFormData();


    if (userIsPresent(userDetails, userService)) {
      currentUser = getCurrentUser(userDetails, userService);

      try {
        Optional<Topic> topicOptional = topicService.getOne(Long.parseLong(id));

        if (topicOptional.isPresent()) {
          selectedTopic = topicOptional.get();
          questionList = questionService.findByTopic(selectedTopic);

        }

      } catch (NumberFormatException e) {
        LOGGER.error(id + " is not topic ID");
        return "error/404";
      }

    } else {
      LOGGER.error("user does not exist");
      return "error/404";
    }

    model.addAttribute("currentUser", currentUser);
    model.addAttribute("selectedTopic", selectedTopic);
    model.addAttribute("questionList", questionList);

    List<Choice> choiceList = new ArrayList<>();
    assert questionList != null;
    for (Question question : questionList) {
      choiceList.add(new Choice());
      LOGGER.info(question.getChoice().getName());
    }
    answerFormData.setChoiceList(choiceList);
    model.addAttribute("answerFormData", answerFormData);

    model.addAttribute("byID", Comparator.comparing(Question::getId));
    model.addAttribute("byChoiceID", Comparator.comparing(Choice::getId));

    // CREATE OPERATION
    model.addAttribute("newQuestion", new CreateQuestionFormData());
    return "questions/list";
  }

  @PostMapping("question/create")
  @Secured({"ROLE_TEACHER"})
  public String saveQuestion(@Validated @ModelAttribute("newQuestion") CreateQuestionFormData formData,
                             BindingResult bindingResult,
                             Model model,
                             @AuthenticationPrincipal UserDetails userDetails) {

    if (userDetails == null) {
      return "home";
    }

    boolean one = formData.getChoice1().equals(formData.getChoice2()) && !formData.getChoice2().isEmpty();
    boolean two = formData.getChoice1().equals(formData.getChoice3()) && !formData.getChoice3().isEmpty();
    boolean three = formData.getChoice1().equals(formData.getChoice4()) && !formData.getChoice4().isEmpty();
    boolean four = formData.getChoice2().equals(formData.getChoice3()) && !formData.getChoice3().isEmpty();
    boolean five = formData.getChoice2().equals(formData.getChoice4()) && !formData.getChoice4().isEmpty();
    boolean six = formData.getChoice3().equals(formData.getChoice4()) && !formData.getChoice4().isEmpty();
    if (one || two || three || four || five || six) {
      User currentUser = new User();
      Topic selectedTopic = new Topic();
      List<Question> questionList = new ArrayList<>();
      if (userIsPresent(userDetails, userService)) {
        currentUser = getCurrentUser(userDetails, userService);
        Long topicID = formData.getTopicID();
        Optional<Topic> optionalTopic = this.topicService.getOne(topicID);
        if (optionalTopic.isPresent()) {
          selectedTopic = optionalTopic.get();
          questionList = this.questionService.findByTopic(selectedTopic);
        }
      }
      model.addAttribute("checkAnswer", "Duplicate choices detected");
      model.addAttribute("currentUser", currentUser);
      model.addAttribute("selectedTopic", selectedTopic);
      model.addAttribute("questionList", questionList);
      model.addAttribute("byID", Comparator.comparing(Question::getId));
      model.addAttribute("byChoiceID", Comparator.comparing(Choice::getId));
      return "questions/list";

    }

    if (bindingResult.hasErrors()) {
      User currentUser = new User();
      Topic selectedTopic = new Topic();
      List<Question> questionList = new ArrayList<>();
      if (userIsPresent(userDetails, userService)) {
        currentUser = getCurrentUser(userDetails, userService);
        Long topicID = formData.getTopicID();
        Optional<Topic> optionalTopic = this.topicService.getOne(topicID);
        if (optionalTopic.isPresent()) {
          selectedTopic = optionalTopic.get();
          questionList = this.questionService.findByTopic(selectedTopic);
        }
      }
      if (formData.getAnswer() == null) {
        model.addAttribute("emptyAnswer", "Please select an answer");
      }
      model.addAttribute("currentUser", currentUser);
      model.addAttribute("selectedTopic", selectedTopic);
      model.addAttribute("questionList", questionList);
      model.addAttribute("byID", Comparator.comparing(Question::getId));
      model.addAttribute("byChoiceID", Comparator.comparing(Choice::getId));
      return "questions/list";
    } else {
      this.questionService.save(formData.toParameters(topicService));
      return "redirect:/topics/" + formData.getTopicID();
    }
  }

  @PostMapping("question/{id}/delete")
  @Secured({"ROLE_TEACHER"})
  public String doDeleteQuestion(@PathVariable String id,
                                 RedirectAttributes redirectAttributes,
                                 @AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) {
      return "home";
    }
    if (userIsPresent(userDetails, userService)) {
      User currentUser = getCurrentUser(userDetails, userService);
      try {
        Optional<Question> optionalQuestion = this.questionService.getOne(Long.parseLong(id));
        if (optionalQuestion.isPresent()) {
          Topic topic = optionalQuestion.get().getTopic();
          User teacherInCharge = topic.getSubjectTeacher().getTeacherInCharge();
          if (currentUser.equals(teacherInCharge)) {
            this.questionService.deleteById(optionalQuestion.get().getId());
            redirectAttributes.addFlashAttribute("deletedQuestion", optionalQuestion.get().getName());
            return "redirect:/topics/" + topic.getId();
          } else {
            LOGGER.error(currentUser.getUsername() + " access to question with ID " + id + " DENIED");
            return "error/403";
          }
        }
      } catch (NumberFormatException e) {
        LOGGER.error(id + " is not question ID");
        return "error/404";
      }
    } else {
      LOGGER.error("User does not exist");
      return "home";
    }
    return "home";
  }

  @PostMapping("questions/answers/{topicId}")
  @Secured({"ROLE_STUDENT"})
  public String saveAnswers(@ModelAttribute AnswerFormData answerFormData,
                            @AuthenticationPrincipal UserDetails userDetails,
                            @PathVariable String topicId,
                            Model model) {
    if (userDetails == null) {
      return "home";
    }
    if (userIsPresent(userDetails, userService)) {
      User currentUser = getCurrentUser(userDetails, userService);
      long attempt;

      List<Performance> performanceListForStudents = this.performanceService.findByStudent(currentUser);
      if (performanceListForStudents.isEmpty()) {
        attempt = 1;
      } else {
        attempt = performanceListForStudents.size() + 1;
      }

      long correctAnswerForPerformance = 0L;
      long incorrectAnswer = 0L;
      Topic topic = new Topic();

      if (answerFormData.choiceList != null) {

        for (Choice choice : answerFormData.choiceList) {

          int no_attempt = 0;
          if (choice.getId() != null) {
            LOGGER.info(choice.getId().toString());
            Optional<Choice> optionalChoice = this.choiceService.findById(choice.getId());

            if (optionalChoice.isPresent()) {
              Choice studentChoice = optionalChoice.get();
              Question selectedQuestion = studentChoice.getQuestion();
              topic = selectedQuestion.getTopic();
              Choice correctAnswer = selectedQuestion.getChoice();
              Answer answer = new Answer(studentChoice, selectedQuestion, currentUser);

              answer.setAttempt(attempt);
              this.answerService.saveAnswer(answer);

              if (answer.getAnswer().equals(correctAnswer)) {
                correctAnswerForPerformance = correctAnswerForPerformance + 1;
              } else {
                incorrectAnswer = incorrectAnswer + 1;
              }
            }

          } else {
            no_attempt = no_attempt + 1;
            incorrectAnswer = incorrectAnswer + 1;
            LOGGER.error("User has " + no_attempt + " questions");
          }

        }
      } else {
        LOGGER.error(currentUser.getUsername() + " does not answer any question");
        return list(userDetails, model, topicId);
      }


      float string_percentage = (float) correctAnswerForPerformance / topic.getQuestions().size();

      BigDecimal bd = new BigDecimal(Double.toString(string_percentage));
      bd = bd.setScale(2, RoundingMode.HALF_UP);
      double percentages = bd.doubleValue();

      Performance performance = new Performance(attempt, correctAnswerForPerformance,
          incorrectAnswer, percentages, currentUser, topic);
      this.performanceService.savePerformance(performance);

      // TODO: Create table to save student's answers
    } else {
      LOGGER.error("user does not exist");
      return "home";
    }


    return "redirect:/";
  }


}

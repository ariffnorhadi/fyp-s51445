package com.fyp.application.subject.web;

import com.fyp.application.school.School;
import com.fyp.application.school.SchoolService;
import com.fyp.application.subject.Subject;
import com.fyp.application.subject.SubjectService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.fyp.application.infrastructure.web.RootController.getCurrentUser;
import static com.fyp.application.infrastructure.web.RootController.userIsPresent;

@Controller
@RequestMapping("/subjects")
@Secured({"ROLE_PRINCIPAL", "ROLE_ADMIN"})
public class SubjectController {

  private final static Logger LOGGER = LoggerFactory.getLogger(SubjectController.class);
  private final UserService userService;
  private final SchoolService schoolService;
  private final SubjectService subjectService;

  public SubjectController(UserService userService,
                           SchoolService schoolService,
                           SubjectService subjectService) {
    this.userService = userService;
    this.schoolService = schoolService;
    this.subjectService = subjectService;
  }

  @GetMapping("")
  public String list(@AuthenticationPrincipal UserDetails userDetails,
                     Model model) {
    if (userDetails == null) {
      return "home";
    } else {
      if (userIsPresent(userDetails, userService)) {
        if (userIsPresent(userDetails, userService)) {
          User currentUser = getCurrentUser(userDetails, userService);
          model.addAttribute("currentUser", currentUser);
          model.addAttribute("newSubject", new CreateSubjectFormData());

          // Principal
          List<School> ownedSchool = new ArrayList<>();
          boolean userIsPrincipal = currentUser.getRoles().stream()
              .anyMatch(role -> role.getName().equals("ROLE_PRINCIPAL"));
          if (userIsPrincipal) {
            ownedSchool.addAll(this.schoolService.findSchoolsByPrincipal(currentUser));
          }
          model.addAttribute("ownedSchool", ownedSchool);

          // Admin
          List<Subject> subjectListForAdmin = new ArrayList<>();
          boolean userIsAdmin = currentUser.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
          if (userIsAdmin) {
            subjectListForAdmin.addAll(currentUser.getSchool().getSubjects());
          }
          model.addAttribute("subjectListForAdmin", subjectListForAdmin.stream()
              .sorted(Comparator.comparing(Subject::getName)).collect(Collectors.toList()));
        }

      } else {
        LOGGER.error("No user is at /subjects currently");
        return "home";
      }
    }

    return "subjects/list";
  }

  @PostMapping("/create")
  public String doCreateSubject(@Validated @ModelAttribute("newSubject") CreateSubjectFormData formData,
                                BindingResult bindingResult,
                                Model model,
                                @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes redirectAttributes) {
    if (userDetails == null) {
      return "home";
    } else {
      if (userIsPresent(userDetails, userService)) {
        User currentUser = getCurrentUser(userDetails, userService);
        model.addAttribute("currentUser", currentUser);


        if (bindingResult.hasErrors()) {
          if (currentUser.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_PRINCIPAL"))) {
            // Principal
            List<School> ownedSchool = new ArrayList<>();
            if (currentUser.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_PRINCIPAL"))) {
              ownedSchool.addAll(this.schoolService.findSchoolsByPrincipal(currentUser));
            }
            model.addAttribute("ownedSchool", ownedSchool);
            if (formData.getName().isEmpty()) {
              redirectAttributes.addFlashAttribute("blankName", "Please enter a name");
            } else {
              redirectAttributes.addFlashAttribute("existed", "The class has already existed");
            }
          } else if (currentUser.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
            // Admin
            List<Subject> subjectListForAdmin = new ArrayList<>();
            if (currentUser.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
              subjectListForAdmin.addAll(currentUser.getSchool().getSubjects());
            }
            model.addAttribute("subjectListForAdmin", subjectListForAdmin.stream()
                .sorted(Comparator.comparing(Subject::getName)).collect(Collectors.toList()));
            return "subjects/list";
          } else {
            LOGGER.warn(currentUser.getUsername() + " roles is unknown");
          }
        } else {
          this.subjectService.save(formData.toParameters(schoolService));
        }
      } else {
        LOGGER.error("Someone is trying to create a subject.");
      }

      return "redirect:/subjects";

    }
  }
}

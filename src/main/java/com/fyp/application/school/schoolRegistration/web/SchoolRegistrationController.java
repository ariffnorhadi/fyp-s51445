package com.fyp.application.school.schoolRegistration.web;

import com.fyp.application.role.Role;
import com.fyp.application.role.RoleService;
import com.fyp.application.school.School;
import com.fyp.application.school.SchoolService;
import com.fyp.application.school.schoolRegistration.SchoolRegistrationService;
import com.fyp.application.school.schoolRegistration.Status;
import com.fyp.application.user.User;
import com.fyp.application.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/school")
public class SchoolRegistrationController {

  private final static Logger LOGGER = LoggerFactory.getLogger(SchoolRegistrationController.class);
  private final UserService userService;
  private final SchoolService schoolService;
  private final SchoolRegistrationService registrationService;
  private final RoleService roleService;

  public SchoolRegistrationController(UserService userService,
                                      SchoolService schoolService,
                                      SchoolRegistrationService registrationService,
                                      RoleService roleService) {
    this.userService = userService;
    this.schoolService = schoolService;
    this.registrationService = registrationService;
    this.roleService = roleService;
  }

  @PostMapping("/{schoolId}/request/{roleId}")
  public String doRequest(@PathVariable String schoolId,
                          @PathVariable String roleId,
                          @AuthenticationPrincipal UserDetails userDetails,
                          RedirectAttributes redirectAttributes) {
    Optional<School> optionalSchool = schoolService.getOne(Long.parseLong(schoolId));
    Optional<User> optionalUser = userService.findUserByUsername(userDetails.getUsername());
    Optional<Role> optionalRole = roleService.findRoleById(Long.parseLong(roleId));
    if (optionalUser.isPresent() && optionalSchool.isPresent() && optionalRole.isPresent()) {
      User applicant = optionalUser.get();
      School school = optionalSchool.get();
      Role appliedPost = optionalRole.get();

      if (!this.registrationService.existsByApplicantAndSchool(applicant, school)
          || !this.registrationService.existsByApplicantAndSchoolAndStatus(
          applicant, school, Status.PENDING.name())) {

        this.registrationService.saveRequest(applicant, school, appliedPost);
        redirectAttributes.addFlashAttribute(
            "successRequestMessage", school.getName());
        LOGGER.info(applicant.getUsername() + " request as " + appliedPost.getDescription() +
            " at " + school.getName() + " SUCCESS");
      } else {
        redirectAttributes.addFlashAttribute("redundantRequest", school.getName());
      }
    } else {
      redirectAttributes.addFlashAttribute("failRequest");
      LOGGER.error("User does not exist");
    }

    return "redirect:/schools";
  }
}

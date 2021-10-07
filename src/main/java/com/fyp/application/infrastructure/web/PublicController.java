package com.fyp.application.infrastructure.web;

import com.fyp.application.role.Role;
import com.fyp.application.role.RoleService;
import com.fyp.application.school.School;
import com.fyp.application.school.SchoolService;
import com.fyp.application.school.schoolRegistration.SchoolRegistration;
import com.fyp.application.school.schoolRegistration.SchoolRegistrationService;
import com.fyp.application.school.schoolRegistration.Status;
import com.fyp.application.user.User;
import com.fyp.application.user.UserNotFoundException;
import com.fyp.application.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
public class PublicController {

  private final static Logger LOGGER = LoggerFactory.getLogger(PublicController.class);
  private final UserService userService;
  private final RoleService roleService;
  private final SchoolService schoolService;
  private final SchoolRegistrationService schoolRegistrationService;

  public PublicController(SchoolService schoolService,
                          UserService userService,
                          SchoolRegistrationService schoolRegistrationService,
                          RoleService roleService) {
    this.schoolService = schoolService;
    this.userService = userService;
    this.schoolRegistrationService = schoolRegistrationService;
    this.roleService = roleService;
  }

  @ModelAttribute("schoolList")
  public List<School> schoolList() {
    return this.schoolService.SCHOOL_LIST();
  }

  @ModelAttribute("schools")
  public Page<School> schools(Pageable pageable) {
    return this.schoolService.getAllSchoolsWithPage(pageable);
  }

  @ModelAttribute("rolesList")
  public List<Role> roleList() {
    return roleService.ROLE_LIST();
  }


  @GetMapping("/schools")
  public String schoolsList(@AuthenticationPrincipal UserDetails userDetails,
                            Model model) {
    if (userDetails == null) {
      return "schools/schools";
    } else {
      Optional<User> optionalUser = userService.findUserByUsername(userDetails.getUsername());
      if (optionalUser.isPresent()) {
        User currentUser = optionalUser.get();
        List<SchoolRegistration> pendingRequestListByCurrentUser = this.schoolRegistrationService
            .findSchoolRegistrationsByApplicantAndStatus(
                currentUser, Status.PENDING.name()
            );
        model.addAttribute("pendingRequests", pendingRequestListByCurrentUser);

        List<School> availableSchools = schoolService.SCHOOL_LIST();
        List<School> ownedSchools = schoolService.findSchoolsByPrincipal(currentUser);
        for (SchoolRegistration pendingRequest : pendingRequestListByCurrentUser) {
          School pendingRequestSchool = pendingRequest.getSchool();
          availableSchools.remove(pendingRequestSchool);
        }
        for (School school : ownedSchools) {
          availableSchools.remove(school);
        }
        model.addAttribute("availableSchools", availableSchools);

      } else {
        LOGGER.error("User does not exist");
      }
      return "schools/list";
    }
  }

  @GetMapping("/schools/{id}")
  public String getSchool(@PathVariable String id, Model model) {
    Long schoolId = Long.parseLong(id);
    School school = schoolService.getOne(schoolId)
        .orElseThrow(() -> new UserNotFoundException(schoolId));
    model.addAttribute("selectedSchool", school);

    return "schools/profile";
  }
}


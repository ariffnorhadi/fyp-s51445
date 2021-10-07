package com.fyp.application.school.web;

import com.fyp.application.role.Role;
import com.fyp.application.role.RoleService;
import com.fyp.application.school.School;
import com.fyp.application.school.SchoolService;
import com.fyp.application.school.schoolRegistration.SchoolRegistration;
import com.fyp.application.school.schoolRegistration.SchoolRegistrationService;
import com.fyp.application.user.User;
import com.fyp.application.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/school")
public class SchoolController {

  private final static Logger LOGGER = LoggerFactory.getLogger(SchoolController.class);
  private final SchoolService schoolService;
  private final UserService userService;
  private final RoleService roleService;
  private final SchoolRegistrationService schoolRegistrationService;

  public SchoolController(SchoolService schoolService, UserService userService,
                          RoleService roleService,
                          SchoolRegistrationService schoolRegistrationService) {
    this.schoolService = schoolService;
    this.userService = userService;
    this.roleService = roleService;
    this.schoolRegistrationService = schoolRegistrationService;
  }

  @ModelAttribute("schoolList")
  public List<School> schoolList() {
    return this.schoolService.SCHOOL_LIST();
  }

  @ModelAttribute("schools")
  public Page<School> schools(Pageable pageable) {
    return this.schoolService.getAllSchoolsWithPage(pageable);
  }

  @GetMapping("/establish")
  public String createSchoolForm(Model model) {
    model.addAttribute("school", new CreateSchoolFormData());

    return "schools/create";
  }

  @PostMapping("/establish")
  public String doCreateSchool(@Validated
                               @ModelAttribute("school")
                                   CreateSchoolFormData formData,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               @AuthenticationPrincipal UserDetails userDetails) {
    if (bindingResult.hasErrors()) {
      return "schools/create";
    }

    Optional<User> optionalUser =
        userService.findUserByUsername(userDetails.getUsername());
    Optional<Role> optionalPrincipalRole =
        this.roleService.findRoleById(1L);
    if (optionalUser.isPresent() && optionalPrincipalRole.isPresent()) {
      User currentUser = optionalUser.get();
      Role principalRole = optionalPrincipalRole.get();
      formData.setCreatedBy(currentUser);
      formData.setCreatedOn(LocalDate.now());
      formData.setPrincipal(currentUser);

      /*
      How to reload authorities on user update with Spring Security
      https://stackoverflow.com/questions/9910252/how-to-reload-authorities-on-user-update-with-spring-security
       */
      Set<Role> userRoles = currentUser.getRoles();
      userRoles.add(principalRole);
      currentUser.setRoles(userRoles);
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      List<GrantedAuthority> updatedAuthorities = new ArrayList<>(auth.getAuthorities());
      updatedAuthorities.add(new SimpleGrantedAuthority(principalRole.getName())); //add your role here [e.g., new SimpleGrantedAuthority("ROLE_NEW_ROLE")]
      Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);
      SecurityContextHolder.getContext().setAuthentication(newAuth);

      schoolService.saveSchool(formData.toParameters());

      redirectAttributes.addFlashAttribute(
          "registeredSchoolName", formData.getName());

      LOGGER.info(currentUser.getUsername() + " created a school named " + formData.getName());

      if (currentUser.getSchool() != null) {
        LOGGER.warn(currentUser.getUsername() + " created a school but currently he/she is part of " + currentUser.getSchool().getName());
        return "redirect:/";
      }
    }
    return "redirect:/schools";
  }

  @PostMapping("/{id}/approve")
  public String doApproveRequest(@PathVariable String id,
                                 @AuthenticationPrincipal UserDetails userDetails) {
    Long requestId = Long.parseLong(id);
    Optional<User> optionalApprovedBy =
        userService.findUserByUsername(userDetails.getUsername());
    Optional<SchoolRegistration> optional =
        schoolRegistrationService.getOne(requestId);
    if (optional.isPresent() && optionalApprovedBy.isPresent()) {
      SchoolRegistration request = optional.get();
      User approvedBy = optionalApprovedBy.get();

      schoolRegistrationService.approveRequest(request, approvedBy);
    }
    return "redirect:/requests";
  }

  @PostMapping("/{id}/approveAll")
  public String doApproveAllRequest(@PathVariable String id,
                                    @AuthenticationPrincipal UserDetails userDetails) {
    Long schoolId = Long.parseLong(id);
    Optional<School> optionalSchool = this.schoolService.getOne(schoolId);
    Optional<User> optionalApprovedBy =
        userService.findUserByUsername(userDetails.getUsername());
    if (optionalSchool.isPresent() && optionalApprovedBy.isPresent()) {
      School school = optionalSchool.get();
      User approvedBy = optionalApprovedBy.get();

      this.schoolRegistrationService.approveAllRequestsBySchool(school, approvedBy);
    }
    return "redirect:/requests";
  }

  @PostMapping("/{id}/reject")
  public String doRejectRequest(@PathVariable String id,
                                @AuthenticationPrincipal UserDetails userDetails) {
    Long requestId = Long.parseLong(id);
    Optional<User> optionalRejectBy = userService.findUserByUsername(userDetails.getUsername());
    Optional<SchoolRegistration> optionalRejectRequest = schoolRegistrationService.getOne(requestId);
    if (optionalRejectRequest.isPresent() && optionalRejectBy.isPresent()) {
      SchoolRegistration request = optionalRejectRequest.get();
      User rejectedBy = optionalRejectBy.get();

      schoolRegistrationService.rejectRequest(request, rejectedBy);
    }

    return "redirect:/requests";
  }

}

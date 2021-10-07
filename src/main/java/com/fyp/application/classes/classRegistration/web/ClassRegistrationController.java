package com.fyp.application.classes.classRegistration.web;

import com.fyp.application.classes.ClassService;
import com.fyp.application.classes.Classes;
import com.fyp.application.classes.classRegistration.ClassRegistration;
import com.fyp.application.classes.classRegistration.ClassRegistrationService;
import com.fyp.application.school.schoolRegistration.Status;
import com.fyp.application.user.User;
import com.fyp.application.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class ClassRegistrationController {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(ClassRegistrationController.class);
  private final UserService userService;
  private final ClassService classService;
  private final ClassRegistrationService classRegistrationService;

  public ClassRegistrationController(UserService userService,
                                     ClassService classService,
                                     ClassRegistrationService classRegistrationService) {
    this.userService = userService;
    this.classService = classService;
    this.classRegistrationService = classRegistrationService;
  }

  @PostMapping("/classes/{classId}/enroll")
  @Secured("ROLE_STUDENT")
  public String doRequestEnrollClass(@PathVariable String classId,
                                     @AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) {
      return "home";
    }
    long classID = Long.parseLong(classId);
    Optional<Classes> optionalClasses = this.classService.getOne(classID);
    Optional<User> optionalUser = this.userService.findUserByUsername(userDetails.getUsername());
    if (optionalUser.isPresent() && optionalClasses.isPresent()) {
      User currentUser = optionalUser.get();
      Classes selectedClass = optionalClasses.get();
      ClassRegistration registration = new ClassRegistration();
      registration.setApplicant(currentUser);
      registration.setClasses(selectedClass);
      registration.setStatus(Status.PENDING.name());
      registration.setApplicationDateTime(LocalDateTime.now());

      this.classRegistrationService.save(registration);
      LOGGER.info(currentUser.getUsername() + " request to " + selectedClass.getName() + " SUCCESS");
    } else {
      LOGGER.warn("User does not exist");
    }

    return "redirect:/";

  }

  @PostMapping("/classesRequest/{requestId}/approve")
  @Secured("ROLE_TEACHER")
  public String doApproveRequest(@PathVariable String requestId,
                                 @AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) {
      return "home";
    }

    Optional<User> optionalUser = this.userService.findUserByUsername(userDetails.getUsername());
    Optional<ClassRegistration> optionalClassRegistration =
        this.classRegistrationService.getOne(Long.parseLong(requestId));
    if (optionalUser.isPresent() && optionalClassRegistration.isPresent()) {
      User currentUser = optionalUser.get();
      ClassRegistration request = optionalClassRegistration.get();
      User teacherInCharge = request.getClasses().getTeacher();
      if (currentUser.equals(teacherInCharge)) {
        User applicant = request.getApplicant();

        request.setStatus(Status.APPROVED.name());
        applicant.setClasses(request.getClasses());
        this.classRegistrationService.save(request);

        List<ClassRegistration> otherRequest =
            this.classRegistrationService.findByApplicant(request.getApplicant());
        otherRequest.remove(request);
        for (ClassRegistration registration : otherRequest) {
          registration.setStatus(Status.INVALID.name());
          this.classRegistrationService.save(registration);
        }

      }
    }

    return "redirect:/requests";
  }

  @PostMapping("/classesRequest/{requestId}/reject")
  @Secured("ROLE_TEACHER")
  public String doRejectRequest(@PathVariable String requestId,
                                @AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) {
      return "home";
    }
    Optional<User> optionalUser = this.userService.findUserByUsername(userDetails.getUsername());
    Optional<ClassRegistration> optionalClassRegistration =
        this.classRegistrationService.getOne(Long.parseLong(requestId));
    if (optionalUser.isPresent() && optionalClassRegistration.isPresent()) {
      User currentUser = optionalUser.get();
      ClassRegistration request = optionalClassRegistration.get();
      User teacherInCharge = request.getClasses().getTeacher();
      if (currentUser.equals(teacherInCharge)) {
        request.setStatus(Status.REJECTED.name());
        this.classRegistrationService.save(request);
      }
    }

    return "redirect:/requests";
  }

}

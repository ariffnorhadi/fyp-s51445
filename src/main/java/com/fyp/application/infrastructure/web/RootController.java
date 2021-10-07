package com.fyp.application.infrastructure.web;

import com.fyp.application.classes.ClassService;
import com.fyp.application.classes.Classes;
import com.fyp.application.classes.classRegistration.ClassRegistration;
import com.fyp.application.classes.classRegistration.ClassRegistrationService;
import com.fyp.application.classes.subjectTeacher.SubjectTeacher;
import com.fyp.application.classes.subjectTeacher.SubjectTeacherService;
import com.fyp.application.role.Role;
import com.fyp.application.school.School;
import com.fyp.application.school.SchoolService;
import com.fyp.application.school.schoolRegistration.SchoolRegistration;
import com.fyp.application.school.schoolRegistration.SchoolRegistrationService;
import com.fyp.application.school.schoolRegistration.Status;
import com.fyp.application.user.User;
import com.fyp.application.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/") // <.>
public class RootController {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(RootController.class);

  private final UserService userService;
  private final SchoolService schoolService;
  private final SchoolRegistrationService schoolRegistrationService;
  private final ClassService classService;
  private final ClassRegistrationService classRegistrationService;
  private final SubjectTeacherService subjectTeacherService;

  public RootController(UserService userService,
                        SchoolService schoolService,
                        SchoolRegistrationService schoolRegistrationService,
                        ClassService classService,
                        ClassRegistrationService classRegistrationService,
                        SubjectTeacherService subjectTeacherService) {
    this.userService = userService;
    this.schoolService = schoolService;
    this.schoolRegistrationService = schoolRegistrationService;
    this.classService = classService;
    this.classRegistrationService = classRegistrationService;
    this.subjectTeacherService = subjectTeacherService;
  }

  public static boolean userIsPresent(UserDetails userDetails, UserService userService) {
    Optional<User> optionalUser = userService.findUserByUsername(userDetails.getUsername());
    return optionalUser.isPresent();
  }

  public static User getCurrentUser(UserDetails userDetails, UserService userService) {
    Optional<User> optionalUser = userService.findUserByUsername(userDetails.getUsername());
    return optionalUser.orElse(null);
  }

  public static boolean userIsPrincipal(UserDetails userDetails, UserService userService) {
    Optional<User> optionalUser = userService.findUserByUsername(userDetails.getUsername());
    return optionalUser.map(user -> user.getRoles().stream()
        .anyMatch(role -> role.getName().equals("ROLE_PRINCIPAL"))).orElse(false);
  }

  public static boolean userIsAdmin(UserDetails userDetails, UserService userService) {
    Optional<User> optionalUser = userService.findUserByUsername(userDetails.getUsername());
    return optionalUser.map(user -> user.getRoles().stream()
        .anyMatch(role -> role.getName().equals("ROLE_ADMIN"))).orElse(false);
  }

  public static boolean userIsTeacher(UserDetails userDetails, UserService userService) {
    Optional<User> optionalUser = userService.findUserByUsername(userDetails.getUsername());
    return optionalUser.map(user -> user.getRoles().stream()
        .anyMatch(role -> role.getName().equals("ROLE_TEACHER"))).orElse(false);
  }

  public static boolean userIsStudent(UserDetails userDetails, UserService userService) {
    Optional<User> optionalUser = userService.findUserByUsername(userDetails.getUsername());
    return optionalUser.map(user -> user.getRoles().stream()
        .anyMatch(role -> role.getName().equals("ROLE_STUDENT"))).orElse(false);
  }

  @GetMapping
  public String root(@AuthenticationPrincipal UserDetails userDetails,
                     Model model) {
    if (userDetails == null) {
      return "home";
    }

    Optional<User> optionalUser = userService.findUserByUsername(userDetails.getUsername());
    if (optionalUser.isPresent()) {
      User currentUser = optionalUser.get();
      Set<Role> userRoles = currentUser.getRoles();
      model.addAttribute("currentUser", currentUser);
      model.addAttribute("userSchool", currentUser.getSchool());

      Collection<? extends GrantedAuthority> authorities = currentUser.getAuthorities();
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      List<GrantedAuthority> updatedAuthorities = new ArrayList<>(authorities);
      Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);
      SecurityContextHolder.getContext().setAuthentication(newAuth);

      List<SchoolRegistration> userRequests =
          this.schoolRegistrationService.findSchoolRegistrationsByApplicant(currentUser);
      List<SchoolRegistration> pendingAndRejectedRequests = new ArrayList<>();
      for (SchoolRegistration request : userRequests) {
        if (request.getStatus().equals(Status.PENDING.name()) ||
            request.getStatus().equals(Status.REJECTED.name())) {
          pendingAndRejectedRequests.add(request);
        }
      }
      model.addAttribute(
          "pendingAndRejectedRequests",
          pendingAndRejectedRequests.stream().sorted(Comparator
              .comparing(SchoolRegistration::getStatus)
              .thenComparing(SchoolRegistration::getApplicationDateTime).reversed())
              .collect(Collectors.toList()));

      SchoolRegistration approvedRequest =
          this.schoolRegistrationService
              .findSchoolRegistrationByApplicantAndStatus(
                  currentUser, Status.APPROVED.name());
      model.addAttribute("approvedRequest", approvedRequest);

      // Principal
      List<School> ownedSchoolForPrincipal = this.schoolService.findSchoolsByPrincipal(currentUser);
      List<SchoolRegistration> pendingRequestForPrincipal = new ArrayList<>();

      // Admin
      List<SchoolRegistration> pendingRequestsForAdmin = new ArrayList<>();
      List<User> studentListForAdmin = new ArrayList<>();
      List<User> teacherListForAdmin = new ArrayList<>();

      // Teacher
      List<ClassRegistration> pendingClassRequestsForTeacher = new ArrayList<>();

      // Student
      List<ClassRegistration> pendingClassRequestsForStudent = new ArrayList<>();
      List<Classes> availableClass = new ArrayList<>();
      List<ClassRegistration> studentClassRequests = new ArrayList<>();
      List<SubjectTeacher> subjectClassTeacherListForStudentReference = new ArrayList<>();


      if (userRoles.stream().anyMatch(role -> role.getName().equals("ROLE_PRINCIPAL"))) {
        for (School school : ownedSchoolForPrincipal) {
          List<SchoolRegistration> pendingSchoolRegistrations =
              school.getSchoolRegistrations().stream()
                  .filter(schoolRegistration ->
                      schoolRegistration.getStatus().equals(Status.PENDING.name()))
                  .collect(Collectors.toList());
          pendingRequestForPrincipal.addAll(pendingSchoolRegistrations);
        }

        // TODO: Display students, teachers and admin correctly for PRINCIPAL
      }

      // Admin
      if (userRoles.stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"))) {

        School adminSchool = currentUser.getSchool();
        pendingRequestsForAdmin.addAll
            (this.schoolRegistrationService
                .findSchoolRegistrationsBySchoolAndStatus(adminSchool, Status.PENDING.name()));

        Set<User> peopleFromAdminSchool = adminSchool.getPeople();
        for (User user : peopleFromAdminSchool) {
          Set<Role> peopleRoles = user.getRoles();
          if (peopleRoles.stream().anyMatch(role -> role.getName().equals("ROLE_STUDENT"))) {
            studentListForAdmin.add(user);
          } else if (peopleRoles.stream().anyMatch(role -> role.getName().equals("ROLE_TEACHER"))) {
            teacherListForAdmin.add(user);
          }
        }
      }

      // Teacher
      if (userRoles.stream().anyMatch(role -> role.getName().equals("ROLE_TEACHER"))) {

        Set<Classes> classesInCharge = currentUser.getClassesInCharge();
        for (Classes classes : classesInCharge) {
          Set<ClassRegistration> classRegistrations = classes.getClassRegistrations();
          for (ClassRegistration classRequests : classRegistrations) {
            if (classRequests.getStatus().equals(Status.PENDING.name())) {
              pendingClassRequestsForTeacher.add(classRequests);
            }
          }
        }
      }

      // Student
      if (userRoles.stream().anyMatch(role -> role.getName().equals("ROLE_STUDENT"))) {

        pendingClassRequestsForStudent.addAll(this.classRegistrationService
            .findByApplicantAndStatus(currentUser, Status.PENDING.name()));

        School studentSchool = currentUser.getSchool();
        availableClass.addAll(this.classService.findBySchool(studentSchool));
        for (ClassRegistration request : pendingClassRequestsForStudent) {
          Classes pendingClass = request.getClasses();
          availableClass.remove(pendingClass);
        }

        studentClassRequests.addAll(this.classRegistrationService.findByApplicant(currentUser));

        // Get subject list for the student
        subjectClassTeacherListForStudentReference.addAll(this.subjectTeacherService.findByClasses(currentUser.getClasses()));
      }

      // User
      if (userRoles.stream().noneMatch(role ->
          role.getName().equals("ROLE_PRINCIPAL")
              || role.getName().equals("ROLE_ADMIN")
              || role.getName().equals("ROLE_TEACHER")
              || role.getName().equals("ROLE_STUDENT"))) {
        LOGGER.warn(currentUser.getUsername() + " has no role");
      }

      model.addAttribute("ownedSchoolForPrincipal", ownedSchoolForPrincipal);
      model.addAttribute("pendingRequestForPrincipal", pendingRequestForPrincipal);

      model.addAttribute("pendingRequestsForAdmin", pendingRequestsForAdmin);
      model.addAttribute("studentListForAdmin", studentListForAdmin);
      model.addAttribute("teacherListForAdmin", teacherListForAdmin);

      model.addAttribute("pendingClassRequestsForTeacher", pendingClassRequestsForTeacher);

      model.addAttribute("pendingClassRequest", pendingClassRequestsForStudent);
      model.addAttribute("availableClass", availableClass);
      model.addAttribute("studentClassRequests", studentClassRequests);
      model.addAttribute("subjectListForStudents", subjectClassTeacherListForStudentReference);

    } else {
      LOGGER.warn("User does not exist");
      return "home";
    }

    return "index"; // <.>
  }

  @GetMapping("/requests")
  @Secured({"ROLE_PRINCIPAL", "ROLE_ADMIN", "ROLE_TEACHER"})
  public String requestsPage(Model model,
                             @AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) {
      return "home";
    }

    Optional<User> optionalUser = this.userService
        .findUserByUsername(userDetails.getUsername());
    if (optionalUser.isPresent()) {
      User currentUser = optionalUser.get();
      School userSchool = currentUser.getSchool();
      model.addAttribute("userSchool", userSchool);
      Set<Role> userRoles = currentUser.getRoles();

      // Principal
      List<School> pendingSchoolForPrincipal = new ArrayList<>();
      List<SchoolRegistration> pendingRequestsForPrincipal = new ArrayList<>();
      if (userRoles.stream().anyMatch(role -> role.getName().equals("ROLE_PRINCIPAL"))) {

        List<School> ownedSchoolForPrincipal = this.schoolService.findSchoolsByPrincipal(currentUser);
        for (School school : ownedSchoolForPrincipal) {
          List<SchoolRegistration> pendingRequests =
              this.schoolRegistrationService.findSchoolRegistrationsBySchoolAndStatus(school, Status.PENDING.name());
          pendingRequestsForPrincipal.addAll(pendingRequests);

          Set<SchoolRegistration> schoolRequests = school.getSchoolRegistrations();
          if (schoolRequests.stream().anyMatch(schoolRegistration -> schoolRegistration.getStatus().equals(Status.PENDING.name()))) {
            pendingSchoolForPrincipal.add(school);
          }
        }
      }
      model.addAttribute("pendingSchoolForPrincipal", pendingSchoolForPrincipal);
      model.addAttribute("pendingRequestsForPrincipal", pendingRequestsForPrincipal);

      // Admin
      List<SchoolRegistration> pendingRequestsForAdmin = new ArrayList<>();
      if (userRoles.stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"))) {

        pendingRequestsForAdmin.addAll(this
            .schoolRegistrationService.findSchoolRegistrationsBySchoolAndStatus(
                userSchool, Status.PENDING.name()));
      }
      model.addAttribute("pendingRequestsToBeApprovedByAdmin", pendingRequestsForAdmin);

      // Teachers
      List<Classes> pendingClasses = new ArrayList<>();
      List<ClassRegistration> pendingClassRegistrations = new ArrayList<>();
      if (userRoles.stream().anyMatch(role -> role.getName().equals("ROLE_TEACHER"))) {

        Set<Classes> classesInCharge = currentUser.getClassesInCharge();
        for (Classes classes : classesInCharge) {
          if (classes.getClassRegistrations().stream()
              .anyMatch(registration -> registration.getStatus().equals(Status.PENDING.name()))) {
            pendingClasses.add(classes);
          }
          for (ClassRegistration registration : classes.getClassRegistrations()) {
            if (registration.getStatus().equals(Status.PENDING.name())) {
              pendingClassRegistrations.add(registration);
            }
          }
        }
      }
      model.addAttribute("pendingClassRequest", pendingClasses);
      model.addAttribute("pendingClassRegistrations", pendingClassRegistrations);

    } else {
      LOGGER.error("User does not exist");
    }

    return "schools/schoolRequests/requests";
  }

  @GetMapping("people/{schoolID}")
  @Secured("ROLE_PRINCIPAL")
  public String showPeople(@PathVariable String schoolID,
                           @AuthenticationPrincipal UserDetails userDetails,
                           Model model) {
    if (userDetails == null) {
      return "home";
    }
    List<User> studentListForPrincipal = new ArrayList<>();
    List<User> teacherListForPrincipal = new ArrayList<>();
    List<User> adminListForPrincipal = new ArrayList<>();
    if (userIsPresent(userDetails, userService)) {
      User currentUser = getCurrentUser(userDetails, userService);
      model.addAttribute("currentUser", currentUser);

      try {
        Optional<School> optionalSchool = this.schoolService.getOne(Long.parseLong(schoolID));
        if (optionalSchool.isPresent()) {
          School selectedSchool = optionalSchool.get();
          model.addAttribute("selectedSchool", selectedSchool);
          if (currentUser.getCreatedSchools().stream().anyMatch(school -> school.equals(selectedSchool))) {
            Set<User> people = selectedSchool.getPeople();
            for (User user : people) {
              if (user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_STUDENT"))) {
                studentListForPrincipal.add(user);
              } else if (user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_TEACHER"))) {
                teacherListForPrincipal.add(user);
              } else {
                adminListForPrincipal.add(user);
              }
            }
          } else {
            LOGGER.error(currentUser.getUsername() + " is not a principal of school with ID " + schoolID);
            return "error/403";
          }
        } else {
          LOGGER.error("School with ID " + schoolID + " does not exist");
          return "error/404";
        }
      } catch (NumberFormatException e) {
        LOGGER.error(schoolID + " is not a school ID");
        return "error/404";
      }

    } else {
      LOGGER.error("Someone is trying to see people from some school with " + schoolID + "ID");
      return "error/404";
    }

    model.addAttribute("studentListForPrincipal", studentListForPrincipal);
    model.addAttribute("teacherListForPrincipal", teacherListForPrincipal);
    model.addAttribute("adminListForPrincipal", adminListForPrincipal);

    return "people";

  }

  @PostMapping("/people/{userID}/expel")
  @Secured({"ROLE_PRINCIPAL", "ROLE_ADMIN"})
  public String doExpelUser(@PathVariable String userID,
                            @AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) {
      return "redirect:/";
    }
    if (userIsPresent(userDetails, userService)) {
      User currentUser = getCurrentUser(userDetails, userService);
      try {
        Optional<User> optionalExpelledUser = this.userService.getOne(Long.parseLong(userID));
        if (optionalExpelledUser.isPresent()) {
          User userToBeExpelled = optionalExpelledUser.get();
          if (currentUser.getCreatedSchools().stream()
              .anyMatch(school -> school.equals(userToBeExpelled.getSchool()))
              || currentUser.getSchool().equals(userToBeExpelled.getSchool())) {
            if (userToBeExpelled.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
              this.userService.expelAdmin(userToBeExpelled);
            } else if (userToBeExpelled.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_TEACHER"))) {
              this.userService.expelTeacher(userToBeExpelled);
            } else if (userToBeExpelled.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_STUDENT"))) {
              this.userService.expelStudent(userToBeExpelled);
            } else {
              LOGGER.error(userToBeExpelled.getUsername() + " roles is unknown. EXPEL operation aborted");
            }

          } else {
            LOGGER.error(currentUser.getUsername() + " attempt to expel " + userToBeExpelled.getUsername() + " DENIED");
          }

        } else {
          LOGGER.error("User with ID " + userID + " does not exist");
        }
      } catch (NumberFormatException e) {
        LOGGER.error(userID + " is not a User iD");
      }
    } else {
      LOGGER.error("Someone is trying to update resource to /people/" + userID + "/expel");
    }

    return "redirect:/";

  }

}

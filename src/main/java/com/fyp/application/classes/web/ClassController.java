package com.fyp.application.classes.web;

import com.fyp.application.classes.ClassService;
import com.fyp.application.classes.Classes;
import com.fyp.application.classes.CreateClassParameters;
import com.fyp.application.classes.classRegistration.ClassRegistration;
import com.fyp.application.classes.classRegistration.ClassRegistrationService;
import com.fyp.application.classes.subjectTeacher.SubjectTeacher;
import com.fyp.application.classes.subjectTeacher.SubjectTeacherService;
import com.fyp.application.role.Role;
import com.fyp.application.school.School;
import com.fyp.application.school.SchoolService;
import com.fyp.application.school.schoolRegistration.Status;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.fyp.application.infrastructure.web.RootController.getCurrentUser;
import static com.fyp.application.infrastructure.web.RootController.userIsPresent;

@Controller
@RequestMapping("/classes")
public class ClassController {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(ClassController.class);

  private final UserService userService;
  private final SchoolService schoolService;
  private final ClassService classService;
  private final SubjectService subjectService;
  private final SubjectTeacherService subjectTeacherService;
  private final ClassRegistrationService classRegistrationService;

  public ClassController(UserService userService,
                         SchoolService schoolService,
                         ClassService classService,
                         SubjectService subjectService,
                         SubjectTeacherService subjectTeacherService,
                         ClassRegistrationService classRegistrationService) {
    this.userService = userService;
    this.schoolService = schoolService;
    this.classService = classService;
    this.subjectService = subjectService;
    this.subjectTeacherService = subjectTeacherService;
    this.classRegistrationService = classRegistrationService;
  }

  @GetMapping("")
  public String list(@AuthenticationPrincipal UserDetails userDetails,
                     Model model) {
    if (userDetails == null) {
      return "home";
    } else {
      Optional<User> userOptional = this.userService
          .findUserByUsername(userDetails.getUsername());
      if (userOptional.isPresent()) {
        User currentUser = userOptional.get();
        model.addAttribute("currentUser", currentUser);

        // Create class for Principal
        CreateClassFormData newClass = new CreateClassFormData();
        model.addAttribute("newClass", newClass);

        List<School> ownedSchool = this.schoolService.findSchoolsByPrincipal(currentUser);
        model.addAttribute("ownedSchools", ownedSchool);
        List<School> schoolWithClassesForPrincipalReference =
            this.schoolService.findSchoolsByPrincipal(currentUser);
        for (School school : ownedSchool) {
          Set<Classes> classesBySchool = school.getClasses();
          if (classesBySchool.isEmpty()) {
            schoolWithClassesForPrincipalReference.remove(school);
          }
        }
        model.addAttribute("schoolWithClassesForPrincipalReference",
            schoolWithClassesForPrincipalReference);

        List<Classes> classesListForAdminReference =
            this.classService.findBySchool(currentUser.getSchool());
        model.addAttribute("classListForAdminReference",
            classesListForAdminReference.stream()
                .sorted(Comparator.comparing(Classes::getName))
                .collect(Collectors.toList()));

        return "classes/list";
      } else {
        return "home";
      }
    }
  }

  /*
  Binding result must come after validated object
  https://stackoverflow.com/questions/24802681/org-springframework-validation-beanpropertybindingresult-exception
   */
  @PostMapping("/create")
  @Secured({"ROLE_PRINCIPAL", "ROLE_ADMIN"})
  public String doCreateClass(@Validated @ModelAttribute("newClass") CreateClassFormData formData,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes,
                              @AuthenticationPrincipal UserDetails userDetails) {
    /*
    TODO: figure out why the binding result method does not work
     */
    if (userDetails == null) {
      return "home";
    } else {
      Optional<User> userOptional = this.userService
          .findUserByUsername(userDetails.getUsername());
      if (userOptional.isPresent()) {
        User currentUser = userOptional.get();
        model.addAttribute("currentUser", currentUser);

        // Create class for Principal
        CreateClassFormData newClass = new CreateClassFormData();
        model.addAttribute("newClass", newClass);

        List<School> ownedSchool = this.schoolService.findSchoolsByPrincipal(currentUser);
        model.addAttribute("ownedSchools", ownedSchool);
        List<School> schoolWithClassesForPrincipalReference =
            this.schoolService.findSchoolsByPrincipal(currentUser);
        for (School school : ownedSchool) {
          Set<Classes> classesBySchool = school.getClasses();
          if (classesBySchool.isEmpty()) {
            schoolWithClassesForPrincipalReference.remove(school);
          }
        }
        model.addAttribute("schoolWithClassesForPrincipalReference",
            schoolWithClassesForPrincipalReference);

        List<Classes> classesListForAdminReference =
            this.classService.findBySchool(currentUser.getSchool());
        model.addAttribute("classListForAdminReference",
            classesListForAdminReference);

        if (bindingResult.hasErrors()) {
          if (formData.getName().isEmpty()) {
            redirectAttributes.addFlashAttribute("blankName", "Please enter a name");
          } else {
            redirectAttributes.addFlashAttribute("existed", "The class has already existed");
          }
        } else {

          CreateClassParameters parameters = formData.toParameters(schoolService);
          parameters.setName(parameters.getName().toUpperCase());
          this.classService.saveClass(parameters);
          redirectAttributes.addFlashAttribute("successCreateClass", formData.getName());
        }
        return "redirect:/classes";
      } else {
        return "home";
      }
    }
  }

  @GetMapping("{id}/students")
  @Secured({"ROLE_PRINCIPAL", "ROLE_ADMIN", "ROLE_TEACHER"})
  public String showStudents(@PathVariable String id,
                             Model model,
                             @AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) {
      return "home";
    }

    if (userIsPresent(userDetails, userService)) {
      User currentUser = getCurrentUser(userDetails, userService);
      model.addAttribute("currentUser", currentUser);

      try {
        Optional<Classes> optionalClasses = this.classService.getOne(Long.parseLong(id));
        if (optionalClasses.isPresent()) {
          Classes selectedClass = optionalClasses.get();
          model.addAttribute("selectedClass", selectedClass);
          List<User> studentList;

          boolean userIsPrincipal = currentUser.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_PRINCIPAL"));
          boolean userIsAdmin = currentUser.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
          boolean userIsTeacher = currentUser.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_TEACHER"));
          boolean authorizedTeacher = userIsTeacher && currentUser.getClassesInCharge()
              .stream().anyMatch(classes -> classes.equals(selectedClass));
          Set<SubjectTeacher> subjectTeacherSet = currentUser.getSubjectTeacherSet();
          boolean b = subjectTeacherSet.stream().anyMatch(subjectTeacher ->
              subjectTeacher.getClasses().equals(selectedClass));
          if ((userIsPrincipal && currentUser.getCreatedSchools()
              .stream().anyMatch(school -> school.equals(selectedClass.getSchool())))
              || (userIsAdmin && currentUser.getSchool().equals(selectedClass.getSchool()))) {
            studentList = new ArrayList<>(selectedClass.getStudents());

          } else if (authorizedTeacher || (userIsTeacher && b)) {
            studentList = new ArrayList<>(selectedClass.getStudents());
            model.addAttribute("studentList", studentList);
            return "studentsForTeacher";

          } else {
            LOGGER.error(currentUser.getUsername() + " does not have access to class with ID " + id);
            return "error/403";
          }

          model.addAttribute("studentList", studentList);


        } else {
          LOGGER.error("Class with ID " + id + " does not exist");
          return "error/404";
        }
      } catch (NumberFormatException e) {
        LOGGER.error(id + " is not class ID");
        return "error/404";
      }

    }

    return "students";

  }

  @GetMapping("{id}/assign/teacher")
  @Secured({"ROLE_PRINCIPAL", "ROLE_ADMIN"})
  public String assignTeacherList(Model model,
                                  @AuthenticationPrincipal UserDetails userDetails,
                                  @PathVariable String id) {
    if (userDetails == null) {
      return "home";
    }
    // TODO: Implement getOne method by ID, find school from class, not from the user

    Optional<User> userOptional = this.userService.findUserByUsername(userDetails.getUsername());
    if (userOptional.isPresent()) {
      User currentUser = userOptional.get();
      model.addAttribute("currentUser", currentUser);
      Set<Role> currentUserRoles = currentUser.getRoles();

      try {
        long classId = Long.parseLong(id);
        Optional<Classes> optionalSelectedClass = this.classService.getOne(classId);

        // Principal
        List<User> teacherListForPrincipal = new ArrayList<>();

        // Admin
        List<User> teacherListForAdmin = new ArrayList<>();

        if (optionalSelectedClass.isPresent()) {
          Classes selectedClass = optionalSelectedClass.get();
          model.addAttribute("selectedClass", selectedClass);
          School classSchool = selectedClass.getSchool();
          Set<User> selectedSchoolPeople = classSchool.getPeople();

          // Check if user is a principal to the school's class
          if (currentUserRoles.stream().anyMatch(role -> role.getName().equals("ROLE_PRINCIPAL"))
              && currentUser.getCreatedSchools().stream().anyMatch(school -> school.equals(classSchool))) {

            for (User user : selectedSchoolPeople) {
              if (user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_TEACHER"))) {
                teacherListForPrincipal.add(user);
              }
            }
          }

          // Check if user is an admin
          else if (currentUserRoles.stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"))
              && currentUser.getSchool().equals(classSchool)) {

            School adminSchool = currentUser.getSchool();
            Set<User> adminSchoolPeople = adminSchool.getPeople();
            for (User user : adminSchoolPeople) {
              if (user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_TEACHER"))) {
                teacherListForAdmin.add(user);
              }
            }
          } else {
            LOGGER.warn(currentUser.getUsername() + " access to " + selectedClass.getName() + " at school "
                + classSchool.getName() + " with ID " + classSchool.getId() + " DENIED");
            return "error/403";
          }
        }

        model.addAttribute("teacherListForPrincipal", teacherListForPrincipal);
        model.addAttribute("teacherListForAdmin", teacherListForAdmin);

      } catch (NumberFormatException e) {
        LOGGER.error(currentUser.getUsername() + " is accessing class with id " + id + " which is not a class ID");
        return "error/404";
      }
    } else {
      LOGGER.warn("User does not exist");
    }
    return "classes/assign";
  }

  @PostMapping("/{id}/assign/{teacherID}")
  @Secured({"ROLE_PRINCIPAL", "ROLE_ADMIN"})
  public String doAssignTeacher(@AuthenticationPrincipal UserDetails userDetails,
                                @PathVariable String id,
                                @PathVariable String teacherID) {
    if (userDetails == null) {
      return "home";
    }
    Optional<User> optionalCurrentUser = this.userService.findUserByUsername(userDetails.getUsername());
    if (optionalCurrentUser.isPresent()) {

      try {
        long classId = Long.parseLong(id);
        Optional<Classes> optionalClass = this.classService.getOne(classId);
        long assignedTeacherId = Long.parseLong(teacherID);
        Optional<User> optionalTeacher = this.userService.getOne(assignedTeacherId);

        if (optionalClass.isPresent() && optionalTeacher.isPresent()) {
          // TODO: Check user authorization (PRINCIPAL/ADMIN) whether he/she is part of the school
          Classes selectedClass = optionalClass.get();
          User assignedTeacher = optionalTeacher.get();

          this.classService.saveClass(selectedClass, assignedTeacher);
          User currentUser = optionalCurrentUser.get();
          LOGGER.info(currentUser.getUsername() + " assign " + assignedTeacher.getUsername() + " teacher to "
              + selectedClass.getName() + " in " + selectedClass.getSchool().getName()
              + " SUCCESS");
        }
      } catch (NumberFormatException e) {
        LOGGER.error("Either assigned teacher or the class does not exist");
      }

    }

    return "redirect:/classes";
  }

  @GetMapping("{id}/assign/subject")
  @Secured({"ROLE_PRINCIPAL", "ROLE_ADMIN"})
  public String assignSubjects(@PathVariable String id,
                               @AuthenticationPrincipal UserDetails userDetails,
                               Model model) {
    if (userDetails == null) {
      return "home";
    }

    if (userIsPresent(userDetails, userService)) {
      User currentUser = getCurrentUser(userDetails, userService);
      model.addAttribute("currentUser", currentUser);
      try {
        long classId = Long.parseLong(id);
        Optional<Classes> optionalClasses = this.classService.getOne(classId);
        if (optionalClasses.isPresent()) {
          Classes selectedClass = optionalClasses.get();
          model.addAttribute("selectedClass", selectedClass);

          List<Subject> subjectListBySchool;
          List<SubjectTeacher> subjectTeacherList = new ArrayList<>();
          List<SubjectTeacher> subjectTeacherSelectedClass = this.subjectTeacherService.findByClasses(selectedClass);
          if (!subjectTeacherSelectedClass.isEmpty()) {
            subjectTeacherList.addAll(subjectTeacherSelectedClass);
          }
          model.addAttribute("subjectTeacherList", subjectTeacherList);

          boolean userIsAuthorizedPrincipal = currentUser.getCreatedSchools().stream().anyMatch(school -> school.equals(selectedClass.getSchool()));
          if (userIsAuthorizedPrincipal || currentUser.getSchool().equals(selectedClass.getSchool())) {
            subjectListBySchool = new ArrayList<>(this.subjectService.findBySchool(selectedClass.getSchool()));
            for (SubjectTeacher subjectTeacher : subjectTeacherList) {
              subjectListBySchool.removeIf(subject -> subject.equals(subjectTeacher.getSubject()));
            }
          } else {
            LOGGER.error(currentUser.getUsername() + " access to class with ID " + classId + " DENIED");
            return "error/403";
          }

          model.addAttribute("subjectListBySchool", subjectListBySchool);

        } else {
          LOGGER.error("Class with " + classId + " does not exist");
          return "error/404";
        }

      } catch (NumberFormatException e) {
        LOGGER.error(id + " is not a class ID");
        return "error/404";
      }
    } else {
      LOGGER.error("Someone is trying to assign subjects to class with ID " + id);
      return "error/404";
    }
    return "classes/assignSubjects";
  }

  @PostMapping("{classID}/assign/subject/{subjectID}")
  @Secured({"ROLE_PRINCIPAL", "ROLE_ADMIN"})
  public String doAssignSubjectToClass(@PathVariable String classID,
                                       @PathVariable String subjectID,
                                       @AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) {
      return "home";
    }

    if (userIsPresent(userDetails, userService)) {
      User currentUser = getCurrentUser(userDetails, userService);
      try {
        Optional<Classes> optionalClasses = this.classService.getOne(Long.parseLong(classID));
        Optional<Subject> optionalSubject = this.subjectService.getOne(Long.parseLong(subjectID));

        if (optionalClasses.isPresent() && optionalSubject.isPresent()) {
          boolean userIsPrincipal = currentUser.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_PRINCIPAL"));
          boolean userIsAdmin = currentUser.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"));

          if ((userIsPrincipal && currentUser.getCreatedSchools().stream()
              .anyMatch(school -> school.equals(optionalClasses.get().getSchool())))
              || (userIsAdmin && currentUser.getSchool().equals(optionalClasses.get().getSchool()))) {
            SubjectTeacher subjectTeacher = new SubjectTeacher();
            subjectTeacher.setSubject(optionalSubject.get());
            subjectTeacher.setClasses(optionalClasses.get());

            this.subjectTeacherService.save(subjectTeacher);
          } else {
            LOGGER.error(currentUser.getUsername() + " modification to class with ID " + classID + " DENIED");
          }
        }
      } catch (NumberFormatException e) {
        LOGGER.error("Class with ID " + classID + " or subject with ID " + subjectID + " does not exist");
      }
    }

    return "redirect:/classes/" + classID + "/assign/subject";
  }

  @GetMapping("{classID}/assign/subject/{subjectTeacherID}")
  @Secured({"ROLE_PRINCIPAL", "ROLE_ADMIN"})
  public String assignTeacherSubject(@PathVariable String classID,
                                     @PathVariable String subjectTeacherID,
                                     @AuthenticationPrincipal UserDetails userDetails,
                                     Model model) {
    if (userDetails == null) {
      return "home";
    }
    List<User> teacherListBySchool = new ArrayList<>();

    if (userIsPresent(userDetails, userService)) {
      User currentUser = getCurrentUser(userDetails, userService);
      try {
        Optional<Classes> optionalClasses = this.classService.getOne(Long.parseLong(classID));
        Optional<SubjectTeacher> optionalSubjectTeacher =
            this.subjectTeacherService.getOne(Long.parseLong(subjectTeacherID));
        if (optionalSubjectTeacher.isPresent() && optionalClasses.isPresent()) {

          boolean userIsPrincipal = currentUser.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_PRINCIPAL"));
          boolean userIsAdmin = currentUser.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
          Classes selectedClass = optionalClasses.get();
          model.addAttribute("selectedClass", selectedClass);
          SubjectTeacher subjectTeacher = optionalSubjectTeacher.get();
          model.addAttribute("subjectTeacher", subjectTeacher);

          if ((userIsPrincipal && currentUser.getCreatedSchools()
              .stream().anyMatch(school -> school.equals(selectedClass.getSchool())))
              || (userIsAdmin && currentUser.getSchool().equals(selectedClass.getSchool()))) {

            if (subjectTeacher.getClasses().equals(selectedClass)) {
              LOGGER.info("ACCESS GRANTED for "
                  + currentUser.getUsername() + " to access class with ID " + classID);
              Set<User> people = selectedClass.getSchool().getPeople();
              for (User user : people) {
                if (user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_TEACHER"))) {
                  teacherListBySchool.add(user);
                }
              }

            }
          }
        } else {
          LOGGER.error("Class with ID "
              + classID + " may not exist or any details in the class may not available");
          return "error/404";
        }

      } catch (NumberFormatException e) {
        LOGGER.error("Class with ID " + classID + " or any subject in the class is not available");
        return "error/404";
      }
    }

    model.addAttribute("teacherListBySchool", teacherListBySchool);

    return "classes/assignSubjectTeacher";
  }

  @PostMapping("/{classID}/assign/subject/teacher/{teacherID}/{subjectTeacherID}")
  @Secured({"ROLE_PRINCIPAL", "ROLE_ADMIN"})
  public String doAssignTeacherToSubject(@PathVariable String teacherID,
                                         @PathVariable String subjectTeacherID,
                                         @AuthenticationPrincipal UserDetails userDetails,
                                         @PathVariable String classID) {
    if (userDetails == null) {
      return "home";
    }

    if (userIsPresent(userDetails, userService)) {
      User currentUser = getCurrentUser(userDetails, userService);

      try {
        Optional<SubjectTeacher> optionalSubjectTeacher = this.subjectTeacherService.getOne(Long.parseLong(subjectTeacherID));
        if (optionalSubjectTeacher.isPresent()) {

          boolean userIsPrincipal = currentUser.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_PRINCIPAL"));
          boolean userIsAdmin = currentUser.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
          SubjectTeacher subjectTeacher = optionalSubjectTeacher.get();
          if ((userIsPrincipal && currentUser.getCreatedSchools().stream()
              .anyMatch(school -> school.equals(subjectTeacher.getClasses().getSchool())))
              || (userIsAdmin &&
              currentUser.getSchool().equals(subjectTeacher.getClasses().getSchool()))) {
            try {
              Optional<User> assignedTeacher = this.userService.getOne(Long.parseLong(teacherID));
              if (assignedTeacher.isPresent()) {
                subjectTeacher.setTeacherInCharge(assignedTeacher.get());
                this.subjectTeacherService.save(subjectTeacher);
              }
            } catch (NumberFormatException e) {
              LOGGER.error(teacherID + " is not a teacher ID");
              return "redirect:/classes";
            }

          } else {
            LOGGER.error(currentUser.getUsername() + " modification to class "
                + subjectTeacher.getClasses().getName() + " DENIED");
            return "error/403";
          }

        }

      } catch (NumberFormatException e) {
        LOGGER.error(subjectTeacherID + " is not a subjectTeacher ID");
        return "error/404";
      }
    }

    return "redirect:/classes/" + classID + "/assign/subject";
  }

  @PostMapping("{classId}/approveAll")
  @Secured("ROLE_TEACHER")
  public String doApproveAllClassRequest(@PathVariable String classId,
                                         @AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) {
      return "home";
    }

    Optional<User> optionalUser = this.userService.findUserByUsername(userDetails.getUsername());

    try {
      long classID = Long.parseLong(classId);
      Optional<Classes> optionalClasses = this.classService.getOne(classID);
      if (optionalUser.isPresent() && optionalClasses.isPresent()) {
        User currentUser = optionalUser.get();
        Classes selectedClass = optionalClasses.get();

        if (selectedClass.getTeacher().equals(currentUser)) {
          Set<ClassRegistration> classRegistrations = selectedClass.getClassRegistrations();
          for (ClassRegistration registration : classRegistrations) {
            User applicant = registration.getApplicant();

            registration.setStatus(Status.APPROVED.name());
            applicant.setClasses(registration.getClasses());
            this.classRegistrationService.save(registration);

            Set<ClassRegistration> classRegistrations1 = applicant.getClassRegistrations();
            classRegistrations1.remove(registration);
            for (ClassRegistration registration1 : classRegistrations1) {
              registration1.setStatus(Status.INVALID.name());
              registration1.setVerification_date(LocalDate.now());
              this.classRegistrationService.save(registration1);
            }
          }

        } else {
          LOGGER.error(currentUser.getUsername() + " is not teacher in charge for " + selectedClass.getName());
        }
      }
    } catch (NumberFormatException e) {
      LOGGER.error(classId + " is not a Class ID");
    }

    return "redirect:/requests";
  }

  @GetMapping("{classId}/edit")
  @Secured({"ROLE_PRINCIPAL", "ROLE_ADMIN"})
  public String editClassPage(@PathVariable String classId, Model model,
                              @AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) {
      return "home";
    } else {
      Optional<User> optionalUser = this.userService.findUserByUsername(userDetails.getUsername());
      // Check user
      if (optionalUser.isPresent()) {
        User currentUser = optionalUser.get();
        School userSchool = currentUser.getSchool();
        model.addAttribute("currentUser", currentUser);
        try {
          long id = Long.parseLong(classId);
          Optional<Classes> optionalClasses = this.classService.getOne(id);

          // Check class
          if (optionalClasses.isPresent()) {
            Classes selectedClass = optionalClasses.get();
            School classSchool = selectedClass.getSchool();
            model.addAttribute("selectedClass", selectedClass);
            Set<User> people = classSchool.getPeople();

            List<User> teacherListForClass = new ArrayList<>();
            for (User user : people) {
              if (user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_TEACHER"))) {
                teacherListForClass.add(user);
              }
            }
            if (selectedClass.getTeacher() != null) {
              teacherListForClass.remove(selectedClass.getTeacher());
            }
            model.addAttribute("teacherListForClass",
                teacherListForClass.stream()
                    .sorted(Comparator.comparing(User::getFullName))
                    .collect(Collectors.toList()));

            // TODO: Finish EDIT whole class including name, teacher in charge and subjects

            // Principal
            if (currentUser.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_PRINCIPAL"))
                && currentUser.getCreatedSchools().stream().anyMatch(school -> school.equals(classSchool))) {
              LOGGER.info(currentUser.getUsername() + " is a principal accessing "
                  + selectedClass.getName() + " class");
            }
            // Admin
            else if (currentUser.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"))
                && currentUser.getSchool().equals(classSchool)) {
              if (userSchool.equals(classSchool)) {
                LOGGER.info(currentUser.getUsername() + " is an admin accessing "
                    + selectedClass.getName() + " class");
              } else {
                LOGGER.warn(currentUser.getUsername() + " is not an admin from " + classSchool.getName() + " school");
              }
            } else {
              LOGGER.warn(currentUser.getUsername() + " access to " + selectedClass.getName()
                  + " at school " + classSchool.getName() + " DENIED");
              return "error/403";
            }
          } else {
            LOGGER.error(classId + " does not exist");
            return "error/404";
          }

        } catch (NumberFormatException e) {
          LOGGER.error(classId + " is not a classID");
          return "error/404";
        }
      } else {
        LOGGER.warn("Someone is trying to access endpoint classes/" + classId + "/edit");
        return "home";
      }

      return "classes/edit";
    }

  }

  // TODO: Implement UPDATE operation for whole Class entity including subjects assigned to each class
  @PostMapping("/update/{id}")
  public String doUpdateClass(@PathVariable String id,
                              @AuthenticationPrincipal UserDetails userDetails,
                              RedirectAttributes redirectAttributes) {
    if (userDetails == null) {
      return "redirect:/";
    } else {
      if (userIsPresent(userDetails, userService)) {
        try {
          long classId = Long.parseLong(id);
          Optional<Classes> optionalClass = this.classService.getOne(classId);
          if (optionalClass.isPresent()) {
            LOGGER.error("TODO: Implement complete UPDATE operation for Class entity");

          } else {
            LOGGER.error(id + " ID for Class entity does not exist");
          }
        } catch (NumberFormatException e) {
          LOGGER.error(id + " is a string and ID for class entity supposedly to be a Long / bigint");
        }
      } else {
        LOGGER.error("Someone is trying to update resource for Class entity");
        return "redirect:/";
      }

      return "redirect:/classes";
    }

  }
}

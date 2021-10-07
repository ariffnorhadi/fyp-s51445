package com.fyp.application;

import com.fyp.application.role.Role;
import com.fyp.application.role.RoleService;
import com.fyp.application.user.Gender;
import com.fyp.application.user.User;
import com.fyp.application.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Set;

@SpringBootApplication
public class FinalYearProjectApplication {

  public static void main(String[] args) {
    SpringApplication.run(FinalYearProjectApplication.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(UserService userService,
                                             PasswordEncoder passwordEncoder,
                                             RoleService roleService) {
    return args -> {

      if (roleService.ROLE_LIST().isEmpty()) {

        Role principalRole = new Role();
        principalRole.setName("ROLE_PRINCIPAL");
        principalRole.setDescription("Principal");
        roleService.save(principalRole);

        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");
        adminRole.setDescription("Admin");
        roleService.save(adminRole);

        Role teacherRole = new Role();
        teacherRole.setName("ROLE_TEACHER");
        teacherRole.setDescription("Teacher");
        roleService.save(teacherRole);

        Role studentRole = new Role();
        studentRole.setName("ROLE_STUDENT");
        studentRole.setDescription("Student");
        roleService.save(studentRole);

        Role userRole = new Role();
        userRole.setName("ROLE_USER");
        userRole.setDescription("User");
        roleService.save(userRole);

        Role ownerRole = new Role();
        ownerRole.setName("ROLE_OWNER");
        ownerRole.setDescription("Owner of project");
        roleService.save(ownerRole);

        User owner = new User(1L, "owner",
            "owner@email.com", passwordEncoder.encode("password"),
            "Owner", "Last Name",
            Gender.MALE.name(), "address",
            "01121", LocalDate.now());
        owner.setRoles(Set.of(ownerRole));
        userService.saveUser(owner);

      }

    };
  }

}

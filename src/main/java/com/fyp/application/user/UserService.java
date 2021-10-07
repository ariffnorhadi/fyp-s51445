package com.fyp.application.user;

import com.fyp.application.classes.Classes;
import com.fyp.application.role.Role;
import com.fyp.application.school.School;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {

  void saveUser(User user);

  void saveUser(CreateUserParameters parameters);

  void editUser(Long userId, EditUserParameters parameters);

  Optional<User> getOne(Long userId);

  List<User> getAllUsers();

  Page<User> getAllUsersWithPage(Pageable pageable);

  List<User> findUsersByRoles(Role role);

  List<User> findUsersBySchool(School school);

  List<User> findByClasses(Classes classes);

  boolean userExistsByEmail(String email);

  boolean userExistsByUsername(String username);

  Optional<User> findUserByEmail(String email);

  Optional<User> findUserByUsername(String username);

  void expelAdmin(User user);

  void expelTeacher(User user);

  void expelStudent(User user);

  void deleteUserById(Long id);
}

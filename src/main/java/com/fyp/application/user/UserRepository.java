package com.fyp.application.user;

import com.fyp.application.classes.Classes;
import com.fyp.application.role.Role;
import com.fyp.application.school.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long>,
    PagingAndSortingRepository<User, Long> {

  boolean existsUserByEmail(String email);

  boolean existsUserByUsername(String username);

  Optional<User> findUserByEmail(String email);

  Optional<User> findUserByUsername(String username);

  List<User> findUsersByRoles(Role role);

  List<User> findUsersBySchool(School school);

  List<User> findByClasses(Classes classes);
}

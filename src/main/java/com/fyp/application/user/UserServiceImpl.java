package com.fyp.application.user;

import com.fyp.application.classes.ClassRepository;
import com.fyp.application.classes.Classes;
import com.fyp.application.classes.classRegistration.ClassRegistration;
import com.fyp.application.role.Role;
import com.fyp.application.school.School;
import com.fyp.application.school.schoolRegistration.SchoolRegistration;
import com.fyp.application.school.schoolRegistration.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

  private final UserRepository repository;
  private final ClassRepository classRepository;

  public UserServiceImpl(UserRepository repository,
                         ClassRepository classRepository) {
    this.repository = repository;
    this.classRepository = classRepository;
  }

  @Override
  public void saveUser(User user) {
    this.repository.save(user);
  }

  @Override
  public void saveUser(CreateUserParameters parameters) {
    User user = new User(1L, parameters.getUsername(), parameters.getEmail(),
        parameters.getPassword(), parameters.getFirstName(),
        parameters.getLastName(), parameters.getGender().toString(),
        parameters.getAddress(), parameters.getPhoneNumber(),
        parameters.getBirthday());

    this.repository.save(user);
  }

  @Override
  public void editUser(Long userId, EditUserParameters parameters) {
    User user = this.repository.findById(userId).
        orElseThrow(() -> new UserNotFoundException(userId));
    if (!parameters.getVersion().equals(user.getVersion())) {
      throw new
          ObjectOptimisticLockingFailureException(User.class, user.getId());
    }

    parameters.update(user);
    user.setVersion(parameters.getVersion() + 1);
        /*
        TODO: Ask Wim, because TT book said that save method will be called automatically, but it doesnt work
        Therefore, save method is called below
         */
    this.repository.save(user);
  }

  @Override
  public Optional<User> getOne(Long userId) {
    return this.repository.findById(userId);
  }

  @Override
  public List<User> getAllUsers() {
    return this.repository.findAll();
  }

  @Override
  public Page<User> getAllUsersWithPage(Pageable pageable) {
    return this.repository.findAll(pageable);
  }

  // TODO: Implement displaying number of students and teachers
  @Override
  public List<User> findUsersByRoles(Role role) {
    return this.repository.findUsersByRoles(role);
  }

  @Override
  public List<User> findUsersBySchool(School school) {
    return this.repository.findUsersBySchool(school);
  }

  @Override
  public List<User> findByClasses(Classes classes) {
    return this.repository.findByClasses(classes);
  }

  @Override
  public boolean userExistsByEmail(String email) {
    return this.repository.existsUserByEmail(email);
  }

  @Override
  public boolean userExistsByUsername(String username) {
    return this.repository.existsUserByUsername(username);
  }

  @Override
  public Optional<User> findUserByEmail(String email) {
    return this.repository.findUserByEmail(email);
  }

  @Override
  public Optional<User> findUserByUsername(String username) {
    return this.repository.findUserByUsername(username);
  }

  @Override
  public void expelAdmin(User user) {
    user.setSchool(null);
    Set<Role> roles = user.getRoles();
    roles.removeIf(role -> role.getName().equals("ROLE_ADMIN"));
    Set<SchoolRegistration> schoolRegistrations = user.getSchoolRegistrations();
    for (SchoolRegistration schoolRegistration : schoolRegistrations) {
      if (schoolRegistration.getStatus().equals(Status.APPROVED.name())) {
        schoolRegistration.setStatus(Status.EXPELLED.name());
        schoolRegistration.setVerification_date(LocalDate.now());
      }
    }
  }

  @Override
  public void expelTeacher(User user) {
    user.setSchool(null);
    List<Classes> classesByTeacher = this.classRepository.findClassesByTeacher(user);
    for (Classes classes : classesByTeacher) {
      classes.setTeacher(null);
    }
    Set<Role> roles = user.getRoles();
    roles.removeIf(role -> role.getName().equals("ROLE_TEACHER"));
    Set<SchoolRegistration> schoolRegistrations = user.getSchoolRegistrations();
    for (SchoolRegistration schoolRegistration : schoolRegistrations) {
      if (schoolRegistration.getStatus().equals(Status.APPROVED.name())) {
        schoolRegistration.setStatus(Status.EXPELLED.name());
        schoolRegistration.setVerification_date(LocalDate.now());
      }
    }
  }

  @Override
  public void expelStudent(User user) {
    user.setSchool(null);
    user.setClasses(null);
    Set<Role> roles = user.getRoles();
    roles.removeIf(role -> role.getName().equals("ROLE_STUDENT"));
    Set<SchoolRegistration> schoolRegistrations = user.getSchoolRegistrations();
    for (SchoolRegistration schoolRegistration : schoolRegistrations) {
      if (schoolRegistration.getStatus().equals(Status.APPROVED.name())) {
        schoolRegistration.setStatus(Status.EXPELLED.name());
        schoolRegistration.setVerification_date(LocalDate.now());
      }
    }
    Set<ClassRegistration> classRegistrations = user.getClassRegistrations();
    for (ClassRegistration registration : classRegistrations) {
      if (registration.getStatus().equals(Status.APPROVED.name())) {
        registration.setStatus(Status.EXPELLED.name());
        registration.setVerification_date(LocalDate.now());
      }
    }
  }

  @Override
  public void deleteUserById(Long id) {
    this.repository.deleteById(id);
  }

  @Override
  public UserDetails loadUserByUsername(String credential) throws UsernameNotFoundException {
    if (userExistsByEmail(credential)) {
      return findUserByEmail(credential)
          .orElseThrow(() -> new UsernameNotFoundException("Email " + credential + "is not found"));
    }
    return (findUserByUsername(credential)
        .orElseThrow(() -> new UsernameNotFoundException("Username " + credential + "is not found")));
  }
}

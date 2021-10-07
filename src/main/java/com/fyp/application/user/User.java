package com.fyp.application.user;

import com.fyp.application.classes.Classes;
import com.fyp.application.classes.classRegistration.ClassRegistration;
import com.fyp.application.classes.subjectTeacher.SubjectTeacher;
import com.fyp.application.role.Role;
import com.fyp.application.school.School;
import com.fyp.application.school.schoolRegistration.SchoolRegistration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;


@Entity
@Table(name = "users")
public class User implements UserDetails {

  @Id
  @SequenceGenerator(
      name = "user_sequence",
      sequenceName = "user_sequence",
      allocationSize = 1
  )
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "user_sequence"
  )
  @Column(name = "user_id")
  private Long id;

  private Long version;
  private String username;
  private String email;
  private String password;
  private String firstName;
  private String lastName;
  private String gender;
  private String address;
  private String phoneNumber;
  private LocalDate birthday;

  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
  @JoinTable(
      name = "users_roles",
      joinColumns = {
          @JoinColumn(
              name = "user_id", referencedColumnName = "user_id",
              nullable = false
          ),
      },
      inverseJoinColumns = {
          @JoinColumn(
              name = "role_id", referencedColumnName = "role_id",
              nullable = false
          )
      }
  )
  private Set<Role> roles = new HashSet<>();

  /*
 mappedBy should be referring to the variable name at other related class
  */
  @OneToMany(mappedBy = "createdBy",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL)
  private Set<School> createdSchools;

  // for principal or owner of the school/institute
  @OneToMany(mappedBy = "principal",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL)
  private Set<School> schools;

  @OneToMany(mappedBy = "applicant",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL)
  private Set<SchoolRegistration> schoolRegistrations;

  // differentiate by using role of the user
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "school_id")
  private School school;

  @OneToMany(mappedBy = "teacher",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL)
  private Set<Classes> classesInCharge;

  // for students
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "class_id")
  private Classes classes;

  @OneToMany(mappedBy = "applicant",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL)
  private Set<ClassRegistration> classRegistrations;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "teacherInCharge")
  private Set<SubjectTeacher> subjectTeacherSet;

  public User() {
  }

  public User(Long version, String username, String email, String password,
              String firstName, String lastName, String gender,
              String address, String phoneNumber, LocalDate birthday) {
    this.version = version;
    this.username = username;
    this.email = email;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.gender = gender;
    this.address = address;
    this.phoneNumber = phoneNumber;
    this.birthday = birthday;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    for (Role role : roles) {
      authorities.add(new SimpleGrantedAuthority(role.getName()));
    }
    return authorities;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getFullName() {
    return firstName + " " + lastName;
  }

  public Gender getGender() {
    return Gender.valueOf(gender);
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public LocalDate getBirthday() {
    return birthday;
  }

  public void setBirthday(LocalDate birthday) {
    this.birthday = birthday;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  public Set<School> getCreatedSchools() {
    return createdSchools;
  }

  public void setCreatedSchools(Set<School> createdSchools) {
    this.createdSchools = createdSchools;
  }

  public Set<School> getSchools() {
    return schools;
  }

  public void setSchools(Set<School> schools) {
    this.schools = schools;
  }

  public Set<SchoolRegistration> getSchoolRegistrations() {
    return schoolRegistrations;
  }

  public void setSchoolRegistrations(Set<SchoolRegistration> schoolRegistrations) {
    this.schoolRegistrations = schoolRegistrations;
  }

  public School getSchool() {
    return school;
  }

  public void setSchool(School school) {
    this.school = school;
  }

  public Classes getClasses() {
    return classes;
  }

  public void setClasses(Classes classes) {
    this.classes = classes;
  }

  public Set<Classes> getClassesInCharge() {
    return classesInCharge;
  }

  public void setClassesInCharge(Set<Classes> classesInCharge) {
    this.classesInCharge = classesInCharge;
  }

  public Set<ClassRegistration> getClassRegistrations() {
    return classRegistrations;
  }

  public void setClassRegistrations(Set<ClassRegistration> classRegistrations) {
    this.classRegistrations = classRegistrations;
  }

  public Set<SubjectTeacher> getSubjectTeacherSet() {
    return subjectTeacherSet;
  }

  public void setSubjectTeacherSet(Set<SubjectTeacher> subjectTeacherSet) {
    this.subjectTeacherSet = subjectTeacherSet;
  }
}

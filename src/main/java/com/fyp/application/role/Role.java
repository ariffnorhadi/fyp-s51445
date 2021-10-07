package com.fyp.application.role;

import com.fyp.application.school.schoolRegistration.SchoolRegistration;
import com.fyp.application.user.User;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {

  @Id
  @SequenceGenerator(
      name = "role_sequence",
      sequenceName = "role_sequence",
      allocationSize = 1
  )
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "role_sequence"
  )
  @Column(name = "role_id")
  private Long id;

  private String name;
  private String description;

  @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
  private Set<User> users = new HashSet<>();

  @OneToMany(mappedBy = "appliedPost",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL)
  private Set<SchoolRegistration> schoolRegistrations;

  public Role() {
  }

  public Role(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Set<User> getUsers() {
    return users;
  }

  public void setUsers(Set<User> users) {
    this.users = users;
  }

  public Set<SchoolRegistration> getSchoolRegistrations() {
    return schoolRegistrations;
  }

  public void setSchoolRegistrations(Set<SchoolRegistration> schoolRegistrations) {
    this.schoolRegistrations = schoolRegistrations;
  }
}

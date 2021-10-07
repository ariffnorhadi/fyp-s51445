package com.fyp.application.user.web;

import com.fyp.application.user.EditUserParameters;
import com.fyp.application.user.Gender;
import com.fyp.application.user.User;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@NotExistingUserWhenEditing
public class EditUserFormData {

  private Long id;
  private Long version;
  private String username;

  @NotBlank
  @Email
  private String email;
  @NotBlank
  private String password;
  @NotBlank
  @Size(min = 1, max = 200)
  private String firstName;
  @NotBlank
  @Size(min = 1, max = 200)
  private String lastName;
  @NotNull
  private Gender gender;
  @NotBlank
  @Pattern(regexp = "[0-9.\\-() x/+]+")
  private String phoneNumber;
  private String address;
  @NotNull
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate birthday;

  /*
  This method is used when adding attributes when editing user
  Find user by id, if exist then fill class instance with user's details as below
   */
  public static EditUserFormData fromUser(User user) {
    EditUserFormData result = new EditUserFormData();
    result.setId(user.getId());
    result.setVersion(user.getVersion());
    result.setGender(user.getGender());
    result.setFirstName(user.getFirstName());
    result.setLastName(user.getLastName());
    result.setEmail(user.getEmail());
    result.setPassword(user.getPassword());
    result.setAddress(user.getAddress());
    result.setPhoneNumber(user.getPhoneNumber());
    result.setBirthday(user.getBirthday());

    return result;
  }

  public EditUserParameters toParameters() {
    return new EditUserParameters(getVersion(), getUsername(), getEmail(),
        getPassword(), getFirstName(), getLastName(), getGender(),
        getPhoneNumber(), getAddress(), getBirthday());
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

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
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

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public LocalDate getBirthday() {
    return birthday;
  }

  public void setBirthday(LocalDate birthday) {
    this.birthday = birthday;
  }
}

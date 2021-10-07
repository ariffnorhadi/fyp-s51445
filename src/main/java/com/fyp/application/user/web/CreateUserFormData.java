package com.fyp.application.user.web;

import com.fyp.application.user.CreateUserParameters;
import com.fyp.application.user.Gender;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@NotExistingUsername
@NotExistingUser
@PasswordMatch
public class CreateUserFormData {

  @NotBlank
  private String username;
  @NotBlank
  @Email
  private String email;
  @NotBlank
  private String password;
  @NotBlank
  private String repeatedPassword;
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
  @DateTimeFormat(pattern = "dd-MM-yyyy")
  private LocalDate birthday;

  public CreateUserFormData() {
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

  public String getRepeatedPassword() {
    return repeatedPassword;
  }

  public void setRepeatedPassword(String repeatedPassword) {
    this.repeatedPassword = repeatedPassword;
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

  public CreateUserParameters toParameters() {
    return new CreateUserParameters(username, email, password,
        firstName, lastName, gender, phoneNumber, address, birthday);
  }
}

package com.fyp.application.school.web;

import com.fyp.application.school.CreateSchoolParameters;
import com.fyp.application.user.User;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

public class CreateSchoolFormData {

  @NotBlank
  private String name;
  private String description;
  @NotBlank
  private String address;
  private User createdBy;
  private LocalDate createdOn;
  private User principal;

  public CreateSchoolFormData() {
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

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public User getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(User createdBy) {
    this.createdBy = createdBy;
  }

  public LocalDate getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(LocalDate createdOn) {
    this.createdOn = createdOn;
  }

  public User getPrincipal() {
    return principal;
  }

  public void setPrincipal(User principal) {
    this.principal = principal;
  }

  public CreateSchoolParameters toParameters() {
    return new CreateSchoolParameters(name, description,
        address, createdBy, createdOn, principal);
  }
}

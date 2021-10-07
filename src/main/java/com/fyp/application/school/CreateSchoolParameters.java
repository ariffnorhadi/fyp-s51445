package com.fyp.application.school;

import com.fyp.application.user.User;

import java.time.LocalDate;

public class CreateSchoolParameters {

  private String name;
  private String description;
  private String address;
  private User createdBy;
  private LocalDate createdOn;
  private User principal;

  public CreateSchoolParameters() {
  }

  public CreateSchoolParameters(String name, String description,
                                String address, User createdBy,
                                LocalDate createdOn, User principal) {
    this.name = name;
    this.description = description;
    this.address = address;
    this.createdBy = createdBy;
    this.createdOn = createdOn;
    this.principal = principal;
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
}

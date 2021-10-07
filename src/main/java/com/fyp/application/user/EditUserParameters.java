package com.fyp.application.user;

import java.time.LocalDate;

public class EditUserParameters extends CreateUserParameters {

  private final Long version;

  public EditUserParameters(Long version, String username,
                            String email, String password,
                            String firstName, String lastName,
                            Gender gender, String phoneNumber,
                            String address, LocalDate birthday) {
    super(username, email, password, firstName, lastName, gender,
        phoneNumber, address, birthday);
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }

  public void update(User user) {
    user.setGender(getGender().toString());
    user.setFirstName(getFirstName());
    user.setLastName(getLastName());
    user.setEmail(getEmail());
    user.setPassword(getPassword());
    user.setAddress(getAddress());
    user.setPhoneNumber(getPhoneNumber());
    user.setBirthday(getBirthday());
  }
}

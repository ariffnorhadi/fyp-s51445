package com.fyp.application.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(Long userId) {
    super(String.format("User with id %s not found", userId));
  }
}

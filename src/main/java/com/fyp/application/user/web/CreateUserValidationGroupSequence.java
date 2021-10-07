package com.fyp.application.user.web;

import com.fyp.application.infrastructure.validation.ValidationGroupOne;
import com.fyp.application.infrastructure.validation.ValidationGroupTwo;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

@GroupSequence({Default.class, ValidationGroupOne.class, ValidationGroupTwo.class})
public interface CreateUserValidationGroupSequence {
}

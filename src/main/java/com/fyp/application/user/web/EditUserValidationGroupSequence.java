package com.fyp.application.user.web;

import com.fyp.application.infrastructure.validation.ValidationGroupOne;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

@GroupSequence({Default.class, ValidationGroupOne.class})
public interface EditUserValidationGroupSequence {
}

package com.fyp.application.infrastructure.web;

import com.fyp.application.user.Gender;
import com.fyp.application.user.UserService;
import com.fyp.application.user.web.CreateUserFormData;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class LoginController {

  private final UserService service;
  private final PasswordEncoder passwordEncoder;

  public LoginController(UserService userService,
                         PasswordEncoder passwordEncoder) {
    this.service = userService;
    this.passwordEncoder = passwordEncoder;
  }

  @GetMapping("/login")
  public String login() {
    return "login";
  }

  // tag::register-get[]
  @GetMapping("/signup")
  public String registerUserForm(Model model) {
    model.addAttribute("user", new CreateUserFormData());
    model.addAttribute("genders", List.of(Gender.MALE, Gender.FEMALE, Gender.OTHER));
    model.addAttribute("editMode", EditMode.CREATE); //<.>
    return "users/register";
  }
  // end::register-get[]

  // tag::create-post[]
  @PostMapping("/signup")
  public String doRegisterUser(@Validated @ModelAttribute("user") CreateUserFormData formData,
                               BindingResult bindingResult, Model model,
                               RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("genders", List.of(Gender.MALE, Gender.FEMALE, Gender.OTHER));
      model.addAttribute("editMode", EditMode.CREATE);
      return "users/register";
    }

    formData.setPassword(passwordEncoder.encode(formData.getPassword()));

    service.saveUser(formData.toParameters());
    redirectAttributes.addFlashAttribute("registeredFullName", formData.getUsername());
    return "redirect:/login";
  }
  // end::create-post[]
}

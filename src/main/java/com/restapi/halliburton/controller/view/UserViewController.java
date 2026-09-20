package com.restapi.halliburton.controller.view;

import com.restapi.halliburton.dto.CreateUserRequest;
import com.restapi.halliburton.dto.UserPatchRequest;
import com.restapi.halliburton.dto.UserResponse;
import com.restapi.halliburton.exception.UserNotFoundException;
import com.restapi.halliburton.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Hidden
@Controller
@RequestMapping("/users")
public class UserViewController {

    private final UserService userService;

    public UserViewController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", userService.findAll());
        if (!model.containsAttribute("newUser")) {
            model.addAttribute("newUser", new CreateUserRequest("", ""));
        }
        return "users";
    }

    @PostMapping
    public String add(@Valid @ModelAttribute("newUser") CreateUserRequest form,
                      BindingResult result,
                      Model model,
                      RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("users", userService.findAll());
            return "users";
        }

        userService.create(form);
        redirect.addFlashAttribute("message", "User added.");
        return "redirect:/users";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        UserResponse user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("editForm", new UserPatchRequest(user.name(), user.email()));
        return "edit";
    }


    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("editForm") UserPatchRequest form,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("user", userService.findById(id));
            return "edit";
        }

        userService.patch(id, form);
        redirect.addFlashAttribute("message", "User updated.");
        return "redirect:/users";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        userService.delete(id);
        redirect.addFlashAttribute("message", "User deleted.");
        return "redirect:/users";
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String handleNotFound(UserNotFoundException ex, RedirectAttributes redirect) {
        redirect.addFlashAttribute("error", ex.getMessage());
        return "redirect:/users";
    }
}
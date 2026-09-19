package com.restapi.halliburton.controller;

import com.restapi.halliburton.repository.UserRepository;
import com.restapi.halliburton.entity.UserEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserViewController {

    private final UserRepository userRepository;

    public UserViewController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "users";
    }


    @PostMapping
    public String add(@RequestParam(value = "name", defaultValue = "Unknown") String name,
                      @RequestParam(value = "email", required = false) String email,
                      RedirectAttributes redirect) {

        UserEntity user = new UserEntity(name.isBlank() ? "Unknown" : name);

        if (email != null && !email.isBlank()) {
            user.setEmail(email);
        }

        userRepository.save(user);
        redirect.addFlashAttribute("message",
                String.format("Hello, %s! Your data has been added and saved to the database.", user.getName()));
        return "redirect:/users";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        Optional<UserEntity> entityOptional = userRepository.findById(id);

        if (entityOptional.isEmpty()) {
            redirect.addFlashAttribute("error", String.format("User with ID %s not found.", id));
            return "redirect:/users";
        }

        model.addAttribute("user", entityOptional.get());
        return "edit";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @RequestParam(value = "name", required = false) String name,
                         @RequestParam(value = "email", required = false) String email,
                         RedirectAttributes redirect) {

        Optional<UserEntity> entityOptional = userRepository.findById(id);

        if (entityOptional.isEmpty()) {
            redirect.addFlashAttribute("error", String.format("User with ID %s not found.", id));
            return "redirect:/users";
        }

        UserEntity user = entityOptional.get();

        // Same rule as the REST version: only update fields that are provided and not blank
        if (name != null && !name.isBlank()) {
            user.setName(name);
        }

        if (email != null && !email.isBlank()) {
            user.setEmail(email);
        }

        userRepository.save(user);
        redirect.addFlashAttribute("message", String.format("User with ID %d successfully patched.", id));
        return "redirect:/users";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        Optional<UserEntity> entityOptional = userRepository.findById(id);

        if (entityOptional.isPresent()) {
            userRepository.delete(entityOptional.get());
            redirect.addFlashAttribute("message", String.format("User with ID %s has been deleted.", id));
        } else {
            redirect.addFlashAttribute("error", String.format("User with ID %s not found.", id));
        }

        return "redirect:/users";
    }
}

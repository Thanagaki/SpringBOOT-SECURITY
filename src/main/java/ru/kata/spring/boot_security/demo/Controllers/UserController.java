package ru.kata.spring.boot_security.demo.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getPage(){
        return "welcomePage";
    }


    @GetMapping("/user/registry")
    public String registryNewUser(@ModelAttribute("newUser") User user) {
        return "registry-form";
    }


    @PostMapping("/user/newUser")
    public String saveNewUser(@ModelAttribute("newUser") User user){
        if (!user.getPassword().equals(user.getPasswordConfirm())) {

            return "registry-form";
        }
        if (!userService.saveUser(user)){
            return "registry-form";
        }
        userService.saveUser(user);
        return "redirect:/login";
    }
    @GetMapping("/user")
    public String getUserPage(Principal principal,Model model) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "user";
    }






}

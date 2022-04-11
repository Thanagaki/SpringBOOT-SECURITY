package ru.kata.spring.boot_security.demo.Controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;

@RequestMapping("/admin")
@Controller
public class AdminController {

    private final UserService userService;
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String allUsers(Model model) {
        model.addAttribute("adminAllUsers", userService.findAll());
        return "adminAllUsers";
    }


    @GetMapping("/user/new")
    public String newUser(@ModelAttribute("newUser") User emptyUser){
        return "adminCreateUser";
    }

    @PostMapping()
    public String create(@ModelAttribute("newUser") User user){
        if(!user.getPassword().equals(user.getPasswordConfirm())){
            return "adminCreateUser";
        }
        if(!userService.saveUser(user)){
            return "adminCreateUser";
        }
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/edit/user/{id}")
    public String getUserToUpdate(@PathVariable(value = "id") Long id, Model model){
        model.addAttribute("editUser", userService.findById(id));
    return "adminEditUser";
    }

    @PostMapping("/user/{id}")
    public String update(@ModelAttribute("editUser") User user, @PathVariable(value = "id") Long id) {
        userService.edit(user, id);
        if(!user.getPassword().equals(user.getPasswordConfirm())) {
            return "adminEditUser" ;
        }
      if(!userService.saveUser(user)){
          return "adminEditUser" ;
      }
        return "redirect:/admin";
    }

   @GetMapping("/delete/user/{id}")
    public String delete(@PathVariable(value = "id") Long id ){
        userService.delete(id);
        return "redirect:/admin";
   }
}

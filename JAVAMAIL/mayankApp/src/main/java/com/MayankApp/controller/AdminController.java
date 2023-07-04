package com.MayankApp.controller;

import com.MayankApp.entity.UserDetails;
import com.MayankApp.repository.UserRepository;
import com.MayankApp.service.UserService;
import jakarta.transaction.Transactional;
import org.apache.tomcat.util.http.parser.Authorization;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class AdminController {
    @Autowired
    private UserService userService;

    @GetMapping("/admin/users")
    public String getCourses(Model model) {
        model.addAttribute("users",userService.allUser());
//        return new ResponseEntity<>(userService.allUser(), HttpStatus.OK);
          return "main";
    }

    @PostMapping("/admin/add")
    public String add(@RequestBody UserDetails user) {return userService.signUp(user);}

    @Transactional
    @DeleteMapping ("admin/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        return userService.deleteUser(id);
    }

    @Transactional
    @PutMapping("admin/update/{id}")
    public String update(@PathVariable("id") Long id, @RequestBody UserDetails user) {return userService.userUpdate(id, user);
    }
}

package com.MayankApp.controller;
import com.MayankApp.entity.UserDetails;
import com.MayankApp.repository.UserRepository;
import com.MayankApp.service.JavaMailService;
import com.MayankApp.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository repo;
    @Autowired
    private JavaMailService service;

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String register(@ModelAttribute UserDetails user, RedirectAttributes redirAttrs, String siteURL, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
        UserDetails storedUserDetails = repo.findByEmail(user.getEmail());


        if (storedUserDetails != null) {
            redirAttrs.addFlashAttribute("error", "Email already exists.");
            return "redirect:/";
        }
        if ((user.getPassword()).equals(user.getPassword2())) {
            user.setRole("ROLE_USER");
//            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
//            String encryptPassword = encoder.encode(user.getPassword());
//            user.setPassword(encryptPassword);
//            user.setPassword2(encryptPassword);
//            byte[] array = new byte[64]; // length is bounded by 7
//            new Random().nextBytes(array);
//            String randomCode = new String(array, Charset.forName("UTF-8"));
////            String randomCode = RandomString.make(64);
//            user.setVerificationCode(randomCode);
//            user.setEnabled(false);
//            repo.save(user);
            service.register(user, getSiteURL(request));
            redirAttrs.addFlashAttribute("error", "Registration successful.Please verify.");
            return "redirect:/";
        } else {
            redirAttrs.addFlashAttribute("error", "Passwords mismatch.");
            return "redirect:/";
        }
    }

    @GetMapping("/user/main")
    public String kain(Model model) {
        model.addAttribute("users",userService.allUser());
        return "main";
    }
//    @PostMapping("/user/main")
//    public String login(@ModelAttribute UserDetails userlogin, RedirectAttributes redirAttrs,@Param("code") String code) {
//        UserDetails storedUserDetails = repo.findByEmail(userlogin.getEmail());
//        if (storedUserDetails != null &&  (userlogin.getPassword().equals(storedUserDetails.getPassword()) || new BCryptPasswordEncoder(16).matches(userlogin.getPassword(), storedUserDetails.getPassword())))
//            {
////
//                if (storedUserDetails.isEnabled()) {
//                    redirAttrs.addFlashAttribute("name", storedUserDetails.getName());
//                    return "redirect:/user/main";
//                }
//                return "redirect:/user/verify";
//            }
//        else {
//            redirAttrs.addFlashAttribute("error2", "Not found or not verified.");
//            return "redirect:/login";
//        }
//    }

    @GetMapping("/user/verify")
    public String verify(@Param("code") String code){
        System.out.println(code);
        if(service.verify(code)){
            return "verify_success";
        }
        else{
            return "verify_fail";
        }
    }

//    @GetMapping("/logout")
//    public String logout() {
//        return "redirect:/login";
//    }
@GetMapping("/chatRoom")
public String chatRoom(){
    return "chatRoom";
}
}

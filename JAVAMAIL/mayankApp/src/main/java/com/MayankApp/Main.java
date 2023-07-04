package com.MayankApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
    }
//    @GetMapping("/greet")
//    public GreetResponse gree()
//    {
//        return new GreetResponse("Hello", List.of("Java","C"),new Person("Mayank",28,30000));
//    }
//
//    record Person(String name,int age,double savings)
//    {
//
//    }
//
//    record GreetResponse(String greet,List<String> favProgLang,Person person){}



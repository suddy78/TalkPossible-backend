package com.talkpossible.project;

import com.talkpossible.project.domain.dto.doctor.request.SignupRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        return "hello success!";
    }

    @PostMapping("/hello-post1")
    public String helloPost1(){
        return "테스트 성공";
    }

    @PostMapping("/hello-post2")
    public ResponseEntity<String> helloPost2(@RequestHeader long num){
        return ResponseEntity.ok("테스트 성공 / num = " + num);
    }

}
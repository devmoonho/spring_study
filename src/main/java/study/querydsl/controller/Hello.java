package study.querydsl.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * Hello
 */

// @Controller
@RestController
@RequiredArgsConstructor
public class Hello {

    // 조회
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

}
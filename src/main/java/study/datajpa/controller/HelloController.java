package study.datajpa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * HelloController
 * 
 * Entity를 노출하지 말 것 Dto로 결과 제공
 * 
 */

@RestController
@RequiredArgsConstructor
public class HelloController {

    @GetMapping("/hello")
    public String searchSomeV1() {
        return "hello";
    }
}
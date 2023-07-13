package com.musiclog.controller;

import com.musiclog.request.PostCreate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
public class PostController {

//    @PostMapping("/posts")
    public String post(@RequestParam String title, @RequestParam String content){

        log.info("title = {}, content={}",title,content);
        return "Hello World";
    }

//    @PostMapping("/posts")
    public String post(@RequestParam Map<String, String> params){

        log.info("params = {}", params);

        String title = params.get("title");

        return "Hello World";
    }

    @PostMapping("/posts")
    public String post(@RequestBody PostCreate params){

        log.info("params = {}", params.toString());


        return "Hello World";
    }



}

package com.musiclog.controller;

import com.musiclog.request.PostCreate;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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

    //데이터를 검증하는 이유

    //1. client 개발자가 실수할 수 있다. 실수로 값을 안보낼 수 있다.
    //2. client 버그로 값이 누락될 수 있다.
    //3. 외부에서 나쁜 의도로 값을 조작해서 보낼 수 있다.
    //4. DB에 값을 저장할 때 의도치 않은 오류가 발생할 수 있다.
    //5. 서버 개발자의 편안함을 위해서
    @PostMapping("/posts")
    public Map<String,String> post(@RequestBody @Valid PostCreate params)  {

//        if (result.hasErrors()) {
//            List<FieldError> fieldErrors = result.getFieldErrors();
//            FieldError firstFieldError = fieldErrors.get(0);
//            String fieldName = firstFieldError.getField();
//            String errorMessage = firstFieldError.getDefaultMessage();
//
//            Map<String, String> error = new HashMap<>();
//            error.put(fieldName, errorMessage);
//            return error;
//        }
        // client 개발자에게 오류 내용을 알려주는 방법
//        var title = params.getTitle();


        return Map.of();
    }



}

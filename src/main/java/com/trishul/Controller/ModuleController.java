package com.trishul.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ModuleController {

    @GetMapping("/")
    public String getModule(){
        return "hello World";
    }
}

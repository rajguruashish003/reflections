package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoCtrl {

    @RequestMapping(path = "/demo/hello",method = RequestMethod.GET, produces = "application/json")
    public  String DisplayInputString(@RequestParam String inputString){

        return "Provided Input string is: "+inputString;
    }
}

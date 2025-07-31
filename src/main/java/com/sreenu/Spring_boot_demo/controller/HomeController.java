package com.sreenu.Spring_boot_demo.controller;

import com.sreenu.Spring_boot_demo.model.User;
import org.springframework.web.bind.annotation.*;

@RestController
public class HomeController {

    @RequestMapping("/")
    public String home(){
        return  "Hello world!";
    }
    //@RequestMapping(value = "/user", method = RequestMethod.GET)
    @GetMapping("/user")
    public User user(){
        User user = new User();
        user.setId(1);
        user.setName("Srinivasan");
        user.setEmail("sreenivasm@gamil.com");
        return user;
    }
    @GetMapping("/{id}/{id2}")
    public String pathVariable(@PathVariable String id ,
                               @PathVariable("id2") String name){
        return "The path variable is: "+ id + ": "+ name ;
    }
    @GetMapping("/requestParam")
    public String requestParams(@RequestParam String name,
                                @RequestParam(name = "email",required = false,defaultValue = "") String emailId){
        return "Request params is: "+name + "and Email Id is: " +emailId;
    }
    @GetMapping("/{id3}/emailId")
    public String pathAndRequestVariable(@PathVariable String id3,
                                         @RequestParam String emailId){
        return "Path Variable is: "+ id3 + "and query param is: "+ emailId;
    }


}

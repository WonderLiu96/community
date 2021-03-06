package com.wonder.controller;

import com.wonder.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: wonder
 * @Date: 2019/12/29
 */

public class IndexController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);


    @RequestMapping(value = "/",method = RequestMethod.GET)
    @ResponseBody
    public String index(){
        logger.info("Visit Home!");
        return "Hello World!";
    }
    @RequestMapping(value = "/vm",method = RequestMethod.GET)
    public String template(Model model){


        model.addAttribute("value1","ccc");
        List<String> colors = Arrays.asList("RED","BLUE","GREEN");
        model.addAttribute("colors",colors);

        Map<Integer,String> map = new HashMap<>(10);
        map.put(1,"a");
        map.put(2,"b");
        model.addAttribute("map",map);

        User user = new User("Blank");
        model.addAttribute("user",user);

        return "home";
    }
    @RequestMapping(value = "/redirect/{code}",method = RequestMethod.GET)
    public String redirect(@PathVariable("code")int code,
                          HttpSession httpSession){
        httpSession.setAttribute("msg","from redirect.");
        return "redirect:/";
    }

    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e){
        return "error" + e.getMessage();
    }
}

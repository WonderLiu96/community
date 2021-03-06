package com.wonder.controller;

import com.wonder.async.EventModel;
import com.wonder.async.EventProducer;
import com.wonder.async.EventType;
import com.wonder.service.impl.UserServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


/**
 * @Author: wonder
 * @Date: 2020/1/2
 */
@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private static final String TICKET = "ticket";

    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(path = "/reg/",method = RequestMethod.POST)
    public String reg(Model model,
                      @RequestParam("username")String username,
                      @RequestParam("password")String password,
                      @RequestParam(value = "next",required = false)String next,
                      @RequestParam(value = "remember",defaultValue = "false")boolean rememberme,
                      HttpServletResponse response){
        try{
            Map<String,Object> map = userServiceImpl.register(username,password);
            if(map.containsKey(TICKET)) {
                Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                if(rememberme){
                    cookie.setMaxAge(3600 * 24 * 7);
                }
                response.addCookie(cookie);
                if(StringUtils.isNotBlank(next)){
                    return "redirect:" + next;
                }
                return "redirect:/";
            }else{
                model.addAttribute("msg",map.get("msg"));
                return "login";
            }
        }catch (Exception e){
            logger.error("注册异常:",e);
            model.addAttribute("msg","服务器异常");
            return "login";
        }
    }
    @RequestMapping(path = {"/reglogin"},method = RequestMethod.GET)
    public String regloginPage(Model model){
        return "login";
    }

    @RequestMapping(path = {"/login/"},method = RequestMethod.POST)
    public String login(Model model, @RequestParam("username")String username,
                        @RequestParam("password")String password,
                        @RequestParam(value = "next",required = false)String next,
                        @RequestParam(value = "rememberme",defaultValue = "false")boolean rememberme,
                        HttpServletResponse response){
        try{
            Map<String,Object> map = userServiceImpl.login(username,password);

            if(map.containsKey(TICKET)){
                Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                if(rememberme){
                    cookie.setMaxAge(3600 * 24 * 7);
                }
                response.addCookie(cookie);

                eventProducer.fireEvent(new EventModel(EventType.LOGIN)
                                        .setExt("username",username)
                                        .setExt("email","976563760@qq.com"));

                if(StringUtils.isNotBlank(next)){
                    return "redirect:" + next;
                }
                return "redirect:/";
            }else{
                model.addAttribute("msg",map.get("msg"));
                return "login";

            }
        }catch (Exception e){
            logger.error("登录异常:",e);
            return "login";
        }
    }
    @RequestMapping(path = {"/logout"},method = {RequestMethod.GET,RequestMethod.GET})
    public String logout(@CookieValue("ticket")String ticket){
        userServiceImpl.logout(ticket);
        return "redirect:/";
    }
}

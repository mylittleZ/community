package com.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

//controller就是说允许这个类去接收前端的请求
@Controller

public class IndexController {
    @GetMapping("/index")
   public String index(){
        //model.addAttribute("name",name);//把浏览器中传过来的值放到model里面
       return "index";//返回index.html这个模板
   }
}

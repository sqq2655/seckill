package com.sqq.seckill.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author sqq
 * @version 1.0
 * @date 2023/3/1 20:51
 */
@Controller
@RequestMapping("/demo")
public class DemoController {
    @RequestMapping("/hello")
    public String hello(Model model){
        model.addAttribute("name","123");
        return "hello";
    }
}

package top.wytbook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestTemplateController {
    @GetMapping("/hello")
    public String hello(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("hello");
        return "hello";
    }
}

package top.wytbook.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.wytbook.dto.Result;
import top.wytbook.dto.ResultEnum;

@Controller
@RequestMapping("/error")
public class MyErrorController implements ErrorController {
    @RequestMapping
    @ResponseBody
    public Result error() {
        return Result.res(ResultEnum.NOT_FOUND_SERVICE);
    }
}
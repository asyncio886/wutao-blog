package top.wytbook.exceptions;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import top.wytbook.dto.Result;
import top.wytbook.dto.ResultEnum;

import javax.validation.ValidationException;

@RestControllerAdvice
public class HandlerController {
    @ExceptionHandler(MyException.class)
    public Result handlerNotLogin(MyException myException) {
        return Result.res(myException.getResultEnum());
    }

    @ExceptionHandler(Exception.class)
    public Result handlerNormal(Exception e) {
        e.printStackTrace();
        return Result.res(ResultEnum.UNKNOWN_ERROR);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result handlerFileException() {
        return Result.res(ResultEnum.FILE_SIZE_ERROR);
    }

    @ExceptionHandler({ValidationException.class, ServletRequestBindingException.class})
    public Result handlerValidator() {
        return Result.res(ResultEnum.VALIDATOR_ERROR);
    }
}

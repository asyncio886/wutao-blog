package top.wytbook.exceptions;

import top.wytbook.dto.ResultEnum;

public abstract class MyException extends Exception {

    public abstract ResultEnum getResultEnum();

}

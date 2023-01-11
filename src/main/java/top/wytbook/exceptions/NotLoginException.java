package top.wytbook.exceptions;

import top.wytbook.dto.ResultEnum;

public class NotLoginException extends MyException{
    @Override
    public ResultEnum getResultEnum() {
        return ResultEnum.NEED_LOGIN;
    }
}

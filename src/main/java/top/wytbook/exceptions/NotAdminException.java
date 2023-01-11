package top.wytbook.exceptions;

import top.wytbook.dto.ResultEnum;

public class NotAdminException extends MyException{
    @Override
    public ResultEnum getResultEnum() {
        return ResultEnum.NOT_ADMIN;
    }
}

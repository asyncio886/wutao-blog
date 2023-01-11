package top.wytbook.exceptions;

import top.wytbook.dto.ResultEnum;

public class IpNotFoundException extends MyException{
    @Override
    public ResultEnum getResultEnum() {
        return ResultEnum.IP_NOT_FIND;
    }
}

package top.wytbook.exceptions;

import top.wytbook.dto.ResultEnum;

public class IpAttackException extends MyException{
    @Override
    public ResultEnum getResultEnum() {
        return ResultEnum.IP_ATTACK;
    }
}

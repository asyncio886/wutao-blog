package top.wytbook.dto;

import lombok.Data;

@Data
public class Result {
    Integer code;
    String msg;
    Object data;
    public static Result ok() {
        Result result = new Result();
        result.setCode(0);
        result.setMsg("ok");
        return result;
    }

    public static Result ok(Object data) {
        Result result = ok();
        result.setData(data);
        return result;
    }

    public static Result operatorOk(String message) {
        Result ok = ok();
        Operator operator = new Operator();
        operator.setMessage(message);
        operator.setOk(true);
        ok.setData(operator);
        return ok;
    }
    public static Result operatorFail(String message) {
        Result result = ok();
        Operator operator = new Operator();
        operator.setOk(false);
        operator.setMessage(message);
        result.setData(operator);
        return result;
    }

    public static Result res(ResultEnum resultEnum) {
        Result result = new Result();
        result.setCode(resultEnum.getCode());
        result.setMsg(resultEnum.getMessage());
        return result;
    }

    public static Result operatorOk(String message, Object data) {
        Result ok = ok();
        Operator operator = new Operator();
        operator.setMessage(message);
        operator.setOk(true);
        operator.setData(data);
        ok.setData(operator);
        return ok;
    }
}

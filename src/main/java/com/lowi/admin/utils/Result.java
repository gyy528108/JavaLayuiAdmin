package com.lowi.admin.utils;


import lombok.Data;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.List;

@Data
public class Result<T> {
    private Integer code;
    private String msg;
    private Integer count;
    private T data;

    public static Result getError(Errors errors) {
        String msg = null;
        List<FieldError> fieldErrors = errors.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            msg = fieldError.getDefaultMessage();
            break;
        }
        Result responseResult = new Result();
        responseResult.setCode(1);
        responseResult.setMsg(msg);
        return responseResult;
    }

    public static Result getInstance(Integer code, String msg) {
        Result responseResult = new Result();
        responseResult.setCode(code);
        responseResult.setMsg(msg);
        return responseResult;
    }

    public static Result getInstance(Integer code, String msg, Object object) {
        Result responseResult = new Result();
        responseResult.setCode(code);
        responseResult.setMsg(msg);
        responseResult.setData(object);
        return responseResult;
    }
    public static Result getInstance(Integer code, String msg,int total, Object object) {
        Result responseResult = new Result();
        responseResult.setCode(code);
        responseResult.setMsg(msg);
        responseResult.setCount(total);
        responseResult.setData(object);
        return responseResult;
    }
}

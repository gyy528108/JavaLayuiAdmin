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
}

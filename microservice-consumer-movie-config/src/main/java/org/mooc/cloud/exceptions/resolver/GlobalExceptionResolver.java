package org.mooc.cloud.exceptions.resolver;

import org.mooc.cloud.exceptions.BusinessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;



@RestControllerAdvice
public class GlobalExceptionResolver {

    @ExceptionHandler(value = {BusinessException.class})
    public String handle(HttpServletRequest request, Exception e){

        return null;
    }

}

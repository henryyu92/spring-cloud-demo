package org.mooc.cloud.exceptions.resolver;

import org.mooc.cloud.exceptions.BusinessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : henry
 * @version : 1.0
 * @Description :
 * @Copyright : Sinaif All Rights Reserved
 * @Company : 海南新浪爱问普惠科技有限公司-富鱼
 * @Create Date : 2018/9/6 10:49
 */

@RestControllerAdvice
public class GlobalExceptionResolver {

    @ExceptionHandler(value = {BusinessException.class})
    public String handle(HttpServletRequest request, Exception e){

        return null;
    }

}

package example.web.servlet.exception;

import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 全局异常处理
 */
public class GlobalExceptionHandler {


    @ExceptionHandler(BindException.class)
    public void handleBindException(){

    }
}

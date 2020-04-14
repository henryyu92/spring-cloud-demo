package org.mooc.cloud.web;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class MetricAspect {

    private Logger logger = LoggerFactory.getLogger(getClass());

    ThreadLocal<Long> start = new ThreadLocal<>();

    /**
     * 切面
     */
    @Pointcut("execution(public * org.mooc.cloud.controller..*.*(..))")
    public void webLog(){}

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint){
        start.set(System.currentTimeMillis());
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        logger.info("Aspect-URL: {}", request.getRequestURI().toLowerCase());
        logger.info("Aspect-HTTP_METHOD: {}", request.getMethod());
        logger.info("Aspect-IP: {}", request.getRemoteAddr());
        logger.info("Aspect-REQUEST_METHOD: {}", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logger.info("Aspect-Args: {}", Arrays.toString(joinPoint.getArgs()));
    }

    @After("webLog()")
    public void doAfter(){

    }


    public void doAfterReturning(Object ret) throws Throwable{
        logger.info("Aspect-Response: {}", ret);
        logger.info("Aspect-SpendTime: {}ms", System.currentTimeMillis() - start.get());
    }
}

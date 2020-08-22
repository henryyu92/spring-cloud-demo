package example.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class DoneTimeAspect {

    @Around("@annotation(doneTime)")
    public Object around(ProceedingJoinPoint joinPoint, DoneTime doneTime) throws Throwable{
        long start = System.nanoTime() / 1000;
        Object proceed = joinPoint.proceed();
        long end = System.nanoTime() / 1000;
        System.out.println(end - start);

        return proceed;
    }
}

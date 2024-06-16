package star_wars_card_collector.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* star_wars_card_collector.service.*.*(..))")
    public void logBeforeServiceMethods(JoinPoint joinPoint) {
        System.out.println("Before method: " + joinPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "execution(* star_wars_card_collector.service.*.*(..))", returning = "result")
    public void logAfterServiceMethods(JoinPoint joinPoint, Object result) {
        System.out.println("After method: " + joinPoint.getSignature().getName() + ", returned value: " + result);
    }
}

package org.example.serveremulator.Loggin;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // точка перехвата для всех контроллеров
    @Pointcut("within(org.example.serveremulator.Controllers..*)")
    public void controllerPointcut() {}

    // точка перехвата для всех сервисов
    @Pointcut("within(org.example.serveremulator.Services..*)")
    public void servicePointcut() {}

    // оборачиваем выполнение перехваченных методов
    @Around("controllerPointcut() || servicePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        // логируем вход в метод и его аргументы
        logger.info("вход: {}.{}() с аргументами = {}", className, methodName, Arrays.toString(args));

        try {
            // запускаем сам метод
            Object result = joinPoint.proceed();

            long elapsedTime = System.currentTimeMillis() - startTime;

            // логируем успешный выход и время выполнения
            logger.info("выход: {}.{}() с результатом = {} (время выполнения: {} мс)",
                    className, methodName, result, elapsedTime);

            return result;
        } catch (Exception e) {
            // логируем ошибку, если метод упал
            logger.error("ошибка: в {}.{}() причина = {}", className, methodName, e.getMessage());
            throw e;
        }
    }
}
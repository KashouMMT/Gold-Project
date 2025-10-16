package com.dev.main.utils;


import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
	
	private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
	
	@Before("execution(* com.dev.main..*(..))")
	public void logMethodEntry(JoinPoint joinPoint) {
		logger.debug("Entering: {} with args: {}",joinPoint.getSignature(), Arrays.toString(joinPoint.getArgs()));
	}
	
	@AfterReturning(pointcut = "execution(* com.dev.main..*(..))",returning = "result")
	public void logMethodExit(JoinPoint joinPoint, Object result) {
		logger.debug("Exiting: {} with result: {}",joinPoint.getSignature(),result);
	}
}

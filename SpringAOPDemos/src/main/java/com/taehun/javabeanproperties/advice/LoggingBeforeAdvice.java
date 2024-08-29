package com.taehun.javabeanproperties.advice;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

public class LoggingBeforeAdvice implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("Before method: " + method.getName());
    }
}

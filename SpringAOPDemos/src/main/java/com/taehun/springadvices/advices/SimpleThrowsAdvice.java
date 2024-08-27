package com.taehun.springadvices.advices;

import java.lang.reflect.Method;

import org.springframework.aop.ThrowsAdvice;

public class SimpleThrowsAdvice implements ThrowsAdvice {
	
	public void afterThrowing (Method method, Object[] args, Object Target, Exception e) {
		System.out.println("Exception thrown in method: " + method.getName() +
				", exception: " + e.getMessage());
	}
	
	
}

package com.taehun.springadvices.advices;

import java.lang.reflect.Method;

import org.springframework.aop.MethodBeforeAdvice;

public class CountingBeforeAdvice implements MethodBeforeAdvice {
	
	private int count;
	
	@Override
	public void before (Method method, Object[] args, Object target) throws Throwable {
		count++;
		System.out.println("Before method: " + 
		method.getName() +  ", count=" + count);
	}
	
	public int getCount() {
		return count;
	}
}

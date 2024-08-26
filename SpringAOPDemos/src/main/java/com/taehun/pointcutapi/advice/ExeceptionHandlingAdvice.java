package com.taehun.pointcutapi.advice;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class ExeceptionHandlingAdvice implements MethodInterceptor {
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		
		try {
			return invocation.proceed();
		} catch (Exception e) {
			System.out.println("Exeception caught in method: " +
				invocation.getMethod().getName() +
				", exception: " + e.getMessage()); 
			throw e;
		}
	}
}

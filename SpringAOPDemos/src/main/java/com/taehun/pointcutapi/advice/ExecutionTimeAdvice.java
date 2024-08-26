package com.taehun.pointcutapi.advice;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class ExecutionTimeAdvice implements MethodInterceptor {
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		long startTimeMills = System.currentTimeMillis();
		try {
			invocation.proceed();
		} finally {
			long timeTakeMills = System.currentTimeMillis() - startTimeMills;
			System.out.println(
					"Execution time of " + invocation.getMethod().getName() +
					" :: " + timeTakeMills + "ms");
		}
			
		
		return null;
	}
}

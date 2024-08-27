package com.taehun.springadvices.advices;

import org.aopalliance.intercept.MethodInvocation;
import org.aopalliance.intercept.MethodInterceptor;

public class DebugInterceptor implements MethodInterceptor {
	
	@Override
	public Object invoke (MethodInvocation invocation) throws Throwable {
		System.out.println("Before: invocation=[" + invocation + "]");
		Object retVal = invocation.proceed();
		System.out.println("Invocation returned with value: " + retVal);
		return retVal;
	}
}

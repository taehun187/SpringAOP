package com.taehun.springadvices.advices.introduction;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.DelegatingIntroductionInterceptor;

// delegate : 도입될 인터페이스의 메서드를 실제로 구현
public class LockMixin extends DelegatingIntroductionInterceptor implements Lockable {
	
	private boolean locked; 
	
	@Override
	public void lock() {
		this.locked = true;
	}
	
	@Override
	public void unlock() {
		this.locked = false;
	}
	  
	@Override
	public boolean locked() {                                          
		return this.locked();
	}
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		if (locked() && invocation.getMethod().getName().startsWith("set")) {
			throw new LockedException();
		}
		
		return super.invoke(invocation);
	}
}

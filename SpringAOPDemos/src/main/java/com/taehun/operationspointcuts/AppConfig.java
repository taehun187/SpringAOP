package com.taehun.operationspointcuts;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.Pointcuts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

@Configuration
@Aspect
public class AppConfig {
	
	private static Pointcut pointcutForMethodA() {
		return new StaticMethodMatcherPointcut() {
			
			@Override
			public boolean matches(Method method, Class<?> targetClass) {
				return "methodA".equals(method.getName());
			}
		};
	}
	
	private static Pointcut pointcutForMethodB() {
		return new StaticMethodMatcherPointcut() {
			
			@Override
			public boolean matches(Method method, Class<?> targetClass) {
				return "methodB".equals(method.getName());
			}
		};
	}
	                   
	private static Pointcut unionPointcut() {
		return Pointcuts.union(pointcutForMethodA() , pointcutForMethodB());
	}
	
	private static Pointcut intersectionPointcut() {
		return Pointcuts.intersection(pointcutForMethodA(), pointcutForMethodB());
	}
	
	@Bean
	public SimpleService simpleService() {
		ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
		proxyFactoryBean.setTarget(new SimpleService());
	
		org.aopalliance.aop.Advice advice = 
				new MethodInterceptor() {
			
					@Override
					public Object invoke(MethodInvocation invocation) throws Throwable{
						System.out.println("Additional advice before unionPointcut method");
						return invocation.proceed();
					}			
			                            
		};
		
		DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(unionPointcut(), advice);
		
		proxyFactoryBean.addAdvisor(advisor);
		
		return (SimpleService) proxyFactoryBean.getObject();
	} 	
	
}

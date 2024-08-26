package com.taehun.pointcutapi.config;

import org.springframework.aop.Pointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.taehun.pointcutapi.advice.ExeceptionHandlingAdvice;
import com.taehun.pointcutapi.advice.ExecutionTimeAdvice;
import com.taehun.pointcutapi.advice.LoggingAdvice;
import com.taehun.pointcutapi.pointcut.CustomPointCut;
import com.taehun.pointcutapi.service.AnotherService;
import com.taehun.pointcutapi.service.MyService;

@Configuration
@EnableAspectJAutoProxy
public class AppConfigForEnableAspectJAutoProxy {
	
	@Bean
	public MyService myService() {
		return new MyService();
	}
	
	@Bean
	public AnotherService anotherService() {
		return new AnotherService(); 
	}
	
	
	@Bean
	public Pointcut customPointCut() {
		return new CustomPointCut();
	}
	
	
	@Bean
	public Pointcut aspectJPointcut() {
		AspectJExpressionPointcut pointcut = 
				new AspectJExpressionPointcut();
		pointcut.setExpression(
				"execution(* com.taehun.pointcutapi.service.Anotherservice.myMethod(..))"
					);
		return pointcut;
	}
	
	@Bean
	public DefaultPointcutAdvisor aspectJLoggingAdvisor(
			@Qualifier("aspectJpointCut") Pointcut pointcut) {
		return new DefaultPointcutAdvisor(pointcut, new LoggingAdvice());
	}
	
	
	@Bean
	public DefaultPointcutAdvisor loggingAdvisor(
			@Qualifier("customPointCut") Pointcut pointcut) {
		return new DefaultPointcutAdvisor(pointcut, new LoggingAdvice());
	}
	
	
	@Bean
	public DefaultPointcutAdvisor executionTimeAdvisor(
			@Qualifier("customPointCut") Pointcut pointcut) {
		return new DefaultPointcutAdvisor(pointcut, new ExecutionTimeAdvice());
	}
	
	
	@Bean
	public DefaultPointcutAdvisor execeptionHandlingAdvisor(
			@Qualifier("customPointCut") Pointcut pointcut) {
		return new DefaultPointcutAdvisor(pointcut, new ExeceptionHandlingAdvice());
	}
	
	
}

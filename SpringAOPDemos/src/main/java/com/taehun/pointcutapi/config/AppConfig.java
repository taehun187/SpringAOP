package com.taehun.pointcutapi.config;

import org.springframework.aop.Pointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import com.taehun.pointcutapi.advice.ExeceptionHandlingAdvice;
import com.taehun.pointcutapi.advice.ExecutionTimeAdvice;
import com.taehun.pointcutapi.advice.LoggingAdvice;
import com.taehun.pointcutapi.pointcut.CustomPointCut;
import com.taehun.pointcutapi.service.AnotherService;
import com.taehun.pointcutapi.service.MyService;

@Configuration
public class AppConfig {
	
	@Bean
	public MyService myService() {
		return new MyService();
	}
	
	@Bean
	public AnotherService anotherService() {
		return new AnotherService(); 
	}
	
	@Lazy
	@Bean
	public Pointcut customPointCut() {
		return new CustomPointCut();
	}
	
	@Bean
	public Pointcut aspectJPointcut() {
	    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
	    pointcut.setExpression("execution(* com.taehun.pointcutapi.service.AnotherService.myMethod(..))");
	    return pointcut;
	}

	@Bean
	public DefaultPointcutAdvisor aspectJLoggingAdvisor(
	        @Qualifier("aspectJPointcut") Pointcut pointcut) { // 여기의 이름을 정확하게 맞춰야 함
	    return new DefaultPointcutAdvisor(pointcut, new LoggingAdvice());
	}
	
	@Lazy
	@Bean
	public DefaultPointcutAdvisor loggingAdvisor(
			@Qualifier("customPointCut") Pointcut pointcut) {
		return new DefaultPointcutAdvisor(pointcut, new LoggingAdvice());
	}
	
	@Lazy
	@Bean
	public DefaultPointcutAdvisor executionTimeAdvisor(
			@Qualifier("customPointCut") Pointcut pointcut) {
		return new DefaultPointcutAdvisor(pointcut, new ExecutionTimeAdvice());
	}
	
	@Lazy
	@Bean
	public DefaultPointcutAdvisor execeptionHandlingAdvisor(
			@Qualifier("customPointCut") Pointcut pointcut) {
		return new DefaultPointcutAdvisor(pointcut, new ExeceptionHandlingAdvice());
	}
	
	@Lazy
	@Bean
	public ProxyFactoryBean myServiceProxy(
	        MyService myService,
	        @Qualifier("loggingAdvisor") DefaultPointcutAdvisor loggingAdvisor,
	        @Qualifier("executionTimeAdvisor") DefaultPointcutAdvisor executionTimeAdvisor,
	        @Qualifier("execeptionHandlingAdvisor") DefaultPointcutAdvisor execeptionHandlingAdvisor
	        ) {
	    
	    ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
	    proxyFactoryBean.setTarget(myService);
	    proxyFactoryBean.setInterceptorNames(
	            "loggingAdvisor",
	            "executionTimeAdvisor",
	            "execeptionHandlingAdvisor");
	    
	    return proxyFactoryBean;
	}
	
	@Lazy
	@Bean
	public ProxyFactoryBean anotherServiceProxy(
	        AnotherService anotherService,
	        @Qualifier("loggingAdvisor") DefaultPointcutAdvisor loggingAdvisor,
	        @Qualifier("executionTimeAdvisor") DefaultPointcutAdvisor executionTimeAdvisor,
	        @Qualifier("execeptionHandlingAdvisor") DefaultPointcutAdvisor execeptionHandlingAdvisor
	        ) {
	    
	    ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
	    proxyFactoryBean.setTarget(anotherService);
	    proxyFactoryBean.setInterceptorNames(
	            "loggingAdvisor",
	            "executionTimeAdvisor",
	            "execeptionHandlingAdvisor");
	    
	    return proxyFactoryBean;
	}

}

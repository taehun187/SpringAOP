package com.taehun.springadvices.config;

import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.taehun.springadvices.advices.CountingAfterReturningAdvice;
import com.taehun.springadvices.advices.CountingBeforeAdvice;
import com.taehun.springadvices.advices.DebugInterceptor;
import com.taehun.springadvices.advices.SimpleThrowsAdvice;
import com.taehun.springadvices.service.SimpleService;

@Configuration
public class AppConfig {
	
	@Bean
	public SimpleService simpleService() {
		return new SimpleService();
	}
	
	@Bean
	public DebugInterceptor debugInterceptor() {
		return new DebugInterceptor();
	}
	
	@Bean
	public CountingBeforeAdvice countingBeforeAdvice() {
		return new CountingBeforeAdvice();
	}
	
	@Bean
	public CountingAfterReturningAdvice countingAfterReturningAdvice() {
		return new CountingAfterReturningAdvice();
	}
	
	@Bean
	public SimpleThrowsAdvice simpleThrowsAdvice() {
		return new SimpleThrowsAdvice();
	}
	
	@Bean
	public ProxyFactoryBean proxyFactoryBean() {
		ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
		proxyFactoryBean.setTarget(simpleService());
		proxyFactoryBean.setProxyTargetClass(true);
		
		proxyFactoryBean.addAdvisor(new DefaultPointcutAdvisor(debugInterceptor()));
		proxyFactoryBean.addAdvisor(new DefaultPointcutAdvisor(countingBeforeAdvice()));
		proxyFactoryBean.addAdvisor(new DefaultPointcutAdvisor(countingAfterReturningAdvice()));
		proxyFactoryBean.addAdvisor(new DefaultPointcutAdvisor(simpleThrowsAdvice()));
		
		
		return proxyFactoryBean;
	}
	
	
	
}

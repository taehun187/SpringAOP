package com.taehun.conveniencepointcut.config;

import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.aop.support.RegexpMethodPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.taehun.conveniencepointcut.advice.SimpleAdvice;
import com.taehun.conveniencepointcut.service.SimpleService;

@Configuration
public class AppConfig2 {
	
	@Bean
	public SimpleService simpleService() {
		return new SimpleService();
	}
	
	@Bean
	public SimpleAdvice simpleAdvice() {
		return new SimpleAdvice();
	}
	
	@Bean
	public JdkRegexpMethodPointcut jdkRegexpMethodPointcut() {
		
		JdkRegexpMethodPointcut pointcut =
				new JdkRegexpMethodPointcut();
		
		pointcut.setPattern(".*get.*");
		return pointcut;
		
	}
	
	@Bean
	public DefaultPointcutAdvisor defaultPointcutAdvisor() {
		return new DefaultPointcutAdvisor(jdkRegexpMethodPointcut(),
				simpleAdvice());
	}
	
	@Bean
	public RegexpMethodPointcutAdvisor setterAndAbsquatulateAdvisor() {
		RegexpMethodPointcutAdvisor advisor = 
				new RegexpMethodPointcutAdvisor();
		advisor.setAdvice(simpleAdvice());
		advisor.setPatterns(".*set.*", ".*absquatulate.*");
		return advisor;
	}

	@Bean
	public ProxyFactoryBean proxyFactoyBean() {
		ProxyFactoryBean proxyFactoryBean = 
				new ProxyFactoryBean();         
		
		proxyFactoryBean.setTarget(simpleService());
		proxyFactoryBean.addAdvisor(null);
		proxyFactoryBean.addAdvisor(defaultPointcutAdvisor());
		
		return proxyFactoryBean;
	}
}

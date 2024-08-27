package com.taehun.springadvices;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.taehun.springadvices.config.AppConfig;
import com.taehun.springadvices.service.SimpleService;

public class Main {
	
	public static void main(String[] args) {
		ApplicationContext context =
				new AnnotationConfigApplicationContext(AppConfig.class);
		
		SimpleService service = (SimpleService)context.getBean("proxyFactoryBean");
		
		service.performTask();
		
		String greeting = service.returnGreeting("Me");
		System.out.println("Greeting: " + greeting);
		
		try { 
			service.throwException();
		} catch (Exception e) {
			System.out.println("Exception caught in main: " + e.getMessage());
		}
	}
}

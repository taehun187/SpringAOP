package com.taehun.conveniencepointcut;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.taehun.conveniencepointcut.config.AppConfig2;
import com.taehun.conveniencepointcut.service.SimpleService;

public class Program {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = 
				new AnnotationConfigApplicationContext(AppConfig2.class);
		
		SimpleService service = (SimpleService) context.getBean("proxyFactoryBean");
				
		service.setName("John");
		service.getName();
		service.absquatulate();
		service.performTask();
	}

}

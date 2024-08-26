package com.taehun.operationspointcuts;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.ApplicationContext;

public class Main {
	public static void main(String[] args) {
		
		ApplicationContext context =
				new AnnotationConfigApplicationContext(AppConfig.class);
		
		SimpleService service = context.getBean(SimpleService.class);
		
		service.methodA();
		service.methodB();
		
	}
}

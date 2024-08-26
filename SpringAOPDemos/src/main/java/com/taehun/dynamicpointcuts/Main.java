package com.taehun.dynamicpointcuts;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Main {

	public static void main(String[] args) {
		
		AnnotationConfigApplicationContext context =
				new AnnotationConfigApplicationContext(AppConfig.class);
		
		TaskCaller caller = context.getBean(TaskCaller.class);
		
		caller.callTask();
		
		SimpleService service = (SimpleService) context.getBean(SimpleService.class);
		
		
		context.getBean("proxyFactoryBean", SimpleService.class).performTask();
		
	}

}

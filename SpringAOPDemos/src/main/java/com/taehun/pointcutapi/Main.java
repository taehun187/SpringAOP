package com.taehun.pointcutapi;

import com.taehun.pointcutapi.config.AppConfig;
import com.taehun.pointcutapi.config.AppConfigForEnableAspectJAutoProxy;
import com.taehun.pointcutapi.service.AnotherService;
import com.taehun.pointcutapi.service.MyService;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

public class Main {
	
	public static void execAppConfig() {
		ConfigurableApplicationContext context = 
                new AnnotationConfigApplicationContext(AppConfig.class);
		
		MyService myService = (MyService) context.getBean("myService");
		AnotherService anotherService =
				(AnotherService) context.getBean("anotherService");
		
		myService.myMethod();
		myService.anotherMethod("test");
		
		anotherService.myMethod();		
		anotherService.differentMethod(123);
		
		try {
			myService.methodWithException();
		} catch (Exception e) {
			System.out.println("Exception handled in main");
		}
		
		context.close();
	}
	
	public static void execAppConfigForEnableAspectJAutoProxy() {
		ConfigurableApplicationContext context = 
                new AnnotationConfigApplicationContext(AppConfigForEnableAspectJAutoProxy.class);
		
		MyService myService = (MyService) context.getBean("myService");
		AnotherService anotherService =
				(AnotherService) context.getBean("anotherService");
		
		myService.myMethod();
		myService.anotherMethod("test");
		
		anotherService.myMethod();		
		anotherService.differentMethod(123);
		
		try {
			myService.methodWithException();
		} catch (Exception e) {
			System.out.println("Exception handled in main");
		}
		
		context.close();
	}
	
	
	public static void main(String[] arge) {
		
		execAppConfigForEnableAspectJAutoProxy();
		
	}
}

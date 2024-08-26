package com.taehun.conveniencepointcut;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.taehun.conveniencepointcut.config.AppConfig;
import com.taehun.conveniencepointcut.service.MyService;
import com.taehun.conveniencepointcut.service.MySpecialService;

public class Main {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context =
				new AnnotationConfigApplicationContext(AppConfig.class);
		
		MyService myService = (MyService) context.getBean("myService");
		myService.performOperation();
		
		MySpecialService mySpecialService =
				(MySpecialService) context.getBean("MySpecialService");
		mySpecialService.performSpecialOperation();
		
		context.close();
	}

}

package com.taehun.springadvices.advices.introduction;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

	public static void main(String[] args) {
		ApplicationContext context = 
				new AnnotationConfigApplicationContext(AppConfig.class);
		
		MyTargetClass myObject = 
				context.getBean(MyTargetClass.class);
		
		Lockable lockable = (Lockable) myObject;
		
		if (!lockable.locked()) {
			myObject.setName("hello");
			lockable.lock();
		} else {
			try {                            
				myObject.setName("world");
			} catch ( LockedException e) {
				System.out.println(e.getMessage());
			}
		}
		
		try {
            // 여기에 추가적인 작업을 수행해야 하는 경우 코드 작성
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
package com.taehun.pointcutapi.pointcut;

import java.lang.reflect.Method;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;

import com.taehun.pointcutapi.service.CustomAnnotation;

public class CustomPointCut implements Pointcut {
	
	@Override
	public ClassFilter getClassFilter() {
		
		return clazz -> clazz.getName().startsWith("com.taehun.pointcutapi.service");
	}
	
	@Override
	public MethodMatcher getMethodMatcher() {
		return new MethodMatcher() {
			
			@Override
			public boolean matches(Method method, Class<?> targetClass) {
				return method.isAnnotationPresent(CustomAnnotation.class)
						|| "differentMethod".equals(method.getName());
			}
			
			public boolean isRuntime() {
				System.out.println("called isRuntime 메서드");
				return true;
			}

		    public boolean matches (Method method, Class<?> targetClass, Object... args) {
		    	if (args.length > 0 
		    			&& args[0] instanceof String) {
		    		System.out.println("called matches" + 
		    			method.getName() + "]:arg is String");
		    		return true;
		    	} else {
		    		System.out.println("called matches" + 
			    			method.getName() + "]:arg is not String");
		    	}
		    	return false;
		    }
		};
		
	}
}

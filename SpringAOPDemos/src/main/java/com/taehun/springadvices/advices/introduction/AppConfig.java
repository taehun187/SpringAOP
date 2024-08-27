package com.taehun.springadvices.advices.introduction;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
	
	@Bean
	public LockMixinAdvisor lockMixinAdvisor() {
		return new LockMixinAdvisor();
	}
	
	@Bean
	public MyTargetClass myTargetClass (LockMixinAdvisor lockMixinAdvisor) {
		ProxyFactory factory = new ProxyFactory(new MyTargetClass());
		factory.addAdvisor(lockMixinAdvisor);
		factory.setProxyTargetClass(true);
		return (MyTargetClass) factory.getProxy();		
				
	}	
}

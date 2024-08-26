package com.taehun.conveniencepointcut.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.TransactionProxyFactoryBean;

import com.taehun.conveniencepointcut.service.MyServiceImpl;
import com.taehun.conveniencepointcut.service.MySpecialServiceImpl;
import com.taehun.conveniencepointcut.transaction.SimpleTransactionManager;


@Configuration
@EnableTransactionManagement
public class AppConfig {
	
	@Bean
	public PlatformTransactionManager transactionManager() {
		return new SimpleTransactionManager();
	}
	
	private TransactionProxyFactoryBean createProxy(
			PlatformTransactionManager transactionManager,
			Object target,
			Properties transactionAttributes) {
		TransactionProxyFactoryBean proxyFactoryBean = 
				new TransactionProxyFactoryBean();
		
		proxyFactoryBean.setTarget(target);
		proxyFactoryBean.setTransactionManager(transactionManager);
		proxyFactoryBean.setTransactionAttributes(transactionAttributes);
		proxyFactoryBean.setProxyTargetClass(true);
		
		return proxyFactoryBean;
	}
	
	@Bean
	public TransactionProxyFactoryBean myService(PlatformTransactionManager 
			transactinManager) {
		Properties properties = new Properties();
		properties.setProperty("*", "PROPAGATION_REQUIRED");
		return createProxy(transactinManager,
				new MyServiceImpl(),
				properties);
	}
	
	@Bean
	public TransactionProxyFactoryBean mySpecialService(PlatformTransactionManager 
			transactinManager) {
		Properties properties = new Properties();
		properties.setProperty("get", "PROPAGATION_REQUIRED,readOnly");
		properties.setProperty("find", "PROPAGATION_REQUIRED,readOnly");
		properties.setProperty("load", "PROPAGATION_REQUIRED,readOnly");
		properties.setProperty("store", "PROPAGATION_REQUIRED,readOnly");
		
		return createProxy(transactinManager,
				new MySpecialServiceImpl(),
				properties);
	}
			
}

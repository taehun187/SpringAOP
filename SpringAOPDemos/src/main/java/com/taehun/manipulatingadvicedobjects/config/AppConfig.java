package com.taehun.manipulatingadvicedobjects.config;

import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.taehun.manipulatingadvicedobjects.advice.AnotherInterceptor;
import com.taehun.manipulatingadvicedobjects.advice.DebugInterceptor;
import com.taehun.manipulatingadvicedobjects.pointcut.MySpecialPointcut;
import com.taehun.manipulatingadvicedobjects.service.MyService;
import com.taehun.manipulatingadvicedobjects.service.MyServiceImpl;

@Configuration
public class AppConfig {

    // MyServiceImpl 빈 정의
    @Bean
    public MyService myService() {
        return new MyServiceImpl();
    }

    // DebugInterceptor 빈 정의
    @Bean
    public DebugInterceptor debugInterceptor() {
        return new DebugInterceptor();
    }
    
    // AnotherInterceptor 빈 정의
    @Bean
    public AnotherInterceptor anotherInterceptor() {
        return new AnotherInterceptor();
    }

    // 프록시 팩토리 빈 정의
    @Bean
    public ProxyFactoryBean myServiceProxy(MyService myService, DebugInterceptor debugInterceptor) {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(myService);
        proxyFactoryBean.addAdvice(debugInterceptor);
        proxyFactoryBean.setProxyTargetClass(true);
        return proxyFactoryBean;
    }

    // MySpecialPointcut 빈 정의
    @Bean
    public MySpecialPointcut mySpecialPointcut() {
        return new MySpecialPointcut();
    }

    // DefaultPointcutAdvisor 빈 정의
    @Bean
    public DefaultPointcutAdvisor myAdvisor(MySpecialPointcut mySpecialPointcut, DebugInterceptor debugInterceptor) {
        return new DefaultPointcutAdvisor(mySpecialPointcut, debugInterceptor);
    }
    
 // 두 번째 어드바이저 빈 정의
    @Bean
    public DefaultPointcutAdvisor anotherAdvisor(MySpecialPointcut mySpecialPointcut, AnotherInterceptor anotherInterceptor) {
        return new DefaultPointcutAdvisor(mySpecialPointcut, anotherInterceptor);
    }
}
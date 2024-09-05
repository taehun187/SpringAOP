# Spring AOP 및 Proxy 설정 예제

이 문서는 Spring AOP를 활용하여 `MethodInterceptor`를 사용한 인터셉터 구성, 프록시 팩토리 설정 및 포인트컷을 정의하는 방법에 대한 예제 코드에 대해 설명합니다.

## 프로젝트 구조

- `AnotherInterceptor`: 메서드 실행 전후에 로깅을 추가하는 인터셉터.
- `DebugInterceptor`: 메서드 실행 전후에 디버깅 메시지를 추가하는 인터셉터.
- `AppConfig`: Spring에서 빈을 정의하고, 프록시 팩토리 및 어드바이저(Advisor)를 설정하는 구성 클래스.
- `MySpecialPointcut`: `performOperation` 메서드에만 AOP를 적용하는 포인트컷.
- `MyService`: 서비스 인터페이스로, `performOperation` 메서드를 정의.
- `MyServiceImpl`: `MyService` 인터페이스를 구현한 클래스.

## 코드 설명

### 1. `AnotherInterceptor`와 `DebugInterceptor`

두 인터셉터 클래스는 `MethodInterceptor` 인터페이스를 구현하여, 메서드 실행 전후에 각각의 로깅 기능을 수행합니다.

#### `AnotherInterceptor.java`

```java
public class AnotherInterceptor implements org.aopalliance.intercept.MethodInterceptor {
    @Override
    public Object invoke(org.aopalliance.intercept.MethodInvocation invocation) throws Throwable {
        System.out.println("AnotherInterceptor: Before method " + invocation.getMethod().getName());
        Object retVal = invocation.proceed();
        System.out.println("AnotherInterceptor: After method " + invocation.getMethod().getName());
        return retVal;
    }
}
```

#### `DebugInterceptor.java`

```java
public class DebugInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("DebugInterceptor: Before method " + invocation.getMethod().getName());
        Object retVal = invocation.proceed();
        System.out.println("DebugInterceptor: After method " + invocation.getMethod().getName());
        return retVal;
    }
}
```

### 2. `AppConfig` 구성 클래스

이 클래스에서는 서비스, 인터셉터, 포인트컷 및 어드바이저 빈을 정의하고, 프록시 팩토리를 설정하여 인터셉터가 적용되도록 구성합니다.

#### `AppConfig.java`

```java
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
```

### 3. `MySpecialPointcut`

포인트컷 클래스는 특정 메서드에만 AOP가 적용되도록 조건을 설정합니다. 여기서는 메서드 이름이 `performOperation`인 경우에만 AOP가 적용됩니다.

#### `MySpecialPointcut.java`

```java
public class MySpecialPointcut extends StaticMethodMatcherPointcut {
    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return "performOperation".equals(method.getName());
    }
}
```

### 4. `MyService` 인터페이스와 `MyServiceImpl` 구현 클래스

`MyService`는 비즈니스 로직을 정의하는 인터페이스이고, `MyServiceImpl`은 그 인터페이스를 구현한 클래스입니다.

#### `MyService.java`

```java
public interface MyService {
    void performOperation();
}
```

#### `MyServiceImpl.java`

```java
public class MyServiceImpl implements MyService {
    @Override
    public void performOperation() {
        System.out.println("MyServiceImpl: Operation performed");
    }
}
```

## 실행 흐름

1. `performOperation` 메서드를 호출하면, `MySpecialPointcut`에 의해 AOP가 적용됩니다.
2. `DebugInterceptor`와 `AnotherInterceptor`가 각각 메서드 실행 전후에 로깅을 출력합니다.
3. 실제 비즈니스 로직인 `MyServiceImpl`의 `performOperation`이 실행됩니다.

## 실행 결과

```bash
DebugInterceptor: Before method performOperation
AnotherInterceptor: Before method performOperation
MyServiceImpl: Operation performed
AnotherInterceptor: After method performOperation
DebugInterceptor: After method performOperation
```

## 결론

이 예제에서는 Spring AOP를 사용하여 `MethodInterceptor`를 통한 메서드 호출 전후의 로깅을 구현하였습니다. 포인트컷을 사용하여 특정 메서드에만 AOP를 적용하고, 프록시 팩토리를 통해 인터셉터를 설정하는 방법을 배웠습니다.

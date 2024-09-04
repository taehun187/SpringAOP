# Spring AOP와 ProxyFactoryBean 연습 프로젝트

## 소개

이 프로젝트는 **Spring AOP**와 **ProxyFactoryBean**을 사용하여 AOP를 적용하는 방법을 학습하는 예제입니다. AOP의 **Before Advice**를 사용하여 메소드가 호출되기 전에 특정 로직을 실행하고, 이를 프록시 객체를 통해 적용하는 방법을 연습합니다. 또한, **CGLIB 프록시**와 **JDK 동적 프록시**의 차이점, 프록시의 다양한 설정(`optimize`, `frozen`, `singleton`)을 학습할 수 있습니다.

## 주요 구성 요소

### 1. `LoggingBeforeAdvice` 클래스

- 이 클래스는 `MethodBeforeAdvice` 인터페이스를 구현하여 메소드가 호출되기 전에 로그 메시지를 출력합니다.
- 메소드 실행 전에 `"Before method: [메소드 이름]"`이 출력됩니다.

```java
@Override
public void before(Method method, Object[] args, Object target) throws Throwable {
    System.out.println("Before method: " + method.getName());
}
```

### 2. `SimpleService` 및 `SimpleServiceImpl` 클래스

- **SimpleService**는 인터페이스로, `doSomething()` 메소드를 선언합니다.
- **SimpleServiceImpl**은 이 인터페이스를 구현하며, 실제로 `"Doing something in SimpleService..."` 메시지를 출력하는 메소드를 제공합니다.

```java
@Override
public void doSomething() {
    System.out.println("Doing something in SimpleService...");
}
```

### 3. `AppConfig` 클래스

- 이 클래스는 스프링의 **@Configuration** 클래스로, 각종 빈과 프록시를 설정합니다.

#### 주요 빈 정의:

- **simpleService**: `SimpleServiceImpl`의 실제 구현체입니다.
- **loggingBeforeAdvice**: `LoggingBeforeAdvice` 어드바이스 빈입니다.
- **loggingAdvisor**: AOP의 어드바이저로, `NameMatchMethodPointcut`을 통해 `doSomething` 메소드에만 어드바이스를 적용합니다.
- **proxyFactoryBean**: 프록시를 생성하기 위한 설정입니다. 이 프록시는 **CGLIB**를 강제로 사용하도록 설정되어 있으며, `SimpleService` 인터페이스를 구현한 프록시를 반환합니다.

```java
@Bean
public ProxyFactoryBean proxyFactoryBean() throws ClassNotFoundException {
    ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
    proxyFactoryBean.setTarget(simpleService());  // SimpleServiceImpl 객체에 대한 프록시 생성
    proxyFactoryBean.setProxyTargetClass(true);   // CGLIB 프록시 강제
    proxyFactoryBean.setOptimize(true);           // 성능 최적화 설정
    proxyFactoryBean.setFrozen(false);            // 프록시 설정 변경 가능 여부
    proxyFactoryBean.setExposeProxy(true);        // AopContext를 통해 프록시 노출
    proxyFactoryBean.setProxyInterfaces(new Class<?>[]{SimpleService.class});  // 인터페이스 프록시 설정
    proxyFactoryBean.setInterceptorNames("loggingAdvisor");  // 어드바이저 설정
    proxyFactoryBean.setSingleton(true);           // 싱글톤 빈 설정
    return proxyFactoryBean;
}
```

### 4. `Main` 클래스

- `Main` 클래스는 스프링 컨텍스트를 초기화하고, 프록시 객체를 사용하여 AOP가 적용된 메소드 호출을 테스트합니다.

#### 주요 테스트:

1. **프록시를 통한 메소드 호출**:
   - 프록시 객체를 통해 `doSomething()` 메소드를 호출하여, AOP가 적용되는지 확인합니다.
   
2. **AopContext.currentProxy()**:
   - 현재 실행 중인 프록시 객체를 **AopContext.currentProxy()**를 통해 가져와 메소드를 호출하는 테스트입니다.

3. **프록시 클래스 확인**:
   - 프록시가 CGLIB 프록시인지, JDK 동적 프록시인지를 확인합니다.

4. **CGLIB 프록시 테스트**:
   - 프록시가 CGLIB를 사용하여 생성되었는지 여부를 테스트합니다.

5. **Singleton 프록시 테스트**:
   - 프록시가 싱글톤으로 설정되어 동일한 인스턴스를 반환하는지 테스트합니다.

6. **Frozen 설정 테스트**:
   - 프록시가 `frozen` 설정이 되어 있는지 테스트하며, 프록시 설정 변경이 허용되는지 확인합니다.

### Main 클래스 코드

```java
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // ProxyFactoryBean에서 생성된 프록시를 가져옵니다.
        SimpleService proxy = (SimpleService) context.getBean("proxySimpleService");

        // 1. 프록시를 통해 메서드 호출
        System.out.println("=== 1. 프록시를 통한 메서드 호출 ===");
        proxy.doSomething();

        // 2. AopContext.currentProxy()를 통해 프록시 접근
        System.out.println("=== 2. AopContext.currentProxy()를 통한 메서드 호출 ===");
        try {
            SimpleService currentProxy = (SimpleService) AopContext.currentProxy();
            currentProxy.doSomething();
        } catch (IllegalStateException e) {
            System.out.println("AopContext.currentProxy() 호출 실패: " + e.getMessage());
        }

        // 3. 인터페이스에 따른 프록시 확인 (JDK 프록시 또는 CGLIB 프록시)
        System.out.println("=== 3. 프록시 클래스 확인 ===");
        System.out.println("프록시 클래스: " + proxy.getClass().getName());

        // 4. CGLIB 프록시 테스트
        System.out.println("=== 4. CGLIB 프록시 테스트 ===");
        if (proxy instanceof SimpleServiceImpl) {
            System.out.println("CGLIB 프록시가 적용되었습니다: 프록시가 SimpleServiceImpl을 상속함");
        } else {
            System.out.println("JDK 동적 프록시가 적용되었습니다: 프록시가 인터페이스만 구현함");
        }

        // 5. 프록시가 singleton인지 테스트
        System.out.println("=== 5. Singleton 프록시 테스트 ===");
        SimpleService anotherProxyInstance = (SimpleService) context.getBean("proxySimpleService");
        System.out.println("동일한 프록시 인스턴스인가: " + (proxy == anotherProxyInstance));

        // 6. frozen 설정 테스트
        System.out.println("=== 6. Frozen 설정 테스트 ===");
        try {
            // 동적으로 어드바이스 추가 시도 (실패해야 함)
            ProxyFactoryBean proxyFactoryBean = (ProxyFactoryBean) context.getBean("&proxyFactoryBean");
            Advised advised = (Advised) proxyFactoryBean.getObject();

            advised.addAdvice(new MethodBeforeAdvice() {
                @Override
                public void before(Method method, Object[] args, Object target) throws Throwable {
                    System.out.println("동적으로 추가된 어드바이스");
                }
            });
            System.out.println("Frozen 설정이 무시되었습니다. 어드바이스가 추가되었습니다.");
        } catch (UnsupportedOperationException e) {
            System.out.println("Frozen 설정으로 인해 어드바이스 추가가 차단되었습니다: " + e.getMessage());
        }
    }
}
```

## 연습 목표

1. **AOP 어드바이스 적용**: `LoggingBeforeAdvice`가 `SimpleService`의 `doSomething()` 메소드 호출 전에 제대로 적용되는지 확인합니다.
2. **프록시 확인**: CGLIB 프록시가 올바르게 생성되고, 프록시 설정 옵션이 정상적으로 작동하는지 테스트합니다.
3. **Frozen 설정 테스트**: `ProxyFactoryBean`의 `frozen` 설정이 적용되어 프록시 구성이 변경되지 않도록 차단되는지 확인합니다.

## 테스트 방법

1. 프로젝트를 빌드하고 실행합니다.
2. `Main` 클래스를 실행하여 다양한 테스트 시나리오에서 프록시와 AOP가 정상적으로 작동하는지 확인합니다.

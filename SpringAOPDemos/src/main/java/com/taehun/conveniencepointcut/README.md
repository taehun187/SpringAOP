# Convenience Pointcut 연습 프로젝트

## 소개

이 프로젝트는 **스프링 AOP(Aspect Oriented Programming)**을 사용하여 포인트컷(Pointcut), 어드바이스(Advice), 트랜잭션 관리를 연습하기 위한 간단한 예제입니다. Spring AOP는 메소드 호출 전후에 코드 실행을 삽입하거나 특정 규칙에 맞는 메소드에만 적용할 수 있는 강력한 기능을 제공합니다. 이 예제에서는 두 개의 주요 설정 파일을 통해 서비스 메소드에 트랜잭션 및 포인트컷 어드바이스를 적용하는 방법을 설명합니다.

## 주요 구성 요소

### 1. `SimpleAdvice` 클래스

- `SimpleAdvice`는 AOP의 **어드바이스(Advice)**를 정의하는 역할을 합니다. 이 클래스는 메소드 호출 전후에 출력 메시지를 삽입합니다.
- `MethodInterceptor` 인터페이스를 구현하여, 메소드 실행 전과 후에 `System.out.println`을 통해 메소드 실행 로그를 출력합니다.

```java
@Override
public Object invoke(MethodInvocation invocation) throws Throwable {
    System.out.println("Before method: " + invocation.getMethod().getName());
    Object result = invocation.proceed();  // 실제 메소드 실행
    System.out.println("After method: " + invocation.getMethod().getName());
    return result;
}
```

### 2. 트랜잭션 관리 (`AppConfig`)

- **트랜잭션 관리**를 설정하는 클래스입니다. 
- `TransactionProxyFactoryBean`을 사용하여 서비스 객체에 트랜잭션을 적용합니다.
- 각 서비스별로 트랜잭션 정책을 정의하고 있습니다.
- 트랜잭션 매니저로는 `SimpleTransactionManager`를 사용합니다. 

```java
@Bean
public TransactionProxyFactoryBean myService(PlatformTransactionManager transactionManager) {
    Properties properties = new Properties();
    properties.setProperty("*", "PROPAGATION_REQUIRED");  // 모든 메소드에 대해 트랜잭션이 필요함을 명시
    return createProxy(transactionManager, new MyServiceImpl(), properties);
}
```

### 3. 포인트컷과 어드바이스 설정 (`AppConfig2`)

- 특정 메소드에만 AOP 어드바이스를 적용하는 **포인트컷(Pointcut)**을 설정하는 방법을 연습합니다.
- **정규 표현식**을 사용하여 특정 메소드에만 어드바이스가 적용되도록 설정합니다.
- 예를 들어, `get`, `set` 메소드나 `absquatulate`와 같은 특정 메소드에만 어드바이스가 적용됩니다.

```java
@Bean
public JdkRegexpMethodPointcut jdkRegexpMethodPointcut() {
    JdkRegexpMethodPointcut pointcut = new JdkRegexpMethodPointcut();
    pointcut.setPattern(".*get.*");  // 'get'이 포함된 메소드에만 적용
    return pointcut;
}
```

### 4. 서비스 클래스

- **MyService**와 **MySpecialService**는 트랜잭션 관리를 적용받는 서비스 클래스입니다.
- **SimpleService**는 AOP 포인트컷이 적용되는 클래스입니다. 이 클래스의 `set`, `get`, `absquatulate` 메소드에 AOP가 적용됩니다.

### 5. `SimpleTransactionManager` 클래스

- 스프링의 트랜잭션 관리 인터페이스인 `PlatformTransactionManager`를 구현하여 트랜잭션 시작, 커밋, 롤백을 출력하는 간단한 트랜잭션 매니저입니다.

```java
@Override
public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
    System.out.println("Transaction started");
    return (TransactionStatus) new SimpleTransactionManager();
}
```

### 6. Main 클래스

- **Main** 클래스는 트랜잭션 관리가 적용된 서비스인 `MyService`와 `MySpecialService`를 테스트합니다.
- **Program** 클래스는 포인트컷이 적용된 `SimpleService`의 메소드를 테스트합니다.

```java
SimpleService service = (SimpleService) context.getBean("proxyFactoryBean");
service.setName("John");  // 포인트컷 적용 (before/after 로그 출력)
service.getName();        // 포인트컷 적용 (before/after 로그 출력)
service.absquatulate();    // 포인트컷 적용 (before/after 로그 출력)
service.performTask();     // 포인트컷 미적용
```

## 연습 목표

1. **트랜잭션 관리**: 트랜잭션 시작, 커밋, 롤백과 같은 트랜잭션 관리가 서비스 메소드에 올바르게 적용되는지 확인합니다.
2. **포인트컷과 어드바이스**: 특정 규칙에 맞는 메소드에만 어드바이스가 적용되는 것을 확인합니다. 예를 들어, `get`, `set`과 같은 메소드에만 로그 출력이 추가되는지 확인합니다.
3. **AOP의 이해**: 스프링 AOP의 기본 개념인 어드바이스, 포인트컷, 애스펙트를 이해하고 적용해봅니다.

## 테스트 방법

1. 프로젝트를 빌드하고 실행합니다.
2. `Main` 클래스를 통해 트랜잭션 관리가 적용된 서비스를 테스트합니다.
3. `Program` 클래스를 통해 AOP 포인트컷과 어드바이스가 적용된 메소드를 테스트합니다.
4. 각 메소드 호출 전후로 로그가 출력되는 것을 확인하고, 트랜잭션이 올바르게 관리되는지 확인합니다.



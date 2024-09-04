# Control Flow Pointcut 및 AOP 연습 프로젝트

## 소개

이 프로젝트는 **스프링 AOP**의 **Control Flow Pointcut**을 사용하여 특정 메소드가 호출될 때에만 어드바이스가 적용되는 방법을 연습하는 예제입니다. Control Flow Pointcut은 특정 클래스와 메소드의 실행 흐름 내에서 호출된 메소드에 대해서만 AOP를 적용하는 방식입니다. 이 예제에서는 `TaskCaller` 클래스의 `callTask` 메소드를 통해 호출된 경우에만 `SimpleAdvice`가 적용되도록 설정합니다.

## 주요 구성 요소

### 1. `SimpleAdvice` 클래스

- `SimpleAdvice`는 AOP의 **어드바이스(Advice)**로, **MethodBeforeAdvice** 인터페이스를 구현하여 메소드가 호출되기 전에 특정 작업을 수행합니다.
- 이 어드바이스는 메소드가 호출되기 전마다 `System.out.println`으로 메소드 이름을 출력합니다.

```java
@Override
public void before(Method method, Object[] args, Object target) throws Throwable {
    System.out.println("Before method: " + method.getName());
}
```

### 2. `ControlFlowPointcut` 설정

- 이 프로젝트의 핵심은 **Control Flow Pointcut**을 사용하는 것입니다. Control Flow Pointcut은 특정 클래스와 메소드가 실행 흐름에 있을 때에만 포인트컷이 적용됩니다.
- `TaskCaller` 클래스의 `callTask` 메소드가 실행 흐름에 있을 때에만, `SimpleService`의 `performTask` 메소드에 어드바이스가 적용됩니다.

```java
@Bean
public ControlFlowPointcut controlFlowPointcut() {
    return new ControlFlowPointcut(TaskCaller.class, "callTask");  // TaskCaller의 callTask에서만 포인트컷 적용
}
```

### 3. AOP 어드바이저 설정

- **DefaultPointcutAdvisor**는 `ControlFlowPointcut`과 `SimpleAdvice`를 결합하여, 지정된 흐름 내에서만 어드바이스가 적용되도록 설정합니다.

```java
@Bean
public DefaultPointcutAdvisor defaultPointcutAdvisor() {
    return new DefaultPointcutAdvisor(controlFlowPointcut(), simpleAdvice());  // 포인트컷과 어드바이스 결합
}
```

### 4. `ProxyFactoryBean` 설정

- `ProxyFactoryBean`을 사용하여 **SimpleService**에 AOP 기능을 적용한 프록시 객체를 생성합니다.
- 이 프록시 객체는 `performTask` 메소드가 호출될 때, 특정 조건에 따라 AOP가 적용됩니다.

```java
@Bean
public ProxyFactoryBean proxyFactoryBean() {
    ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
    proxyFactoryBean.setTarget(simpleService());  // SimpleService를 프록시로 감쌈
    proxyFactoryBean.addAdvisor(defaultPointcutAdvisor());  // Advisor 추가
    return proxyFactoryBean;
}
```

### 5. `SimpleService` 클래스

- **SimpleService** 클래스는 `performTask`와 `internalTask`라는 메소드를 가지고 있습니다. AOP가 적용될 메소드는 `performTask`입니다.
- `internalTask` 메소드는 내부적으로 호출되지만, Control Flow Pointcut이 적용되지 않기 때문에 어드바이스는 적용되지 않습니다.

```java
public void performTask() {
    System.out.println("Performing a task");
    internalTask();  // 내부 호출
}

public void internalTask() {
    System.out.println("Performing internal task");
}
```

### 6. `TaskCaller` 클래스

- **TaskCaller**는 `SimpleService`의 `performTask` 메소드를 호출하는 클래스입니다. 이 클래스의 `callTask` 메소드가 Control Flow Pointcut의 기준이 됩니다.
- 즉, `callTask`가 실행 중일 때에만 `SimpleService`의 `performTask`에 어드바이스가 적용됩니다.

```java
public void callTask() {
    System.out.println("Calling task from TaskCaller");
    simpleService.performTask();  // AOP 어드바이스 적용
}
```

### 7. Main 클래스

- **Main** 클래스는 `TaskCaller`의 `callTask`를 호출한 후, AOP가 올바르게 적용되는지 확인합니다.
- 직접적으로 `SimpleService`의 `performTask` 메소드를 호출할 때는 어드바이스가 적용되지 않는지 확인합니다.

```java
public class Main {

    public static void main(String[] args) {
        
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);
        
        TaskCaller caller = context.getBean(TaskCaller.class);
        
        // TaskCaller를 통해 호출할 때는 AOP가 적용됨
        caller.callTask();
        
        // 직접 SimpleService를 호출할 때는 AOP가 적용되지 않음
        context.getBean("proxyFactoryBean", SimpleService.class).performTask();
        
        context.close();
    }

}
```

## 연습 목표

1. **Control Flow Pointcut의 이해**: 특정 클래스의 특정 메소드에서 호출된 메소드에만 AOP 어드바이스가 적용되는지 확인합니다.
2. **AOP 어드바이스 적용**: 메소드가 호출되기 전에 `SimpleAdvice`가 실행되는지 확인합니다.
3. **프록시 설정**: `ProxyFactoryBean`을 사용하여 AOP가 적용된 프록시 객체를 생성하고 이를 사용하는 방법을 학습합니다.

## 테스트 방법

1. 프로젝트를 빌드하고 실행합니다.
2. `Main` 클래스를 통해 `TaskCaller`의 `callTask` 메소드를 호출하여, AOP가 적용되는지 확인합니다.
3. `SimpleService`의 `performTask` 메소드를 직접 호출할 때는 AOP가 적용되지 않는 것을 확인합니다.

# Spring AOP 및 Pointcut API 예제

이 문서는 Spring AOP를 활용한 다양한 `MethodInterceptor` 및 `Pointcut`을 정의하고, 프록시를 사용하여 메서드 호출 전후에 부가 기능을 추가하는 방법을 설명합니다. 이를 통해 메서드 실행 시간 측정, 예외 처리, 로깅 등의 부가 기능을 구현하는 방법을 배웁니다.

## 프로젝트 구조

- **`ExeceptionHandlingAdvice`**: 메서드 실행 중 발생하는 예외를 처리하는 인터셉터.
- **`ExecutionTimeAdvice`**: 메서드 실행 시간을 측정하는 인터셉터.
- **`LoggingAdvice`**: 메서드 호출 전후에 로깅을 추가하는 인터셉터.
- **`AppConfig`**: Spring에서 빈을 정의하고 프록시 팩토리 및 어드바이저(Advisor)를 설정하는 구성 클래스.
- **`AppConfigForEnableAspectJAutoProxy`**: `@EnableAspectJAutoProxy`를 사용하여 AspectJ 스타일로 AOP를 설정하는 구성 클래스.
- **`CustomPointCut`**: 메서드와 클래스에 대한 커스텀 포인트컷 정의.
- **`AnotherService`**: 서비스 클래스, `myMethod`와 `differentMethod`라는 메서드를 포함.
- **`MyService`**: 서비스 클래스, AOP가 적용된 여러 메서드를 포함.
- **`Main`**: Spring 애플리케이션의 메인 클래스.

## 코드 설명

### 1. `ExeceptionHandlingAdvice`

메서드 실행 중 발생하는 예외를 캐치하고, 예외 메시지를 출력한 후 다시 예외를 던집니다.

```java
public class ExeceptionHandlingAdvice implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            return invocation.proceed();
        } catch (Exception e) {
            System.out.println("Exception caught in method: " +
                invocation.getMethod().getName() +
                ", exception: " + e.getMessage());
            throw e;
        }
    }
}
```

### 2. `ExecutionTimeAdvice`

메서드 실행 시간을 측정하고, 결과를 출력합니다.

```java
public class ExecutionTimeAdvice implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        long startTimeMills = System.currentTimeMillis();
        try {
            invocation.proceed();
        } finally {
            long timeTakeMills = System.currentTimeMillis() - startTimeMills;
            System.out.println("Execution time of " + invocation.getMethod().getName() +
                " :: " + timeTakeMills + "ms");
        }
        return null;
    }
}
```

### 3. `LoggingAdvice`

메서드 호출 전후에 로그 메시지를 출력하는 역할을 합니다.

```java
public class LoggingAdvice implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("Before method: " + invocation.getMethod().getName());
        Object result = invocation.proceed();
        System.out.println("After method: " + invocation.getMethod().getName());
        return result;
    }
}
```

### 4. `AppConfig`

Spring 프록시를 사용하여 서비스 클래스에 다양한 어드바이스를 적용하는 구성 클래스입니다. `ProxyFactoryBean`을 사용해 프록시를 생성하고, `Pointcut`과 `Advisor`를 설정합니다.

```java
@Configuration
public class AppConfig {
    @Bean
    public MyService myService() {
        return new MyService();
    }

    @Bean
    public AnotherService anotherService() {
        return new AnotherService();
    }

    @Bean
    public Pointcut customPointCut() {
        return new CustomPointCut();
    }

    @Bean
    public Pointcut aspectJPointcut() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* com.taehun.pointcutapi.service.AnotherService.myMethod(..))");
        return pointcut;
    }

    @Bean
    public DefaultPointcutAdvisor aspectJLoggingAdvisor(@Qualifier("aspectJPointcut") Pointcut pointcut) {
        return new DefaultPointcutAdvisor(pointcut, new LoggingAdvice());
    }

    @Bean
    public DefaultPointcutAdvisor loggingAdvisor(@Qualifier("customPointCut") Pointcut pointcut) {
        return new DefaultPointcutAdvisor(pointcut, new LoggingAdvice());
    }

    @Bean
    public DefaultPointcutAdvisor executionTimeAdvisor(@Qualifier("customPointCut") Pointcut pointcut) {
        return new DefaultPointcutAdvisor(pointcut, new ExecutionTimeAdvice());
    }

    @Bean
    public DefaultPointcutAdvisor execeptionHandlingAdvisor(@Qualifier("customPointCut") Pointcut pointcut) {
        return new DefaultPointcutAdvisor(pointcut, new ExeceptionHandlingAdvice());
    }

    @Bean
    public ProxyFactoryBean myServiceProxy(MyService myService, @Qualifier("loggingAdvisor") DefaultPointcutAdvisor loggingAdvisor, @Qualifier("executionTimeAdvisor") DefaultPointcutAdvisor executionTimeAdvisor, @Qualifier("execeptionHandlingAdvisor") DefaultPointcutAdvisor execeptionHandlingAdvisor) {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(myService);
        proxyFactoryBean.setInterceptorNames("loggingAdvisor", "executionTimeAdvisor", "execeptionHandlingAdvisor");
        return proxyFactoryBean;
    }

    @Bean
    public ProxyFactoryBean anotherServiceProxy(AnotherService anotherService, @Qualifier("loggingAdvisor") DefaultPointcutAdvisor loggingAdvisor, @Qualifier("executionTimeAdvisor") DefaultPointcutAdvisor executionTimeAdvisor, @Qualifier("execeptionHandlingAdvisor") DefaultPointcutAdvisor execeptionHandlingAdvisor) {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(anotherService);
        proxyFactoryBean.setInterceptorNames("loggingAdvisor", "executionTimeAdvisor", "execeptionHandlingAdvisor");
        return proxyFactoryBean;
    }
}
```

### 5. `AppConfigForEnableAspectJAutoProxy`

`@EnableAspectJAutoProxy`를 사용하여 AspectJ 스타일로 AOP 설정을 자동화하는 구성 클래스입니다.

```java
@Configuration
@EnableAspectJAutoProxy
public class AppConfigForEnableAspectJAutoProxy {
    @Bean
    public MyService myService() {
        return new MyService();
    }

    @Bean
    public AnotherService anotherService() {
        return new AnotherService();
    }

    @Bean
    public Pointcut customPointCut() {
        return new CustomPointCut();
    }

    @Bean
    public Pointcut aspectJPointcut() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* com.taehun.pointcutapi.service.Anotherservice.myMethod(..))");
        return pointcut;
    }

    @Bean
    public DefaultPointcutAdvisor aspectJLoggingAdvisor(@Qualifier("aspectJpointCut") Pointcut pointcut) {
        return new DefaultPointcutAdvisor(pointcut, new LoggingAdvice());
    }

    @Bean
    public DefaultPointcutAdvisor loggingAdvisor(@Qualifier("customPointCut") Pointcut pointcut) {
        return new DefaultPointcutAdvisor(pointcut, new LoggingAdvice());
    }

    @Bean
    public DefaultPointcutAdvisor executionTimeAdvisor(@Qualifier("customPointCut") Pointcut pointcut) {
        return new DefaultPointcutAdvisor(pointcut, new ExecutionTimeAdvice());
    }

    @Bean
    public DefaultPointcutAdvisor execeptionHandlingAdvisor(@Qualifier("customPointCut") Pointcut pointcut) {
        return new DefaultPointcutAdvisor(pointcut, new ExeceptionHandlingAdvice());
    }
}
```

### 6. `CustomPointCut`

커스텀 포인트컷을 정의하여 특정 조건에 맞는 메서드에 AOP를 적용합니다.

```java
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
                return method.isAnnotationPresent(CustomAnnotation.class) || "differentMethod".equals(method.getName());
            }

            public boolean isRuntime() {
                System.out.println("called isRuntime 메서드");
                return true;
            }

            public boolean matches(Method method, Class<?> targetClass, Object... args) {
                if (args.length > 0 && args[0] instanceof String) {
                    System.out.println("called matches" + method.getName() + "]:arg is String");
                    return true;
                } else {
                    System.out.println("called matches" + method.getName() + "]:arg is not String");
                }
                return false;
            }
        };
    }
}
```

### 7. `MyService`와 `AnotherService`

서비스 클래스들은 다양한 메서드를 포함하고 있으며, AOP를 통해 메서드 호출 시 다양한 부가 기능이 적용됩니다.

#### `MyService`

```java
public class MyService {
    @CustomAnnotation
    public void myMethod() {
        System.out.println("Executing myMethod");
    }

    @CustomAnnotation


    public void anotherMethod(String arg) {
        System.out.println("Executing anotherMethod with arg: " + arg);
    }

    public void methodWithException() throws Exception {
        throw new Exception("Exception in methodWithException");
    }
}
```

#### `AnotherService`

```java
public class AnotherService {
    public void myMethod() {
        System.out.println("Executing AnotherService myMethod");
    }

    public void differentMethod(int number) {
        System.out.println("Executing differentMethod with number: " + number);
    }
}
```

### 8. `Main`

메인 클래스에서 `AppConfig`와 `AppConfigForEnableAspectJAutoProxy`를 각각 사용하여 Spring AOP를 실행합니다. AOP를 통해 다양한 부가 기능(로깅, 실행 시간 측정, 예외 처리 등)을 적용합니다.

```java
public class Main {

    public static void execAppConfig() {
        ConfigurableApplicationContext context = 
                new AnnotationConfigApplicationContext(AppConfig.class);

        MyService myService = (MyService) context.getBean("myService");
        AnotherService anotherService = (AnotherService) context.getBean("anotherService");

        myService.myMethod();
        myService.anotherMethod("test");

        anotherService.myMethod();        
        anotherService.differentMethod(123);

        try {
            myService.methodWithException();
        } catch (Exception e) {
            System.out.println("Exception handled in main");
        }

        context.close();
    }

    public static void execAppConfigForEnableAspectJAutoProxy() {
        ConfigurableApplicationContext context = 
                new AnnotationConfigApplicationContext(AppConfigForEnableAspectJAutoProxy.class);

        MyService myService = (MyService) context.getBean("myService");
        AnotherService anotherService = (AnotherService) context.getBean("anotherService");

        myService.myMethod();
        myService.anotherMethod("test");

        anotherService.myMethod();        
        anotherService.differentMethod(123);

        try {
            myService.methodWithException();
        } catch (Exception e) {
            System.out.println("Exception handled in main");
        }

        context.close();
    }

    public static void main(String[] args) {
        execAppConfigForEnableAspectJAutoProxy();
    }
}
```

## 실행 흐름

1. Spring 애플리케이션 컨텍스트를 초기화합니다.
2. `MyService`와 `AnotherService`에서 메서드 호출 시 `LoggingAdvice`, `ExecutionTimeAdvice`, `ExeceptionHandlingAdvice`가 각각 적용됩니다.
3. 메서드 실행 전후에 로그가 출력되고, 메서드 실행 시간이 측정되며, 예외가 발생하면 처리됩니다.

## 실행 결과

```bash
Before method: myMethod
Executing myMethod
After method: myMethod
Execution time of myMethod :: [execution time]ms

Before method: anotherMethod
Executing anotherMethod with arg: test
After method: anotherMethod
Execution time of anotherMethod :: [execution time]ms

Before method: myMethod
Executing AnotherService myMethod
After method: myMethod
Execution time of myMethod :: [execution time]ms

Before method: differentMethod
Executing differentMethod with number: 123
After method: differentMethod
Execution time of differentMethod :: [execution time]ms

Exception caught in method: methodWithException, exception: Exception in methodWithException
Exception handled in main
```

## 결론

이 예제에서는 Spring AOP를 사용하여 메서드 호출 전후에 다양한 부가 기능(로깅, 실행 시간 측정, 예외 처리)을 적용하는 방법을 배웠습니다. `ProxyFactoryBean`을 통해 동적 프록시를 사용하고, `Pointcut`과 `Advisor`를 통해 특정 메서드에만 AOP 기능을 적용할 수 있었습니다. 또한, `@EnableAspectJAutoProxy`를 사용하여 AspectJ 스타일로 AOP를 설정하는 방법도 다루었습니다.


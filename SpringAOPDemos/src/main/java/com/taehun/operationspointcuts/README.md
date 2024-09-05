# Spring AOP Pointcut 및 Proxy 설정 예제

이 문서는 Spring AOP를 활용하여 `Pointcut`을 정의하고, 프록시 팩토리를 사용하여 메서드 호출 전후에 부가적인 기능을 추가하는 방법을 설명합니다. `methodA`와 `methodB`에 대한 포인트컷 설정, 포인트컷의 합집합 및 교집합, 그리고 프록시 팩토리로 부가 기능을 적용하는 과정을 다룹니다.

## 프로젝트 구조

- `AppConfig`: Spring에서 빈을 정의하고, `Pointcut` 및 어드바이저(Advisor)를 설정하는 구성 클래스입니다.
- `SimpleService`: 두 가지 메서드(`methodA`, `methodB`)를 제공하는 서비스 클래스입니다.
- `Main`: Spring 애플리케이션의 메인 클래스입니다. 애플리케이션 컨텍스트를 초기화하고 서비스를 호출합니다.

## 코드 설명

### 1. `AppConfig`

#### 포인트컷 정의
- **`pointcutForMethodA`**: 메서드 이름이 `methodA`인 경우에만 적용되는 포인트컷.
- **`pointcutForMethodB`**: 메서드 이름이 `methodB`인 경우에만 적용되는 포인트컷.
- **`unionPointcut`**: `methodA`와 `methodB` 두 메서드에 모두 적용되는 **합집합 포인트컷**.
- **`intersectionPointcut`**: 두 메서드에 모두 일치해야만 적용되는 **교집합 포인트컷** (예제에서는 사용되지 않음).

#### `ProxyFactoryBean`을 사용한 프록시 생성
- `SimpleService` 객체를 대상으로 프록시를 생성하고, 합집합 포인트컷(`unionPointcut`)에 대한 어드바이스를 적용합니다. 
- `MethodInterceptor`를 사용하여 `methodA`와 `methodB` 호출 전후에 추가 기능을 실행합니다.

```java
@Configuration
@Aspect
public class AppConfig {

	private static Pointcut pointcutForMethodA() {
		return new StaticMethodMatcherPointcut() {
			@Override
			public boolean matches(Method method, Class<?> targetClass) {
				return "methodA".equals(method.getName());
			}
		};
	}

	private static Pointcut pointcutForMethodB() {
		return new StaticMethodMatcherPointcut() {
			@Override
			public boolean matches(Method method, Class<?> targetClass) {
				return "methodB".equals(method.getName());
			}
		};
	}

	private static Pointcut unionPointcut() {
		return Pointcuts.union(pointcutForMethodA(), pointcutForMethodB());
	}

	@Bean
	public SimpleService simpleService() {
		ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
		proxyFactoryBean.setTarget(new SimpleService());

		org.aopalliance.aop.Advice advice = new MethodInterceptor() {
			@Override
			public Object invoke(MethodInvocation invocation) throws Throwable {
				System.out.println("Additional advice before unionPointcut method");
				return invocation.proceed();
			}
		};

		DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(unionPointcut(), advice);
		proxyFactoryBean.addAdvisor(advisor);

		return (SimpleService) proxyFactoryBean.getObject();
	}
}
```

### 2. `SimpleService`

`SimpleService` 클래스는 `methodA`와 `methodB`라는 두 가지 메서드를 제공하는 단순 서비스 클래스입니다. 각각의 메서드가 호출될 때 출력 메시지를 반환합니다.

```java
public class SimpleService {

	public void methodA() {
		System.out.println("Executing methodA");
	}

	public void methodB() {
		System.out.println("Executing methodB");
	}
}
```

### 3. `Main`

`Main` 클래스는 Spring 애플리케이션 컨텍스트를 초기화하고, `SimpleService`를 호출하는 역할을 합니다. 이를 통해 `methodA`와 `methodB`가 호출될 때 AOP를 통한 부가 기능이 적용됩니다.

```java
public class Main {
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		SimpleService service = context.getBean(SimpleService.class);
		
		service.methodA();
		service.methodB();
	}
}
```

## 실행 흐름

1. Spring 애플리케이션 컨텍스트가 초기화됩니다.
2. `SimpleService` 객체가 프록시로 감싸져 AOP 어드바이스가 적용됩니다.
3. `methodA()`와 `methodB()`를 호출하면 `unionPointcut`에 따라 `MethodInterceptor`의 부가 기능이 실행됩니다.

## 실행 결과

```bash
Additional advice before unionPointcut method
Executing methodA
Additional advice before unionPointcut method
Executing methodB
```

- `methodA`와 `methodB` 호출 전, "Additional advice before unionPointcut method" 메시지가 출력됩니다.
- 각 메서드가 실행되며 "Executing methodA", "Executing methodB" 메시지가 출력됩니다.

## 결론

이 예제에서는 Spring AOP에서 `Pointcut`을 설정하고, 이를 통해 특정 메서드에 부가 기능을 적용하는 방법을 배웠습니다. 프록시 팩토리를 사용하여 AOP를 적용하고, 합집합 포인트컷을 통해 두 개의 메서드에 동일한 어드바이스를 적용하는 과정을 설명했습니다.

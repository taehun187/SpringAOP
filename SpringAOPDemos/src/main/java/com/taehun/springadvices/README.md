# AOP with Spring Proxy Example

## 프로젝트 개요

이 프로젝트는 **Spring AOP(Aspect Oriented Programming)** 기능을 사용하여 프록시 객체를 생성하고 다양한 부가기능(Advice)을 적용하는 예제입니다. `Lockable` 인터페이스를 도입하고, 클래스의 상태를 보호하기 위한 `LockMixin`을 구현합니다. 또한 `Before`, `AfterReturning`, `ThrowsAdvice`, `MethodInterceptor`를 사용한 다양한 애드바이스를 통해 메소드 호출 전후, 예외 발생 시의 동작을 정의합니다.

## 주요 클래스 설명

### 1. `AppConfig`

- `@Configuration`을 사용하여 스프링 빈을 정의합니다.
- 프록시를 위한 팩토리 빈(`ProxyFactoryBean`)을 설정하고 여러 `Advisor`들을 추가합니다.

### 2. `LockMixin` 및 `LockMixinAdvisor`

- `Lockable` 인터페이스를 구현하는 `LockMixin`은 객체의 상태를 "잠금" 상태로 전환할 수 있으며, 잠긴 상태에서는 setter 메소드 호출을 제한합니다.
- `LockMixinAdvisor`는 `LockMixin`을 기반으로 `Lockable` 인터페이스를 도입하는 기능을 담당합니다.

### 3. `SimpleService`

- `SimpleService`는 간단한 서비스 클래스입니다. 이 클래스는 AOP를 적용한 프록시 객체로 감싸져서 메소드 호출 시 다양한 애드바이스가 실행됩니다.
- `performTask()`, `returnGreeting(String name)`, `throwException()` 메소드를 제공합니다.

### 4. 애드바이스

#### 4.1 `CountingBeforeAdvice`
- 메소드 실행 전 호출 횟수를 카운팅하여 출력합니다.

#### 4.2 `CountingAfterReturningAdvice`
- 메소드 실행 후 리턴된 값과 호출 횟수를 출력합니다.

#### 4.3 `DebugInterceptor`
- 메소드 실행 전과 후의 상태를 출력합니다.

#### 4.4 `SimpleThrowsAdvice`
- 예외 발생 시 실행되며, 메소드 이름과 발생한 예외 메시지를 출력합니다.

## 실행 방법

1. `AppConfig` 설정 파일을 통해 모든 애드바이스 및 프록시가 설정됩니다.
2. `Main` 클래스의 `main` 메소드를 실행하여 `SimpleService` 메소드를 호출합니다.
3. 각 메소드 호출 시 지정된 애드바이스가 실행되어 로그가 출력됩니다.

```bash

## 주요 기능

- **Proxy Factory**를 사용한 동적 프록시 생성
- 다양한 **Advice** 적용: Before, AfterReturning, Throws, Interceptor
- **Lockable** 인터페이스를 통한 객체 상태 보호

## 예시 출력


Before method: performTask, count=1
Performing a task
After method: returnGreeting, return value: hello, Me, count=1
Greeting: hello, Me
Before: invocation=[MethodInvocation: public void SimpleService.throwException()]
Exception thrown in method: throwException, exception: This is a test exception
Exception caught in main: This is a test exception


## 참고 사항

이 프로젝트는 AOP의 개념을 이해하는 데 중점을 두고 있으며, 실제 비즈니스 로직에서는 적절한 트랜잭션 관리와 에러 핸들링이 중요합니다.

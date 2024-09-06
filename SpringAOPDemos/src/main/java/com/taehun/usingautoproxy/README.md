다음은 요청하신 `README` 파일의 내용입니다:

---

# Spring AOP 및 Transaction Management 예제

## 프로젝트 개요

이 프로젝트는 **Spring AOP**와 **Transaction Management**를 사용하여 비즈니스 로직을 자동 프록시로 감싸고 트랜잭션 처리를 적용하는 예제입니다. Bean 이름을 기반으로 한 `BeanNameAutoProxyCreator`와 어드바이저 기반의 `DefaultAdvisorAutoProxyCreator`를 사용하여 프록시 객체를 생성하며, 트랜잭션과 AOP 기능을 결합하여 비즈니스 로직을 처리합니다.

## 주요 클래스 설명

1. **AppConfig**
    - **@EnableTransactionManagement**: 트랜잭션 관리 기능을 활성화합니다.
    - **BeanNameAutoProxyCreator**: 특정 Bean 이름 패턴에 맞는 Bean을 프록시로 자동 래핑합니다. 예시에서는 `jdkMyBean`과 `onlyJdk`라는 이름의 Bean에 대해 자동 프록시가 생성됩니다.
    - **DefaultAdvisorAutoProxyCreator**: 어드바이저 기반으로 자동 프록시를 생성합니다.
    - **TransactionInterceptor**: 트랜잭션 관리자를 설정하고, `@Transactional` 애노테이션이 붙은 메서드에 대해 트랜잭션 처리를 적용합니다.

2. **BusinessObject1**, **BusinessObject2**
    - **BusinessService** 인터페이스를 구현하며, 각각의 클래스에서 `@Transactional` 애노테이션이 적용되어 트랜잭션 처리를 제공합니다.

3. **MyBean**
    - AOP 프록시 적용 테스트를 위한 간단한 Bean입니다. `BeanNameAutoProxyCreator`에 의해 자동 프록시가 적용됩니다.

4. **프록시 정보 출력**
    - `AopProxyUtils`를 사용하여 프록시가 JDK 동적 프록시인지 CGLIB 프록시인지 확인하고, 최종 타겟 클래스 정보를 출력합니다.

## 실행 방법

1. `AppConfig` 설정 파일을 통해 트랜잭션 관리, AOP 프록시 생성, Bean 정의가 설정됩니다.
2. `Main` 클래스의 `main` 메소드를 실행하여 `BusinessObject1`, `BusinessObject2`, `MyBean`에 대해 프록시가 적용되는지 확인할 수 있습니다.
3. 각 객체에 대해 생성된 프록시 정보가 출력됩니다.

실행 명령어:
```
$ ./gradlew bootRun
```

## 주요 기능

- **BeanNameAutoProxyCreator**를 통한 Bean 이름 기반 프록시 생성
- **DefaultAdvisorAutoProxyCreator**를 통한 트랜잭션 및 어드바이스 적용
- **JDK 동적 프록시**와 **CGLIB 프록시**의 구분 및 동작 확인

## 예시 출력

```
Bean class: com.sun.proxy.$ProxyXX
Is JDK dynamic proxy: true
Is CGLIB proxy: false
Target class: BusinessObject1
----------------------
Bean class: com.sun.proxy.$ProxyXX
Is JDK dynamic proxy: true
Is CGLIB proxy: false
Target class: BusinessObject2
----------------------
Bean class: com.sun.proxy.$ProxyXX
Is JDK dynamic proxy: false
Is CGLIB proxy: true
Target class: MyBean
----------------------
```

## 참고 사항

- 트랜잭션 처리 시 `@Transactional` 애노테이션이 적용된 메서드는 AOP 프록시에 의해 트랜잭션 관리가 자동으로 처리됩니다.
- `BeanNameAutoProxyCreator`와 `DefaultAdvisorAutoProxyCreator`는 동시에 사용될 수 있으며, 프록시 생성 시 우선순위를 `@Order`로 지정할 수 있습니다.

## 의존성

- Spring Framework 5.x
- Java 8 이상
- Gradle 또는 Maven 빌드 도구

---

이 파일은 프로젝트의 주요 기능 및 설정에 대한 설명을 포함하며, 실행 방법과 예시 출력을 통해 프로젝트 동작 방식을 쉽게 파악할 수 있도록 도와줍니다.

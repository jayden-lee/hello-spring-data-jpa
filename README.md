# Hello Spring Data JPA
> 인프런 실전 스프링 데이터 JPA 강의를 학습하면서 정리하는 Repo

## 리포지토리 생성
<code>MemberRepository</code> 인터페이스는 스프링 데이터 JPA가 제공하는 <code>JpaRepository</code> 인터페이스를 상속 받아서 정의한다. 타입으로 첫 번째는 엔티티, 두 번째는 PK의 키 값이다. <code>JpaRepository</code> 인터페이스의 코드를 보면 기본적인 CRUD 기능이 정의되어 있다. 프로그래머는 공통의 기능을 만들기 위해서 반복적인 작업을 하지 않아도 된다. 

```java
public interface MemberRepository extends JpaRepository<Member, Long> {
}
```

인터페이스만 선언하면 스프링 데이터 JPA가 구현 클래스(프록시 클래스)를 생성하고 컴포넌트 스캔에 의해서 등록되기 때문에 별도로 @Repository 애노테이션을 붙일 필요가 없다. 또한, JPA 예외를 스프링 예외로 변환하는 과정도 자동으로 처리한다.  

## 인터페이스 분석
아래 코드는 <code>JpaRepository</code> 인터페이스 전체 소스코드이다. 패키지 명을 보면 <b>org.springframework.data.jpa.repository</b> 이라고 정의되어 있다. 근간이 되는 스프링 프레임워크 데이터 프로젝트가 있고 특정 모듈에 따라 JDBC, Mongo, JPA 프로젝트로 나눠지기 때문에 JPA 모듈에서는 jpa.repository 패키지 명을 사용한다. 이 인터페이스는 spring-data-jpa 라이브러리에 위치하고 있다.

```java
package org.springframework.data.jpa.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

@NoRepositoryBean
public interface JpaRepository<T, ID> extends PagingAndSortingRepository<T, ID>, QueryByExampleExecutor<T> {

	@Override
	List<T> findAll();

	@Override
	List<T> findAll(Sort sort);

	@Override
	List<T> findAllById(Iterable<ID> ids);

	@Override
	<S extends T> List<S> saveAll(Iterable<S> entities);

	void flush();

	<S extends T> S saveAndFlush(S entity);

	void deleteInBatch(Iterable<T> entities);

	void deleteAllInBatch();

	T getOne(ID id);

	@Override
	<S extends T> List<S> findAll(Example<S> example);

	@Override
	<S extends T> List<S> findAll(Example<S> example, Sort sort);
}
```

아래 코드는 <code>JpaRepository</code> 인터페이스가 상속 받고 있는 <code>PagingAndSortingRepository</code> 인터페이스의 소스코드이다. 패키지 명을 살펴보면 jpa 라는 이름이 없으며, <b>org.springframework.data.repository</b>으로 정의되어 있다. 상위 모듈에서 필수 공통의 기능을 선언한 인터페이스이다. 이 인터페이스는 spring-data-commons 라이브러리에 위치하고 있다.

```java
package org.springframework.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@NoRepositoryBean
public interface PagingAndSortingRepository<T, ID> extends CrudRepository<T, ID> {

	Iterable<T> findAll(Sort sort);

	Page<T> findAll(Pageable pageable);
}
```

CRUD 기능은 <code>CrudRepository</code> 인터페이스에 정의 되어 있다.

## 쿼리 메서드

### 메서드 이름으로 쿼리 생성
인터페이스에 <code>findByUsernameAndAgeGreaterThan</code> 이름으로 메서드를 정의한다. 메서드 이름은 SQL에서 사용 했던 익숙한 키워드(And, Between, LessThan 등)을 이용해서 조건문을 표현하도록 작성한다. 쿼리 메서드가 가능한 이유는 스프링 데이터 JPA가 인터페이스에 정의된 메서드 이름을 분석해서 JPQL을 생성하기 때문이다. 

```
public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

}
```

실제 실행되는 SQL 문은 다음과 같다. 메서드 이름에 사용된 <code>Username</code>, <code>And</code>, <code>AgeGreaterThan</code> 키워드가 SQL 문의 필터 조건인 <code>member0_.username=? and member0_.age>?</code>으로 표현된다.

```sql
select
    member0_.member_id as member_i1_0_,
    member0_.age as age2_0_,
    member0_.team_id as team_id4_0_,
    member0_.username as username3_0_ 
from
    member member0_ 
where
    member0_.username=? 
    and member0_.age>?
```

> 메서드 이름을 쿼리를 생성하는 기능은 간단한 쿼리를 작성할 때 편하지만 복잡한 쿼리를 정의하기에는 불편한 점이 있다.그리고 엔티티의 필드명이 변경되면 인터페이스에 정의한 메서드 이름도 같이 변경해줘야 한다. 

## References
- [Spring Data JPA Docs](https://docs.spring.io/spring-data/jpa/docs/2.2.4.RELEASE/reference/html/#reference)
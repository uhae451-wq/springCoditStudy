package com.example.springcoditstudy.d0930;

import com.example.springcoditstudy.d0930.Course.CourseLevel;
import com.example.springcoditstudy.d1001.entity.Department;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

// 스프링 부트를 빠르게 실행하기 위한 , 스프링부트에 작성한 메서드를 빠르게 실행하기 위한 테스트 코드
@SpringBootTest
@Transactional
@Rollback(value = false)
public class PracticeTest {


  // 스프링 부트 안에 있는 영속성 컨텍스트 꺼내기
  @PersistenceContext
  EntityManager em;

  // 영속성 컨텍스트를 통해서 값을 저장
  @Test
  void 실습1_저장(){
    Department department = new Department("안과");
    System.out.println(department.getId()); // null
    em.persist(department);
    System.out.println(department.getId()); // 1
  }

  @Test
  void 실습2_조회(){
    // 영속성 컨텍스트에게 찾아줘
    Department found = em.find(Department.class,2);
    System.out.println(found.getName());
  }

  @Test
  void 실습3_수정(){
    // 영속성 컨텍스트를 통해서 수정. / 영속성 컨텍스트에는 update 메서드가 없다.
    Department found = em.find(Department.class,2);
    found.setName("수정된 안과");
    // 영속성 컨텍스트는 영속성 컨텍스트에 의해서 찾아온 값이 변경되면 알아서 반영이 된다.
    // 수정했으면 알아서 반영이 된다.
  }

  @Test
  void 실습4_삭제(){
    Department found = em.find(Department.class,2);
    em.remove(found);
  }

  @Test
  void total(){

    // 비영속 상태의 엔티티를 생성
    Department a = new Department("성형외과");
    Department b = new Department("산부인과");

    // 영속 전환 -> 엔티티드를 DB에 반영하고 그 값을 1차 캐시에 저장한다.
    em.persist(a);
    em.persist(b);

    System.out.println(a.getId());
    System.out.println(b.getId());

    Department foundA = em.find(Department.class,a.getId());
    Department foundB = em.find(Department.class,b.getId());

    // 저장하고 영속성 컨텍스트의 저장소를 깔끔하게 비우기
    em.flush(); // 쌓여있는 SQL문 쿼리를 모두 실행하라
    em.clear(); // 1차 캣시, 스탭샷 모두 제거.

  }

  @Test
  void 강의등록(){
    Category c = new Category("백엔드");
    em.persist(c);
    Instructor i = new Instructor("김강사","kim@google.com");
    em.persist(i);
    Course course = new Course("스프링 입문",55000, CourseLevel.BEGINNER);
    em.persist(course);
  }







}

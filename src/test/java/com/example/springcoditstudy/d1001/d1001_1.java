package com.example.springcoditstudy.d1001;

import com.example.springcoditstudy.d1001.entity.Department;
import com.example.springcoditstudy.d1001.entity.Doctor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback(value = false)
public class d1001_1 {

  // 스프링 부트 안에 있는 영속성 컨텍스트 꺼내기
  @PersistenceContext
  EntityManager em;

  @Test
  void 의사등록(){
    Department department1 = new Department("내과");
    em.persist(department1);
    Department department2 = new Department("외과");
    em.persist(department2);
    Department department3 = new Department("피부과");
    em.persist(department3);
    Doctor doctor = new Doctor("김현수","123",department3);
    em.persist(doctor);
  }

  @Test
  void 의사조회(){
    Department department1 = new Department("내과");
    em.persist(department1);
    Doctor doctor = new Doctor("김현수","123",department1);
    em.persist(doctor);

    em.flush();
    em.clear();

    Doctor foundDoctor = em.find(Doctor.class,1);
    System.out.println(foundDoctor);

    Department foundDepartment = em.find(Department.class,1);
    System.out.println(foundDepartment.getDoctors().get(0).getLicenseNo());
  }

  @Test
  void 주인_아닌쪽에_값을_넣어보자(){
    // 1:N 관계에서 1은 부서, N은 의사일때 N이 주인이 된다.
    Department d1 = new Department("내과");
    em.persist(d1);
    /*Doctor doctor = new Doctor("김현수","김라이선스");*//*
    doctor.assignTo(d1);
    em.persist(doctor);*/

    // 부서의 의사 목록인 ArrayList
   /* d1.getDoctors().add(doctor);*/
    // 무소속인 의사를 내과에 추가?
    em.flush();
    em.clear();
    Doctor foundDoctor = em.find(Doctor.class,1);
    System.out.println(foundDoctor);

  }

/*  @Test
  void 처방_생성(){
    MedicalRecord medicalRecord = new MedicalRecord("졸림","혈당 쇼크로 인한 피로감");
    em.persist(medicalRecord);

    Medicine medicine = new Medicine("박카스","병");
    Medicine medicine1 = new Medicine("위고비","주사기");
    em.persist(medicine);
    em.persist(medicine1);

    Prescription prescription = new Prescription(medicalRecord,medicine,"1병");
    Prescription prescription1 = new Prescription(medicalRecord,medicine1,"1");

    em.persist(prescription);
    em.persist(prescription1);
  }*/
}

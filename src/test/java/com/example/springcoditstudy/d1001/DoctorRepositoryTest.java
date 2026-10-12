package com.example.springcoditstudy.d1001;

import com.example.springcoditstudy.d1001.entity.Allergy;
import com.example.springcoditstudy.d1001.entity.Department;
import com.example.springcoditstudy.d1001.entity.Doctor;
import com.example.springcoditstudy.d1001.entity.MedicalRecord;
import com.example.springcoditstudy.d1001.entity.Medicine;
import com.example.springcoditstudy.d1001.entity.Patient;
import com.example.springcoditstudy.d1001.entity.Prescription;
import com.example.springcoditstudy.d1001.entity.Reservation;
import com.example.springcoditstudy.d1001.repository.DepartmentRepository;
import com.example.springcoditstudy.d1001.repository.DoctorRepository;
import com.example.springcoditstudy.d1001.repository.ReservationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Rollback(value = false)
@Transactional
class DoctorRepositoryTest {

  @PersistenceContext
  EntityManager em;

  @Autowired
  DoctorRepository doctorRepository;
  @Autowired
  DepartmentRepository departmentRepository;
  @Autowired
  ReservationRepository reservationRepository;

  @Test
  void 의사_저장(){
    Department department = new Department("내과");
    departmentRepository.save(department);

    Doctor doctor = new Doctor("김의사","123123",department);
    doctorRepository.save(doctor);

  }

  @Test
  void 의사_찾기(){
    // 잇을수도 잇고 없을수도 잇다.
    Doctor doctor1 = doctorRepository.findById(1l).orElseThrow(()-> new NullPointerException("의사 없음"));

    /*Doctor doctor2 = doctorRepository.findById(100l)
        .orElseThrow(()-> new NullPointerException("의사 없음"));*/

    Doctor foundByName = doctorRepository.findByName("김의사")
        .orElseThrow(()->new NullPointerException("의사 없음"));

    Doctor foundByNameAndLicenseNo = doctorRepository.findByNameAndLicenseNo("김의사","123123")
        .orElseThrow(()->new NullPointerException("의사 없음"));

    // 내과인 의사를 찾는다.
    List<Doctor> doctorList = doctorRepository.findByDepartment_Name("내과");


  }

  @Test
  void 의사_페이지_찾기(){
    Pageable pageable = PageRequest.of(1,10, Sort.by("name"));
    Page<Doctor> doctors = doctorRepository.findByDepartment_Name("내과88992",pageable);
  }


  @Test
  void 더미데이터_생성() {
    // unique 컬럼(진료과명, 면허번호, 전화번호)이 겹치지 않도록 매번 다른 접미사를 붙인다.
    String s = String.valueOf(System.currentTimeMillis() % 100000);

    // 진료과 3개
    Department 내과 = new Department("내과" + s);
    Department 정형외과 = new Department("정형외과" + s);
    Department 피부과 = new Department("피부과" + s);
    em.persist(내과);
    em.persist(정형외과);
    em.persist(피부과);

    // 의사 4명
    Doctor 김동현 = new Doctor("김동현", "LIC-" + s + "-1", 내과);
    Doctor 이서연 = new Doctor("이서연", "LIC-" + s + "-2", 내과);
    Doctor 박준호 = new Doctor("박준호", "LIC-" + s + "-3", 정형외과);
    Doctor 최민지 = new Doctor("최민지", "LIC-" + s + "-4", 피부과);
    em.persist(김동현);
    em.persist(이서연);
    em.persist(박준호);
    em.persist(최민지);

    // 환자 4명
    Patient 홍길동 = new Patient("홍길동", "010-" + s + "-1", LocalDate.of(1985, 3, 12));
    Patient 김영희 = new Patient("김영희", "010-" + s + "-2", LocalDate.of(1992, 7, 25));
    Patient 이철수 = new Patient("이철수", "010-" + s + "-3", LocalDate.of(1978, 11, 2));
    Patient 박민수 = new Patient("박민수", "010-" + s + "-4", LocalDate.of(2015, 5, 18));
    em.persist(홍길동);
    em.persist(김영희);
    em.persist(이철수);
    em.persist(박민수);

    // 알레르기 (홍길동 2건, 김영희 1건, 나머지는 없음)
    em.persist(new Allergy(홍길동, "페니실린"));
    em.persist(new Allergy(홍길동, "땅콩"));
    em.persist(new Allergy(김영희, "아스피린"));

    // 약 4개
    Medicine 타이레놀 = new Medicine("타이레놀", "정");
    Medicine 무코솔반 = new Medicine("무코솔반", "포");
    Medicine 이부프로펜 = new Medicine("이부프로펜", "정");
    Medicine 항히스타민 = new Medicine("항히스타민", "정");
    em.persist(타이레놀);
    em.persist(무코솔반);
    em.persist(이부프로펜);
    em.persist(항히스타민);

    // 예약 5건 — 앞의 3건은 진료를 받았고, 뒤의 2건은 기록이 없다
    LocalDateTime now = LocalDateTime.now();
    Reservation 예약1 = new Reservation(홍길동, 김동현, now.minusDays(7));
    Reservation 예약2 = new Reservation(김영희, 박준호, now.minusDays(5));
    Reservation 예약3 = new Reservation(이철수, 최민지, now.minusDays(2));
    Reservation 예약4 = new Reservation(홍길동, 이서연, now.plusDays(1));
    Reservation 예약5 = new Reservation(박민수, 김동현, now.plusDays(3));
    em.persist(예약1);
    em.persist(예약2);
    em.persist(예약3);
    em.persist(예약4);
    em.persist(예약5);

    // 진료기록 3건 + 처방
    MedicalRecord 기록1 = new MedicalRecord(예약1, "두통과 미열이 3일째 지속", "감기 몸살로 판단, 휴식 권고");
    MedicalRecord 기록2 = new MedicalRecord(예약2, "무릎 통증", "연골 마모 초기, 물리치료 병행");
    MedicalRecord 기록3 = new MedicalRecord(예약3, "피부 발진과 가려움", "접촉성 피부염");
    em.persist(기록1);
    em.persist(기록2);
    em.persist(기록3);

    em.persist(new Prescription(기록1, 타이레놀, "1일 3회 1정"));
    em.persist(new Prescription(기록1, 무코솔반, "1일 2회 1포"));
    em.persist(new Prescription(기록2, 이부프로펜, "1일 2회 1정"));
    em.persist(new Prescription(기록3, 항히스타민, "취침 전 1정"));
  }

  @Test
  void test(){
    Department department = departmentRepository.findById(1l).orElseThrow();

  }

  @Test
  void test1(){
    List<Reservation> reservation = reservationRepository.findAll();
    // 예약시간 확인용
    // lazy일때 예약정보만 가져오게 되는데
    // eager로 바꾸면 해당 컬럼 연관 테이블 정보까지 한번에 가져온다.
    // lazy는 연관 테이블 정보가 필요로할때 다시 호출한다.
    // doctor와 patient를 수만큼 호출
    for(Reservation r : reservation){
      System.out.println(r.getDoctor() + " : " + r.getPatient());
    }
  }

  @Test
  void test2(){
    List<Reservation> reservation = reservationRepository.findAllWithPatientAndDoctor();
    // lazy지만 join fetch를 통해서 eager처럼 한번에 가져오게 한다.
    for(Reservation r : reservation){
      System.out.println(r.getDoctor() + " : " + r.getPatient());
    }
  }

  @Test
  void test3(){
    List<Reservation> reservation = reservationRepository.findAll();
    // lazy지만 join fetch를 통해서 eager처럼 한번에 가져오게 한다.
    for(Reservation r : reservation){
      System.out.println(r.getDoctor() + " : " + r.getPatient());
    }
  }
}
package com.example.springcoditstudy.d1001;

import com.example.springcoditstudy.d1001.entity.Patient;
import com.example.springcoditstudy.d1001.service.PatientService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Rollback(value = false)
@Transactional
class PatientServiceTest {

  @Autowired
  PatientService patientService;

  @Test
  void test1(){
    Long patient1 = patientService.createPatient("김현수","010-7156-7741", LocalDate.now());
  }

  @Test
  void test2(){
    List<Patient> list = patientService.findByName("홍길동");
    list.forEach((i)-> System.out.println(i.getName()));
  }

  @Test
  void test3(){
    patientService.changeInfo(1l,"111122222");
  }


}
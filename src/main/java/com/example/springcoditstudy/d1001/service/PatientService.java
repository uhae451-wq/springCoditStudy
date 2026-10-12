package com.example.springcoditstudy.d1001.service;

import com.example.springcoditstudy.d1001.entity.Patient;
import com.example.springcoditstudy.d1001.repository.PatientRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 해당 트랜잭션 구간에서 읽기만 가능한지 쓰기,수정,삭제도 가능한지
public class PatientService {

  private final PatientRepository patientRepository;


  public Long createPatient(String name,String phone, LocalDate birthDay){

    if(patientRepository.existsByPhone(phone)){
      throw new IllegalArgumentException("이미 등록된 전화번호입니다.");
    }
    Patient patient = new Patient(name,phone,birthDay);
    patientRepository.save(patient);
    return patient.getId();
  }

  public List<Patient> findByName(String keyword){
    return patientRepository.findByNameContaining(keyword);
  }

  public void changeInfo(Long id, String phone){
    Patient patient = patientRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
    patient.setPhone(phone);
  }

}

package com.example.springcoditstudy.d1001.repository;

import com.example.springcoditstudy.d1001.entity.Reservation;
import com.example.springcoditstudy.d1001.entity.Reservation.ReservationStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {

  List<Reservation> findByPatientOrderByReservedAtDesc(String string);

  List<Reservation> findByStatusAndReservedAtBetween(ReservationStatus status, LocalDateTime reservedAtAfter, LocalDateTime reservedAtBefore);

  List<Reservation> findTop5ByOrderByReservedAtDesc();

  // N+1 문제가 발생하는 쿼리를 직접 작성한다.
  @Query("select r from Reservation r join fetch r.patient join fetch r.doctor")
  List<Reservation> findAllWithPatientAndDoctor();
  // 기본 메서드를 덮어쓴다
  @EntityGraph(attributePaths = {"patient", "doctor"})
  List<Reservation> findAll();
  // 쿼리 메서드에도
  @EntityGraph(attributePaths = {"patient", "doctor"})
  List<Reservation> findByStatus(ReservationStatus status);
  // 페이징도 OK
  @EntityGraph(attributePaths = {"doctor", "doctor.department"})
  Page<Reservation> findByPatientId(Long patientId, Pageable pageable);


}

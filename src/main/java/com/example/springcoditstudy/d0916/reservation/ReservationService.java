package com.example.springcoditstudy.d0916.reservation;

import com.example.springcoditstudy.d0916.trainer.TrainerService2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository_1 reservationRepository1;
    private final TrainerService2 trainerService;

    public Reservation createReservation(Long trainerId, String memberName, LocalDateTime startTime){
        trainerService.getTrainer(trainerId); // 에러발생가능
        if(reservationRepository1.existsConflict(trainerId,startTime,null)){
            throw new ReservationConflictException(trainerId,startTime);
        }
        return reservationRepository1.save(trainerId,memberName,startTime);
    }

    public Reservation getReservation(Long id){
        Reservation reservation = reservationRepository1.findById(id);
        if(reservation == null){
            throw new ReservationNotFoundException(id);
        }
        return reservation;
    }

    public List<Reservation> getReservations(Long trainerId){
        List<Reservation> reservations = reservationRepository1.findAll();
        if(trainerId == null) return reservations;
        return reservations.stream().filter(r -> r.getTrainerId()==trainerId).toList();
    }


    public Reservation updateReservation(Long id,UpdateReservationRequest updateReservationRequest){
        Reservation reservation = getReservation(id); // 에러발생가능
        if(reservationRepository1.existsConflict(reservation.getTrainerId(),updateReservationRequest.getStartTime(),null)){
            throw new ReservationConflictException(id,updateReservationRequest.getStartTime());
        }
        Reservation reservation1 = reservationRepository1.update(id,updateReservationRequest.getStartTime());
        return reservation1;
    }

    public void deleteReservation(Long id){
        getReservation(id);
        reservationRepository1.delete(id);
    }
}

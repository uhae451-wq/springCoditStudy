package com.example.springcoditstudy.d0916.reservation;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ReservationRepository {
    private final Map<Long, Reservation> store = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Reservation save(Long trainerId, String memberName, LocalDateTime localDateTime){
        long id = idGenerator.getAndIncrement();
        Reservation reservation = new Reservation(id,trainerId,memberName,localDateTime);
        store.put(id,reservation);
        return reservation;
    }

    public Reservation findById(Long id){
        return store.get(id);
    }

    public List<Reservation> findAll(){
        return new ArrayList<>(store.values());
    }

    public boolean existsConflict(Long trainerId, LocalDateTime startTime, Long excludeId){
        return store.values().stream().
                anyMatch(e -> e.getTrainerId() == trainerId && e.getStartTime() == startTime);
    }
    /*
        public boolean existsConflict(long trainerId, LocalDateTime startTime, Long excludeId) {
        return store.values().stream()
                .anyMatch(r -> r.getTrainerId() == trainerId
                        && r.getStartTime().equals(startTime)
                        && (excludeId == null || r.getId() != excludeId));
    }
    * */
    public Reservation update(long id, LocalDateTime startTime){
        Reservation reservation = store.get(id);
        Reservation newReservation = new Reservation(id,reservation.getTrainerId(),reservation.getMemberName(),startTime);
        store.put(id,newReservation);
        return newReservation;
    }

    public void delete(long id){
        store.remove(id);
    }
}

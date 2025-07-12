package com.bookingsystem.domain.port;

import com.bookingsystem.domain.model.Reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    
    Reservation save(Reservation reservation);
    
    Optional<Reservation> findById(Long id);
    
    List<Reservation> findAll();
    
    List<Reservation> findByUserId(Long userId);
    
    List<Reservation> findByUserIdAndDate(Long userId, LocalDate date);
    
    List<Reservation> findOverlappingReservations(LocalDateTime startTime, LocalDateTime endTime);
    
    void deleteById(Long id);
    
    boolean existsById(Long id);
}
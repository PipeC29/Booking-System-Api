package com.bookingsystem.application.usecase;

import com.bookingsystem.domain.model.Reservation;
import com.bookingsystem.domain.port.ReservationRepository;

import java.time.LocalDate;
import java.util.List;

public class CreateReservationUseCase {
    
    private final ReservationRepository reservationRepository;
    
    public CreateReservationUseCase(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }
    
    public Reservation execute(Reservation reservation) {
        validateBusinessRules(reservation);
        return reservationRepository.save(reservation);
    }
    
    private void validateBusinessRules(Reservation reservation) {
        if (!reservation.isValidBusinessHours()) {
            throw new IllegalArgumentException("Reservation must be within business hours");
        }
        
        if (!reservation.isValidTimeRange()) {
            throw new IllegalArgumentException("Invalid time range for reservation");
        }
        
        validateNoOverlapping(reservation);
        validateDailyLimit(reservation);
    }
    
    private void validateNoOverlapping(Reservation reservation) {
        List<Reservation> overlapping = reservationRepository.findOverlappingReservations(
            reservation.getStartTime(), reservation.getEndTime()
        );
        
        if (!overlapping.isEmpty()) {
            throw new IllegalArgumentException("Reservation overlaps with existing reservation");
        }
    }
    
    private void validateDailyLimit(Reservation reservation) {
        LocalDate reservationDate = reservation.getStartTime().toLocalDate();
        List<Reservation> dailyReservations = reservationRepository.findByUserIdAndDate(
            reservation.getUserId(), reservationDate
        );
        
        if (dailyReservations.size() >= Reservation.getMaxReservationsPerDay()) {
            throw new IllegalArgumentException("Daily reservation limit exceeded");
        }
    }
}
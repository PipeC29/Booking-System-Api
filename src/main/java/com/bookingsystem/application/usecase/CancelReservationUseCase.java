package com.bookingsystem.application.usecase;

import com.bookingsystem.domain.model.Reservation;
import com.bookingsystem.domain.port.ReservationRepository;

public class CancelReservationUseCase {
    
    private final ReservationRepository reservationRepository;
    
    public CancelReservationUseCase(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }
    
    public void execute(Long reservationId, Long userId) {
        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        
        if (!reservation.getUserId().equals(userId)) {
            throw new IllegalArgumentException("User can only cancel their own reservations");
        }
        
        reservation.cancel();
        reservationRepository.save(reservation);
    }
}
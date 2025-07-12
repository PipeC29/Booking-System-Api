package com.bookingsystem.application.usecase;

import com.bookingsystem.domain.model.Reservation;
import com.bookingsystem.domain.port.ReservationRepository;

import java.util.List;

public class GetReservationsUseCase {
    
    private final ReservationRepository reservationRepository;
    
    public GetReservationsUseCase(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }
    
    public List<Reservation> execute() {
        return reservationRepository.findAll();
    }
    
    public List<Reservation> executeByUserId(Long userId) {
        return reservationRepository.findByUserId(userId);
    }
}
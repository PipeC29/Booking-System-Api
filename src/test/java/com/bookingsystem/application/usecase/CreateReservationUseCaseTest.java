package com.bookingsystem.application.usecase;

import com.bookingsystem.domain.model.Reservation;
import com.bookingsystem.domain.model.ReservationStatus;
import com.bookingsystem.domain.port.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CreateReservationUseCaseTest {
    
    @Mock
    private ReservationRepository reservationRepository;
    
    private CreateReservationUseCase createReservationUseCase;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createReservationUseCase = new CreateReservationUseCase(reservationRepository);
    }
    
    @Test
    void shouldCreateReservationSuccessfully() {
        LocalDateTime startTime = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
        LocalDateTime endTime = startTime.plusHours(1);
        
        Reservation reservation = new Reservation(
            "Juan Perez",
            "juan@email.com",
            startTime,
            endTime,
            "Consulta médica",
            1L
        );
        
        when(reservationRepository.findOverlappingReservations(any(), any()))
            .thenReturn(Collections.emptyList());
        when(reservationRepository.findByUserIdAndDate(eq(1L), any(LocalDate.class)))
            .thenReturn(Collections.emptyList());
        when(reservationRepository.save(any())).thenReturn(reservation);
        
        Reservation result = createReservationUseCase.execute(reservation);
        
        assertNotNull(result);
        assertEquals("Juan Perez", result.getCustomerName());
        verify(reservationRepository).save(reservation);
    }
    
    @Test
    void shouldThrowExceptionWhenReservationOverlaps() {
        LocalDateTime startTime = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
        LocalDateTime endTime = startTime.plusHours(1);
        
        Reservation reservation = new Reservation(
            "Juan Perez",
            "juan@email.com",
            startTime,
            endTime,
            "Consulta médica",
            1L
        );
        
        Reservation existingReservation = new Reservation();
        existingReservation.setStartTime(startTime);
        existingReservation.setEndTime(endTime);
        existingReservation.setStatus(ReservationStatus.ACTIVE);
        
        when(reservationRepository.findOverlappingReservations(any(), any()))
            .thenReturn(Collections.singletonList(existingReservation));
        
        assertThrows(IllegalArgumentException.class, () -> {
            createReservationUseCase.execute(reservation);
        });
        
        verify(reservationRepository, never()).save(any());
    }
    
    @Test
    void shouldThrowExceptionWhenDailyLimitExceeded() {
        LocalDateTime startTime = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
        LocalDateTime endTime = startTime.plusHours(1);
        
        Reservation reservation = new Reservation(
            "Juan Perez",
            "juan@email.com",
            startTime,
            endTime,
            "Consulta médica",
            1L
        );
        
        when(reservationRepository.findOverlappingReservations(any(), any()))
            .thenReturn(Collections.emptyList());
        when(reservationRepository.findByUserIdAndDate(eq(1L), any(LocalDate.class)))
            .thenReturn(Collections.nCopies(3, new Reservation()));
        
        assertThrows(IllegalArgumentException.class, () -> {
            createReservationUseCase.execute(reservation);
        });
        
        verify(reservationRepository, never()).save(any());
    }
}
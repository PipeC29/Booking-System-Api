package com.bookingsystem.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

class ReservationTest {
    
    @Test
    void shouldCreateValidReservation() {
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
        
        assertNotNull(reservation);
        assertEquals("Juan Perez", reservation.getCustomerName());
        assertEquals(ReservationStatus.ACTIVE, reservation.getStatus());
    }
    
    @Test
    void shouldThrowExceptionForInvalidTimeRange() {
        LocalDateTime startTime = LocalDateTime.now().minusHours(1);
        LocalDateTime endTime = startTime.plusHours(1);
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Reservation(
                "Juan Perez",
                "juan@email.com",
                startTime,
                endTime,
                "Consulta médica",
                1L
            );
        });
    }
    
    @Test
    void shouldThrowExceptionForWeekendReservation() {
        LocalDateTime startTime = LocalDateTime.now().plusDays(7).withHour(10).withMinute(0);
        while (startTime.getDayOfWeek().getValue() < 6) {
            startTime = startTime.plusDays(1);
        }
        LocalDateTime endTime = startTime.plusHours(1);
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Reservation(
                "Juan Perez",
                "juan@email.com",
                startTime,
                endTime,
                "Consulta médica",
                1L
            );
        });
    }
    
    @Test
    void shouldDetectOverlappingReservations() {
        LocalDateTime baseTime = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
        
        Reservation reservation1 = new Reservation(
            "Juan Perez",
            "juan@email.com",
            baseTime,
            baseTime.plusHours(2),
            "Consulta 1",
            1L
        );
        
        Reservation reservation2 = new Reservation();
        reservation2.setStartTime(baseTime.plusHours(1));
        reservation2.setEndTime(baseTime.plusHours(3));
        
        assertTrue(reservation1.overlapsWith(reservation2));
    }
    
    @Test
    void shouldCancelReservation() {
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
        
        reservation.cancel();
        assertEquals(ReservationStatus.CANCELLED, reservation.getStatus());
    }
}
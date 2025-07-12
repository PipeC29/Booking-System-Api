package com.bookingsystem.infrastructure.adapter.web;

import com.bookingsystem.application.usecase.CreateReservationUseCase;
import com.bookingsystem.application.usecase.GetReservationsUseCase;
import com.bookingsystem.application.usecase.CancelReservationUseCase;
import com.bookingsystem.domain.model.Reservation;
import com.bookingsystem.infrastructure.adapter.web.dto.CreateReservationRequest;
import com.bookingsystem.infrastructure.adapter.web.dto.ReservationResponse;
import com.bookingsystem.infrastructure.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservations")
@Tag(name = "Reservations", description = "Reservation management endpoints")
public class ReservationController {
    
    private final CreateReservationUseCase createReservationUseCase;
    private final GetReservationsUseCase getReservationsUseCase;
    private final CancelReservationUseCase cancelReservationUseCase;
    private final JwtUtil jwtUtil;
    
    public ReservationController(CreateReservationUseCase createReservationUseCase,
                               GetReservationsUseCase getReservationsUseCase,
                               CancelReservationUseCase cancelReservationUseCase,
                               JwtUtil jwtUtil) {
        this.createReservationUseCase = createReservationUseCase;
        this.getReservationsUseCase = getReservationsUseCase;
        this.cancelReservationUseCase = cancelReservationUseCase;
        this.jwtUtil = jwtUtil;
    }
    
    @PostMapping
    @Operation(summary = "Create a new reservation", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ReservationResponse> createReservation(
            @Valid @RequestBody CreateReservationRequest request,
            HttpServletRequest httpRequest) {
        
        Long userId = getUserIdFromToken(httpRequest);
        
        Reservation reservation = new Reservation(
            request.getCustomerName(),
            request.getCustomerEmail(),
            request.getStartTime(),
            request.getEndTime(),
            request.getDescription(),
            userId
        );
        
        Reservation createdReservation = createReservationUseCase.execute(reservation);
        ReservationResponse response = toResponse(createdReservation);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping
    @Operation(summary = "Get all reservations", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<ReservationResponse>> getAllReservations() {
        List<Reservation> reservations = getReservationsUseCase.execute();
        List<ReservationResponse> responses = reservations.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/my")
    @Operation(summary = "Get user's reservations", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<ReservationResponse>> getMyReservations(HttpServletRequest httpRequest) {
        Long userId = getUserIdFromToken(httpRequest);
        List<Reservation> reservations = getReservationsUseCase.executeByUserId(userId);
        List<ReservationResponse> responses = reservations.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(responses);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Cancel a reservation", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id, HttpServletRequest httpRequest) {
        Long userId = getUserIdFromToken(httpRequest);
        cancelReservationUseCase.execute(id, userId);
        return ResponseEntity.noContent().build();
    }
    
    private Long getUserIdFromToken(HttpServletRequest request) {
        String token = jwtUtil.extractTokenFromRequest(request);
        return jwtUtil.extractUserId(token);
    }
    
    private ReservationResponse toResponse(Reservation reservation) {
        return new ReservationResponse(
            reservation.getId(),
            reservation.getCustomerName(),
            reservation.getCustomerEmail(),
            reservation.getStartTime(),
            reservation.getEndTime(),
            reservation.getDescription(),
            reservation.getStatus().name(),
            reservation.getUserId()
        );
    }
}
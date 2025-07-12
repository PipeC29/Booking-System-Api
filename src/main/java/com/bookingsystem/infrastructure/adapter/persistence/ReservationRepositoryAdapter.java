package com.bookingsystem.infrastructure.adapter.persistence;

import com.bookingsystem.domain.model.Reservation;
import com.bookingsystem.domain.model.ReservationStatus;
import com.bookingsystem.domain.port.ReservationRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ReservationRepositoryAdapter implements ReservationRepository {
    
    private final JpaReservationRepository jpaReservationRepository;
    
    public ReservationRepositoryAdapter(JpaReservationRepository jpaReservationRepository) {
        this.jpaReservationRepository = jpaReservationRepository;
    }
    
    @Override
    public Reservation save(Reservation reservation) {
        ReservationEntity entity = toEntity(reservation);
        ReservationEntity savedEntity = jpaReservationRepository.save(entity);
        return toDomain(savedEntity);
    }
    
    @Override
    public Optional<Reservation> findById(Long id) {
        return jpaReservationRepository.findById(id).map(this::toDomain);
    }
    
    @Override
    public List<Reservation> findAll() {
        return jpaReservationRepository.findAll()
            .stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Reservation> findByUserId(Long userId) {
        return jpaReservationRepository.findByUserId(userId)
            .stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Reservation> findByUserIdAndDate(Long userId, LocalDate date) {
        LocalDateTime dateTime = date.atStartOfDay();
        return jpaReservationRepository.findByUserIdAndDate(userId, dateTime)
            .stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Reservation> findOverlappingReservations(LocalDateTime startTime, LocalDateTime endTime) {
        return jpaReservationRepository.findOverlappingReservations(startTime, endTime)
            .stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(Long id) {
        jpaReservationRepository.deleteById(id);
    }
    
    @Override
    public boolean existsById(Long id) {
        return jpaReservationRepository.existsById(id);
    }
    
    private ReservationEntity toEntity(Reservation reservation) {
        ReservationEntity entity = new ReservationEntity();
        entity.setId(reservation.getId());
        entity.setCustomerName(reservation.getCustomerName());
        entity.setCustomerEmail(reservation.getCustomerEmail());
        entity.setStartTime(reservation.getStartTime());
        entity.setEndTime(reservation.getEndTime());
        entity.setDescription(reservation.getDescription());
        entity.setStatus(ReservationStatusEntity.valueOf(reservation.getStatus().name()));
        entity.setUserId(reservation.getUserId());
        return entity;
    }
    
    private Reservation toDomain(ReservationEntity entity) {
        Reservation reservation = new Reservation();
        reservation.setId(entity.getId());
        reservation.setCustomerName(entity.getCustomerName());
        reservation.setCustomerEmail(entity.getCustomerEmail());
        reservation.setStartTime(entity.getStartTime());
        reservation.setEndTime(entity.getEndTime());
        reservation.setDescription(entity.getDescription());
        reservation.setStatus(ReservationStatus.valueOf(entity.getStatus().name()));
        reservation.setUserId(entity.getUserId());
        return reservation;
    }
}
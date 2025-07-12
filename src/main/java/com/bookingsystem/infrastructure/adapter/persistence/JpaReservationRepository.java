package com.bookingsystem.infrastructure.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JpaReservationRepository extends JpaRepository<ReservationEntity, Long> {
    
    List<ReservationEntity> findByUserId(Long userId);
    
    @Query("SELECT r FROM ReservationEntity r WHERE r.userId = :userId AND DATE(r.startTime) = DATE(:date)")
    List<ReservationEntity> findByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDateTime date);
    
    @Query("SELECT r FROM ReservationEntity r WHERE r.startTime < :endTime AND r.endTime > :startTime AND r.status = 'ACTIVE'")
    List<ReservationEntity> findOverlappingReservations(@Param("startTime") LocalDateTime startTime, 
                                                        @Param("endTime") LocalDateTime endTime);
}
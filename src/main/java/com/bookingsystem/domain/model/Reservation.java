package com.bookingsystem.domain.model;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

public class Reservation {
    private Long id;
    private String customerName;
    private String customerEmail;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;
    private ReservationStatus status;
    private Long userId;

    private static final int MAX_RESERVATIONS_PER_DAY = 3;
    private static final int START_HOUR = 8;
    private static final int END_HOUR = 18;
    private static final Set<DayOfWeek> WORK_DAYS = Set.of(
        DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, 
        DayOfWeek.THURSDAY, DayOfWeek.FRIDAY
    );

    public Reservation() {}

    public Reservation(String customerName, String customerEmail, LocalDateTime startTime, 
                      LocalDateTime endTime, String description, Long userId) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.status = ReservationStatus.ACTIVE;
        this.userId = userId;
        validateReservation();
    }

    public boolean isValidBusinessHours() {
        return startTime.getHour() >= START_HOUR && 
               endTime.getHour() <= END_HOUR && 
               WORK_DAYS.contains(startTime.getDayOfWeek());
    }

    public boolean isValidTimeRange() {
        return startTime.isBefore(endTime) && startTime.isAfter(LocalDateTime.now());
    }

    public boolean overlapsWith(Reservation other) {
        return this.startTime.isBefore(other.endTime) && this.endTime.isAfter(other.startTime);
    }

    public void cancel() {
        this.status = ReservationStatus.CANCELLED;
    }

    private void validateReservation() {
        if (!isValidTimeRange()) {
            throw new IllegalArgumentException("Invalid time range for reservation");
        }
        if (!isValidBusinessHours()) {
            throw new IllegalArgumentException("Reservation must be within business hours (8-18) on weekdays");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public static int getMaxReservationsPerDay() {
        return MAX_RESERVATIONS_PER_DAY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", status=" + status +
                '}';
    }
}
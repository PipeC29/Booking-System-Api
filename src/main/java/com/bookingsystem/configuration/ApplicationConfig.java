package com.bookingsystem.configuration;

import com.bookingsystem.application.service.AuthenticationService;
import com.bookingsystem.application.usecase.CancelReservationUseCase;
import com.bookingsystem.application.usecase.CreateReservationUseCase;
import com.bookingsystem.application.usecase.GetReservationsUseCase;
import com.bookingsystem.domain.port.ReservationRepository;
import com.bookingsystem.domain.port.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    
    @Bean
    public CreateReservationUseCase createReservationUseCase(ReservationRepository reservationRepository) {
        return new CreateReservationUseCase(reservationRepository);
    }
    
    @Bean
    public GetReservationsUseCase getReservationsUseCase(ReservationRepository reservationRepository) {
        return new GetReservationsUseCase(reservationRepository);
    }
    
    @Bean
    public CancelReservationUseCase cancelReservationUseCase(ReservationRepository reservationRepository) {
        return new CancelReservationUseCase(reservationRepository);
    }
    
    @Bean
    public AuthenticationService authenticationService(UserRepository userRepository) {
        return new AuthenticationService(userRepository);
    }
}
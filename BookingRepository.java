package com.museum.booking.repo;

import com.museum.booking.model.AppUser;
import com.museum.booking.model.TicketBooking;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<TicketBooking, Long> {
    List<TicketBooking> findByUserOrderByTicketIdDesc(AppUser user);
}

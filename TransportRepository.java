package com.museum.booking.repo;

import com.museum.booking.model.Transport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransportRepository extends JpaRepository<Transport, Long> { }

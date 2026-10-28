package com.museum.booking.repo;

import com.museum.booking.model.Museum;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MuseumRepository extends JpaRepository<Museum, Long> {
    // Search relationship: user searches museum by name, location or description
    List<Museum> findByNameContainingIgnoreCaseOrLocationContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String name, String location, String description);
}

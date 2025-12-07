package com.rkumar.tikitly.repositories;
import com.rkumar.tikitly.models.Screen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScreenRepository extends JpaRepository<Screen, Long> {
    List<Screen> findByTheaterId(Long theaterId);

    Optional<Screen> findByName(String name);
}

package com.est.mungpe.event.repository;

import com.est.mungpe.event.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EventRepository extends JpaRepository<Event, Long> {
}

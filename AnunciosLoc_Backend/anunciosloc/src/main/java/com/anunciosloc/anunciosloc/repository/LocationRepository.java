package com.anunciosloc.anunciosloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anunciosloc.anunciosloc.model.Location;

public interface LocationRepository
        extends JpaRepository<Location, Long> { }

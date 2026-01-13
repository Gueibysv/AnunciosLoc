package com.anunciosloc.anunciosloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anunciosloc.anunciosloc.model.Advertisement;

public interface AdRepository
        extends JpaRepository<Advertisement, Long> { }

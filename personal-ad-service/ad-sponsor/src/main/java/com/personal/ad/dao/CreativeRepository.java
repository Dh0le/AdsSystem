package com.personal.ad.dao;

import com.personal.ad.entity.Creative;
import com.personal.ad.entity.CreativeUnit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreativeRepository extends JpaRepository<Creative,Long> {
}

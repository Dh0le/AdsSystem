package com.personal.ad.dao;

import com.personal.ad.entity.AdUser;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdUserRepository extends JpaRepository<AdUser, Long> {

    /**
     *
     * @param username
     * <h2>Find user via username</h2>
     * @return
     */
    AdUser findByUsername(String username);
}

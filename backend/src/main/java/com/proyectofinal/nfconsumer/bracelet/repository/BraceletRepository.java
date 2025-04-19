package com.proyectofinal.nfconsumer.bracelet.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyectofinal.nfconsumer.user.repository.User;

@Repository
public interface BraceletRepository extends JpaRepository<Bracelet, UUID> {
    Optional<Bracelet> findByUser(User user);

    Optional<Bracelet> findByNfcuid(String nfcuid);
}


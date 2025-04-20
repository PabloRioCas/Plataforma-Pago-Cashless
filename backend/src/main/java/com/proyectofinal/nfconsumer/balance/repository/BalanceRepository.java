package com.proyectofinal.nfconsumer.balance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyectofinal.nfconsumer.user.repository.User;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {
    Optional<Balance> findByUser(User user);
}

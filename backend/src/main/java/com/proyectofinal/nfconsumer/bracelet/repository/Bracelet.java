package com.proyectofinal.nfconsumer.bracelet.repository;

import java.util.UUID;

import com.proyectofinal.nfconsumer.user.repository.User;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bracelets")
public class Bracelet {

    @Id
    private UUID id;

    private String uid;

    private Double balance;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
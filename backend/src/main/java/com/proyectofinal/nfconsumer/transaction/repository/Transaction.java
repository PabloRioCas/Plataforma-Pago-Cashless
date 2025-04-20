package com.proyectofinal.nfconsumer.transaction.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.proyectofinal.nfconsumer.user.repository.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TransactionType type;

    @Column(name = "qty", nullable = false)
    private BigDecimal qty;

    @Column(name = "description")
    private String description;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "origin")
    private String origin;

    public enum TransactionType{
        PAYMENT,
        RECHARGE
    }
}

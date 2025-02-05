package com.proyectofinal.nfconsumer.models;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Avisamos de que es una Entidad Real
@Entity
//Mapeamos el modelo al nombre que tendrá la db en la migración de flyway
@Table(name = "pago")

//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentModel implements Serializable {
     
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(unique=true, nullable=false)
    private long id;


}

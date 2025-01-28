package com.proyectofinal.nfconsumer.models;
import java.io.Serializable;
import java.util.Date;

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
@Table(name = "usuario")

//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserModel implements Serializable {
    
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(unique=true, nullable=false)
    private Long id;
    
    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private String lastname;

    @Column(unique=true,nullable=false)
    private String mail;

    @Column(nullable=false)
    private Date creationDate;

}

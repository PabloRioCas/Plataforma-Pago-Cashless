package com.proyectofinal.nfconsumer.bracelet.service;

import org.springframework.stereotype.Service;

import com.proyectofinal.nfconsumer.balance.service.BalanceService;
import com.proyectofinal.nfconsumer.bracelet.controller.SyncResponse;
import com.proyectofinal.nfconsumer.bracelet.repository.Bracelet;
import com.proyectofinal.nfconsumer.bracelet.repository.BraceletRepository;
import com.proyectofinal.nfconsumer.bracelet.repository.Bracelet.State;
import com.proyectofinal.nfconsumer.exceptions.UserNotFoundException;
import com.proyectofinal.nfconsumer.exceptions.BraceletAlredyAssignedException;
import com.proyectofinal.nfconsumer.user.repository.User;
import com.proyectofinal.nfconsumer.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SyncService {

    private final UserRepository userRepository;
    private final BraceletRepository braceletRepository;
    private final BalanceService balanceService;


    public SyncResponse syncBracelet(String email, String nfcuid) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        // Verifica si ya existe una pulsera con ese UID
        braceletRepository.findByNfcuid(nfcuid).ifPresent(existing -> {
            if (!existing.getUser().getId().equals(user.getId())) {
                throw new BraceletAlredyAssignedException("Esta pulsera ya está asignada a otro usuario");
            }
            // Si ya está asignada al mismo usuario, simplemente terminamos
        });

                // Si el usuario ya tiene una pulsera registrada, no permitimos más
        if (braceletRepository.findByUser(user).isPresent()) {
            throw new BraceletAlredyAssignedException("Este usuario ya tiene una pulsera asignada");
        }

        // Asignamos nueva pulsera
        Bracelet bracelet = new Bracelet();
        bracelet.setNfcuid(nfcuid);
        bracelet.setUser(user);
        bracelet.setState(State.LINKED);
        braceletRepository.save(bracelet);
        
        // Si el usuario no tiene balance aún, lo creamos
        balanceService.firstAssignBalance(user);

        return new SyncResponse("La pulsera " + nfcuid + " ha sido registrada correctamente");
    }
}

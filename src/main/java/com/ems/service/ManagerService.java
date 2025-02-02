package com.ems.service;

import com.ems.entity.Manager;
import com.ems.repository.ManagerRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ManagerService {

    private final ManagerRepository managerRepository;

    public ManagerService(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    public boolean authenticateManager(String email, String password) {
        Optional<Manager> managerOpt = managerRepository.findByEmail(email);
        return managerOpt.isPresent() && managerOpt.get().getPassword().equals(password);
    }
}
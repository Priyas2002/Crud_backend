package com.ems.service;

import com.ems.entity.Manager;
import com.ems.repository.ManagerRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ManagerService {
	
	private static final Logger logger = LoggerFactory.getLogger(ManagerService.class);

	@Autowired
	private ManagerRepository managerRepository;
//
//	public ManagerService(ManagerRepository managerRepository) {
//		this.managerRepository = managerRepository;
//	}
	
	
	// ✅ Fetch Manager by Email
    public Manager getManagerByEmail(String email) {
        return managerRepository.findByEmail(email).orElse(null);
    }

	public boolean authenticateManager(String email, String password) {
		Optional<Manager> managerOpt = managerRepository.findByEmail(email);
		return managerOpt.isPresent() && managerOpt.get().getPassword().equals(password);
	}
}
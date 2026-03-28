package org.andreigheta.expensetracker.config;

import org.andreigheta.expensetracker.entity.Role;
import org.andreigheta.expensetracker.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

	private final RoleRepository roleRepository;

	@Override
	public void run(String... args) throws Exception {
		if (roleRepository.findByName("ROLE_USER").isEmpty()) {
			roleRepository.save(new Role(null, "ROLE_USER"));
		}

		if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
			roleRepository.save(new Role(null, "ROLE_ADMIN"));
		}
	}
}

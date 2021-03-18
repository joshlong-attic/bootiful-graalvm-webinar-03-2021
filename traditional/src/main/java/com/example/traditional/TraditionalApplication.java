package com.example.traditional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bind.annotation.BindingPriority;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.nativex.hint.TypeHint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Collection;
import java.util.List;

@SpringBootApplication
public class TraditionalApplication {

	public static void main(String[] args) {
		SpringApplication.run(TraditionalApplication.class, args);
	}

	@Bean
	ApplicationRunner runner(CustomerRepository repository) {
		return event -> {
			List
				.of("Yuxin", "Spencer", "Madhura", "Dr. Syer", "Olga", "Violetta", "Stéphane", "Ilaya")
				.stream()
				.map(name -> new Customer(null, name))
				.forEach(repository::save);

			repository.findAll().forEach(System.out::println);

		};
	}

}

@RestController
@RequiredArgsConstructor
class CustomerRestController {

	private final CustomerRepository customerRepository;

	@GetMapping("/customers")
	Collection<Customer> customers() {
		return this.customerRepository.findAll();
	}


}

interface CustomerRepository extends JpaRepository<Customer, Integer> {
}

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
class Customer {

	@Id
	@GeneratedValue
	private Integer id;
	private String name;
}
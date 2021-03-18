package com.example.reactive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;


@SpringBootApplication
public class ReactiveApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveApplication.class, args);
	}

	@Bean
	ApplicationRunner runner(DatabaseClient dbc, CustomerRepository repository) {
		return event -> {

			var ddl = dbc.sql("create table customer(id serial primary key, name varchar (255) not null)").fetch().rowsUpdated();

			var customers = Flux.just("A", "B", "D", "E")
				.flatMap(name -> repository.save(new Customer(null, name)));

			ddl.thenMany(customers).subscribe(System.out::println);
		};
	}

	@Bean
	RouterFunction<ServerResponse> routes(CustomerRepository repository) {
		return route()
			.GET("/customers", r -> ok().body(repository.findAll(), Customer.class))
			.build();
	}
}


@Data
@AllArgsConstructor
@NoArgsConstructor
class Greeting {
	private String message;
}

@Controller
class RSocketController {

	@MessageMapping("greetings.{name}")
	Flux<Greeting> greet(@DestinationVariable String name) {
		return Flux.fromStream(Stream.generate(() -> new Greeting("Hello, " + name + " @ " + Instant.now() + "!"))).delayElements(Duration.ofSeconds(1));
	}
}


interface CustomerRepository extends ReactiveCrudRepository<Customer, Integer> {
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Customer {

	@Id
	private Integer id;
	private String name;
}
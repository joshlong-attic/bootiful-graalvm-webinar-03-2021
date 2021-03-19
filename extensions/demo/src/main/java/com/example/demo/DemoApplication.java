package com.example.demo;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.nativex.hint.ProxyHint;
import org.springframework.nativex.hint.TypeHint;
import org.springframework.stereotype.Component;

import java.util.UUID;


// 1. reflection
// 3. proxies
// 2. resources

@Log4j2
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	Animal animal() {
		var pfb = new ProxyFactoryBean();
		pfb.addInterface(Animal.class);
		pfb.addAdvice((MethodInterceptor) method -> {
			System.out.println("before");

			if (method.getMethod().getName().equals("talk"))
				return "Hello Spring Native";

			var result = method.proceed();
			log.info("after");
			return result;
		});
		return (Animal) pfb.getObject();
	}

}

interface Animal {

	String talk();
}

@Log4j2
@Component
@RequiredArgsConstructor
class Initializer implements ApplicationListener<ApplicationReadyEvent> {

	private final Animal animal;

	@SneakyThrows
	@SuppressWarnings("unchecked")
	<T> T load(String clazz) {
		Class<?> aClass = Class.forName(clazz);
		return (T) aClass.getDeclaredConstructor().newInstance();
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
		Foo foo = load("com.example.demo.Foo");
		log.info("foo.uid=" + foo.uid);
		log.info("message: " + animal.talk());
	}
}

class Foo {
	String uid = UUID.randomUUID().toString();
}
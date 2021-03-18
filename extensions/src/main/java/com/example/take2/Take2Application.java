package com.example.take2;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.nativex.hint.ProxyHint;
import org.springframework.nativex.hint.TypeHint;

/*

// NB: we demonstrate this in this codebase first, then move the hints to a separate module, `myhints`, that gets added to the classpath of the maven plugin. 

@TypeHint(types = Foo.class)
@ProxyHint(
	types = {
		Animal.class,
		org.springframework.aop.SpringProxy.class,
		org.springframework.aop.framework.Advised.class,
		org.springframework.core.DecoratingProxy.class
	}
)*/
@SpringBootApplication
public class Take2Application {

	public static void main(String[] args) {
		SpringApplication.run(Take2Application.class, args);
	}

	@Bean
	Animal animal() {
		var pfb = new ProxyFactoryBean();
		pfb.setTargetClass(Animal.class);
		pfb.addAdvice((MethodInterceptor) methodInvocation -> {
			System.out.println("before");

			if (methodInvocation.getMethod().getName().contains("talk"))
				return "HODOR";

			var result = methodInvocation.proceed();
			System.out.println("after");
			return result;
		});
		return (Animal) pfb.getObject();
	}

	@Bean
	ApplicationRunner runner(Animal animal) {
		return args -> {
			var name = Foo.class.getName();
			System.out.println("the name is " + name);
			System.out.println("say something: " + animal.talk());
		};
	}

}

interface Animal {

	String talk();

}


class Foo {
}

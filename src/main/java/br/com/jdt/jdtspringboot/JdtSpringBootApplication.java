package br.com.jdt.jdtspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("br.com.jdt.jdtspringboot.model.entity")
public class JdtSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(JdtSpringBootApplication.class, args);
	}

}

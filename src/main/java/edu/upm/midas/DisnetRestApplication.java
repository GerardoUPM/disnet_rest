package edu.upm.midas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@EnableFeignClients
@EnableCircuitBreaker
@EnableHystrix
@SpringBootApplication
public class DisnetRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(DisnetRestApplication.class, args);
	}
}

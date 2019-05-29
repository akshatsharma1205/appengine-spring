package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;


@SpringBootApplication
@RestController
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	
	@GetMapping("/sendotp")
	public String hello() {
		return "hello world!";
	}
	
	private static final String template = "%s";
	private final AtomicLong counter = new AtomicLong();
	
	@RequestMapping("/number")
	public PhoneNumber phoneNumber(@RequestParam(value="name", defaultValue="World") String name) {
		SendOtp so = new SendOtp("", "+91" + name);
		so.Send_OTP();
		return new PhoneNumber(counter.incrementAndGet(),
				String.format(template, name));
		
		
	}
}

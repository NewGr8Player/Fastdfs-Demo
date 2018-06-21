package com.xavier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author NewGr8Player
 */
@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class FastDFSApplication {

	public static void main(String[] args) {
		SpringApplication.run(FastDFSApplication.class, args);
	}
}
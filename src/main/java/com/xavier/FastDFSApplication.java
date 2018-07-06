package com.xavier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.support.RegistrationPolicy;

import javax.servlet.MultipartConfigElement;

/**
 * @author NewGr8Player
 */
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
@SpringBootApplication
public class FastDFSApplication extends SpringBootServletInitializer {

	@Value("${fastdfs.max_file_size}")
	private String maxFileSize;
	@Value("${fastdfs.max_request_size}")
	private String maxRequestSize;

	public static void main(String[] args) {
		SpringApplication.run(FastDFSApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(FastDFSApplication.class);
	}

	@Bean
	public MultipartConfigElement multipartConfigElement(){
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize(maxFileSize);
		factory.setMaxRequestSize(maxRequestSize);
		return factory.createMultipartConfig();
	}
}
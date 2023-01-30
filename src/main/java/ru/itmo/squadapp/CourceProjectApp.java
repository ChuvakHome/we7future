package ru.itmo.squadapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ru.itmo.squadapp.vk.VKBean;

@SpringBootApplication
public class CourceProjectApp {
	public static void main(String[] args) {
		SpringApplication
		.run(new Class[] {
			VKBean.class,
			CourceProjectApp.class
		}, args);
	}
}

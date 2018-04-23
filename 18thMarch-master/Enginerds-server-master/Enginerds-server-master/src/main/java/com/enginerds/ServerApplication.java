package com.enginerds;

import java.io.File;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

import com.enginerds.services.PersonService;
import com.enginerds.services.SanctionService;
import com.enginerds.services.TransactionService;

@SpringBootApplication
@EntityScan(basePackages = {"com.enginerds.domain"})
@ComponentScan(basePackages = "com.enginerds")
public class ServerApplication {

	@Autowired
	TransactionService transactionservice;
	
	@Autowired
	SanctionService sanctionService;
	
	@Autowired
	PersonService personService;
	
	@PostConstruct
    public void combine()
	{
		File Archive = new File("Archive");
		Archive.mkdir();
		File Files = new File("Files");
		Files.mkdir();
		sanctionService.readSanctionList("sanctionList.txt");
		personService.createUser("customerList.txt");
		transactionservice.invokeMethods();
		transactionservice.count();
    }
	
	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}
}

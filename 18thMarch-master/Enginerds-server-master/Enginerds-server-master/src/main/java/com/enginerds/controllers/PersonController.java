package com.enginerds.controllers;

import com.enginerds.domain.Transaction;
import com.enginerds.services.TransactionService;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/person")

@CrossOrigin(origins = "http://localhost:4200" , allowedHeaders = "*")

public class PersonController {
    
    @Autowired
    private TransactionService transactionService;
    
    @RequestMapping("/api")
    public ArrayList<Transaction> getTransaction()
    {
		return (ArrayList)transactionService.findAllTransaction();
	}
}

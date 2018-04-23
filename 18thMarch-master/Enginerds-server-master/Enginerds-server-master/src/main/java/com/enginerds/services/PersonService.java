package com.enginerds.services;

import java.util.ArrayList;
import java.util.List;

import com.enginerds.domain.*;
import com.enginerds.services.Implementation.RollbackException;

public interface PersonService{

    public Person savePerson(Person person) throws Exception;
    public void createUser(String filename);
    public Person findOnebyId(Transaction transaction);
    public boolean doTransaction(Transaction transaction) throws RollbackException;
    public void withdraw(String payerName, float Amt);
    public void deposit(String payeeName, float Amt);
    public boolean checkBalance(Transaction transaction);

    public ArrayList<Person> selectRecord();
	
}
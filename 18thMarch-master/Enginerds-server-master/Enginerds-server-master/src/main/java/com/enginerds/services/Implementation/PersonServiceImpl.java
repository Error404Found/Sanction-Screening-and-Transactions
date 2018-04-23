package com.enginerds.services.Implementation;

import com.enginerds.ServerApplication;
import com.enginerds.domain.Person;
import com.enginerds.domain.Transaction;
import com.enginerds.repositories.PersonRepository;
import com.enginerds.repositories.TransactionRepository;
import com.enginerds.services.PersonService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

import org.springframework.transaction.annotation.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service ("personService")
public class PersonServiceImpl implements PersonService {
	
    @Autowired
    PersonRepository personRepository;
    
    @Autowired
    TransactionRepository transactionRepository;
    
	private static final Logger logger = LogManager.getLogger(ServerApplication.class);

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Person savePerson(Person person) throws Exception {
        return personRepository.save(person);
    }

    public boolean checkBalance(Transaction transaction)
    {
    	Person person = new Person();
    	person = findOnebyId(transaction);
    	boolean checkBool = true;
    	if(person.getBalance() < transaction.getAmount())
    		checkBool = false;
    	return checkBool;
    }
    
	@Override
	public void createUser(String filename) {
    
        Random rand = new Random();
        int range = rand.nextInt(1000000);
        float balance;       
        String charString = "0123456789";
        
        try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String name = new String();
			while((name=br.readLine()) != null)
			{
		        StringBuilder sb = new StringBuilder();
			    Person person;
	        	balance = rand.nextFloat()*range;
	            for( int i = 0 ; i < 12 ; i++ ) 
	                sb.append( charString.charAt( rand.nextInt(charString.length())));
	    		person = new Person(sb.toString(),name,balance);
	    		System.out.println(person.toString());
	    		try {
					savePerson(person);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}		
	}
	
    public ArrayList<Person> selectRecord() 
    {
        return (ArrayList<Person>) personRepository.findAll();
    }

	@Override
	public Person findOnebyId(Transaction transaction) 
	{
		return personRepository.findOne(transaction.getPayerAccount());
	}
	
	 @Transactional(rollbackFor=RollbackException.class)
	public boolean doTransaction(Transaction transaction) throws RollbackException
	{
		 	Random rand=new Random();
		 	boolean value = true;
		 	int num=rand.nextInt(100);
			withdraw(transaction.getPayerName(),transaction.getAmount());
			RollbackException r = new RollbackException();
			try 
			{
				if(num<5) {
					value = false;
					throw r;
				}
				
			} catch (Exception e) 
			{}
			deposit(transaction.getPayeeName(),transaction.getAmount());
			logger.info("**************TRANSACTION COMMITED**************");
			return value;	
	}
	
	 @Transactional
	public void withdraw(String payerName, float Amt) 
	{
		Person person = personRepository.findByname(payerName);
		float bal = person.getBalance();
		bal = bal - Amt;
		person.setBalance(bal);
		personRepository.save(person);
		
	}
	
	 @Transactional
	public void deposit(String payeeName, float Amt) 
	{
		Person person = personRepository.findByname(payeeName);
		float bal = person.getBalance();
		bal = bal + Amt;
		person.setBalance(bal);
		personRepository.save(person);
	}

}
package com.enginerds.services.Implementation;

import com.enginerds.ServerApplication;
import com.enginerds.domain.Person;
import com.enginerds.domain.Status;
import com.enginerds.domain.Transaction;
import com.enginerds.repositories.TransactionRepository;
import com.enginerds.services.PersonService;
import com.enginerds.services.SanctionService;
import com.enginerds.services.TransactionService;
import com.querydsl.core.types.Predicate;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("transactionService")
public class TransactionServiceImpl implements TransactionService {
    Predicate predicate;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    PersonService personService;
    @Autowired
    SanctionService sanctionService;
    
    public TransactionServiceImpl() {}

	private static final Logger logger = LogManager.getLogger(ServerApplication.class);
    
    ExecutorService executor = Executors.newFixedThreadPool(2);
	List<Callable<Void>> taskList = new ArrayList<Callable<Void>>();

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
    
    Callable<Void> callable1 = new Callable<Void>() {
		@Override
		public Void call() throws Exception
		{
			createTransactionFile();
			return null;
		}
	};
	
	Callable<Void> callable2 = new Callable<Void>() 
	{
		@Override
		public Void call() throws Exception
		{
			Polling();
			return null;
		}
	};
	@Override
	public void invokeMethods() {
	    taskList.add(callable1);
	    taskList.add(callable2);
	    try {
			executor.invokeAll(taskList,40,TimeUnit.SECONDS);
			executor.shutdown();
			System.out.println("yepppiiii");
			
		} catch (InterruptedException e) {}
	}

    @Override
    public Transaction saveTransaction(Transaction transaction) throws Exception {
        return transactionRepository.save(transaction);
    }
    @Override
    public void createTransactionFile() throws IOException
    {
    	logger.info("\nInside createTransaction function");
    	ArrayList<Person> personList = personService.selectRecord();
    	int noOfrecords = personList.size();
    	String charSet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    	Random rand = new Random();
    	String payerName,payeeName,payername,payeename;
    	String filename;
    	String date;
    	String payerAccount,payeeAccount;
    	int index,files = 0;
    	float amount;
    	while(files < 10) {
	    	BufferedWriter writer = null;
	    	String curr = new File( "." ).getCanonicalPath();
	    	filename = curr+"\\Files"+"\\sample"+(files+1)+".txt";
	    	try {
	    		writer = new BufferedWriter(new FileWriter(filename));
		    	
		    	for(int numOfRecords = 0 ; numOfRecords < 10 ; numOfRecords++)
		    	{
		    		DecimalFormat f = new DecimalFormat("##.00");
		            StringBuilder refId = new StringBuilder();
		            for(int i = 0 ; i < 12 ; i++)
		                refId.append(charSet.charAt(rand.nextInt(charSet.length())));
		            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		            Calendar cal = Calendar.getInstance();
		            date = dateFormat.format(cal.getTime());
		            date = date.replaceAll("/","");
		            
		            index = rand.nextInt(noOfrecords);
		            payerName = personList.get(index).getName();
		            payername = String.format("%-35s",payerName);
		            payerAccount = personList.get(index).getAccount();
		            
		            index = rand.nextInt(noOfrecords);
		            payeeName = personList.get(index).getName();
		            payeename = String.format("%-35s",payeeName);
		            payeeAccount = personList.get(index).getAccount();
		            
		            amount = rand.nextFloat()*10000;
		            String string_form = f.format(amount);
	                string_form = String.format("%1$13s",string_form);
		            
		            writer.append(refId.toString());
		            writer.append(date);
		            writer.append(payername);
		            writer.append(payerAccount);
		            writer.append(payeename);
		            writer.append(payeeAccount);
		            writer.append(string_form);
		            writer.append("\r\n");
		    	}
		    	writer.close();
	    	}catch(Exception e) {}
	    	files++;
    	}
    }
    
    @Override
	public Transaction readFile(String filename)
    {
    	BufferedReader br;
        Transaction transaction = null;
        try 
        {
        	logger.info("Inside readFile method");
            String current = new File( "." ).getCanonicalPath();
            br = new BufferedReader(new FileReader(current+"\\Files\\"+filename));
            String line = new String();
            boolean value=false;
            while((line=br.readLine()) != null)
            {
                logger.info("Inside read file");
                System.out.println(line);
                transaction = getParameters(line);
                String status;
                try {
					value = fieldValidate(transaction);
					if(value == true)
					{
						status = new String(new Status().fieldValidPass);
						if(personService.checkBalance(transaction) == true)
						{
							value = sanctionService.CheckSanctionList(transaction);
							if(value == true) {
								status = new String(new Status().statusValidPass);
								value = personService.doTransaction(transaction);
								if(value != true)
									status = new String("ROLLBACK");
							}
							else
								status = new String(new Status().statusValidFail);
						}else
							status = new String(new Status().balanceValidFail);
		            }		
					else {
						status = new String(new Status().fieldValidFail);
					}
					transaction.setStatus(status);
					saveTransaction(transaction);					
				} catch (Exception e) {}
            }
            br.close();
	        Path temp = Files.move(Paths.get(current+"\\Files\\"+filename),Paths.get(current+"\\Archive\\"+filename));
	 
	        if(temp != null)
	            logger.info("\nFile renamed and moved successfully");
	        else
	            logger.info("\nFailed to move the file");
        } 
        catch (Exception e){
            e.printStackTrace();
        }
        return transaction;
    }
	public boolean fieldValidate(Transaction transaction) throws ParseException
	{
        Calendar cal = Calendar.getInstance();
        Date date1;
        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        date1 = (Date) formatter.parse(formatter.format(cal.getTime()));
		boolean value = true;
		
		if(!isAlphaNumeric(transaction.getId()) || !isAlphaNumeric(transaction.getPayerName()) || !isAlphaNumeric(transaction.getPayeeName()) || !isAlphaNumeric(transaction.getPayerAccount()) || !isAlphaNumeric(transaction.getPayeeAccount()))
			value = false;
		if((transaction.getAmount() < 0 )|| transaction.getDate().compareTo(date1) != 0)
			value = false;
		return value;
	}
    public boolean isAlphaNumeric(String s)
    {
        String pattern= "^[a-zA-Z0-9]*$";
        return s.matches(pattern);
    }
    
    public Transaction getParameters(String line)
    {
        char CharArr[]=new char[127];
        CharArr=line.toCharArray();
        char TransChar[]=new char[12];
        char PayerName[]=new char[35];
        char PayeeName[]=new char[35];
        char PayerAccount[] = new char[12];
        char PayeeAccount[] = new char[12];
        String payeeName = null,payerName = null,str = "",RefId = null;
        float amount = 0;
        char Amount[] = new char[12];
        Date date = null;
        int lineIndex = 0 , size = 0;

    	for(int counter = 0 ; counter < 12 ; counter++,lineIndex++)					// Extracting the reference ID of transaction
    		TransChar[counter] = CharArr[lineIndex];
    	RefId = new String(TransChar);
    	
    	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    	for(int counter = 0 ; counter < 8 ; counter++,lineIndex++)
    	{
            str = str + CharArr[lineIndex];
            if( counter == 1 || counter == 3 )
            	str = str + '/';
    	}
        try {
            date = (Date) formatter.parse(str);										// Extracting the date of transaction
         }catch (ParseException e){
         }
        
        for(int counter = 0 ; CharArr[lineIndex] != ' ' ; counter++,lineIndex++,size++)
            PayerName[counter]=CharArr[lineIndex];
        PayerName = Arrays.copyOfRange(PayerName, 0 , size);
        size = 0;
        lineIndex = 55;
        payerName = new String(PayerName);											// Extracting the Payer name of transaction
        
        for(int counter = 0 ; counter < 12 ; counter++,lineIndex++)
        	PayerAccount[counter] = CharArr[lineIndex];
        String payerAccount = new String(PayerAccount);								// Extracting the Payer Account of transaction
        
        for(int counter = 0 ; CharArr[lineIndex] != ' ' ; counter++,lineIndex++,size++)
            PayeeName[counter]=CharArr[lineIndex];
        PayeeName = Arrays.copyOfRange(PayeeName, 0 , size);
        lineIndex = 102;
        payeeName = new String(PayeeName);											// Extracting the Payee name of transaction
        
        for(int counter = 0 ; counter < 12 ; counter++,lineIndex++)
        	PayeeAccount[counter] = CharArr[lineIndex];
        String payeeAccount = new String(PayeeAccount);								// Extracting the Payee Account of transaction
        
        while(CharArr[lineIndex] == ' ' && lineIndex >= 114)
            lineIndex++;
        int index=lineIndex;           
        for(int counter = 0 ; counter < 127-index ; counter++,lineIndex++)
            Amount[counter] = CharArr[lineIndex];     
        String amt = new String(Amount);    
        amount = Float.parseFloat(amt);												// Extracting the Amount of transaction
        String status = new String(new Status().uploadFail);
        Transaction transaction = new Transaction(RefId,payerName,payeeName,date,amount,payerAccount,payeeAccount,status);
        return transaction;
    }
    @Override
    public void Polling() throws IOException,InterruptedException 
    {
    	 logger.info("\nInside Polling function");
         String current = new File( "." ).getCanonicalPath();
		 Path faxFolder = Paths.get(current+"\\Files");
		 WatchService watchService = FileSystems.getDefault().newWatchService();
		 faxFolder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
		 String fileName = new String();
		 boolean valid = true;
		 do{
			WatchKey watchKey = watchService.take();
		
			for (WatchEvent event : watchKey.pollEvents()) {
				WatchEvent.Kind kind = event.kind();
				if (StandardWatchEventKinds.ENTRY_CREATE.equals(event.kind())) 
				{
					fileName = event.context().toString();
					System.out.println("File Created: " + fileName);
					readFile(fileName);	
				}
			}
			valid = watchKey.reset();
		} while (true);	 
	}
    @Override
    public Object findAllTransaction()
    {
    	return transactionRepository.findAll();
    }

	@Override
	public int count() {
		ArrayList<Transaction> transaction = (ArrayList<Transaction>) transactionRepository.findAll();
		int sanctionCount = 0;
		for(int index = 0 ; index < transaction.size() ; index++)
		{
			Transaction t = transaction.get(index);
			if(t.getStatus().equals("screen_validation_pass"))
				sanctionCount++;
		}
		logger.info("\nNumber of sanction count is: "+sanctionCount);
		return sanctionCount;
	}
}

package com.enginerds.server;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.enginerds.domain.Person;
import com.enginerds.services.PersonService;
import com.enginerds.services.Implementation.PersonServiceImpl;
import com.enginerds.services.Implementation.TransactionServiceImpl;

import junit.framework.Assert;

import static org.mockito.Mockito.*;

import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class CreateTransactionFileTest {
	   private PersonService personService;
	   @Autowired
	   private PersonServiceImpl personServiceImpl;
	   private ArrayList<Person> value;
	   
	@Before
	public void setUp() {
		personService = mock(PersonService.class);
		Person person = new Person("902897322101","LorintzToker",(float) 53450.75);
		value = new ArrayList<Person>();
		value.add(person);
	}
	@Test
	public void contextLoads() {
		when(personService.selectRecord()).thenReturn(value);
		
		Assert.assertEquals(personServiceImpl.selectRecord(), value);
		
	}

}

package com.enginerds.domain;

import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;

@Entity
@Table(name="Person")
public class Person {
    @Id
    @ApiModelProperty(notes = "The database generated person ID")
    private String account;

    @Column(name="Name")
    @ApiModelProperty(notes = "The name of  person", required = true)
    private String name;
    private float balance;
    
    public Person() {}
    public Person(String account,String name,float balance){
        this.account = account;
        this.name = name;
        this.balance = balance;
    }
    
    public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getBalance() {
		return balance;
	}
	public void setBalance(float balance) {
		this.balance = balance;
	}
	@Override
	public String toString()
	{
		return " Account Number: "+account+" Name: "+name+" Balance: "+balance;
	}
	
}




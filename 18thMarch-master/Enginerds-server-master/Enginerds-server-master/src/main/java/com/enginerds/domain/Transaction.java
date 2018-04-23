package com.enginerds.domain;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name="Transaction")
public class Transaction {
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(notes = "The database generated transaction ID")
    private String RefId;

    @Column(name="PayerName")
    @ApiModelProperty(notes = "The name of  transaction", required = true)
    private String payerName;
    private String payeeName;
    private String payerAccount;
    private String payeeAccount;
    private float amount;
    private Date date;
    private String status;
    
    public Transaction() {}

    public Transaction(String RefId,String payerName,String payeeName,Date date,float amount,String payerAccount,String payeeAccount,String status){
    	this.RefId = RefId;
        this.payerName = payerName;
        this.payerAccount = payerAccount;
        this.payeeName = payeeName;
        this.payeeAccount = payeeAccount;
        this.date = date;
        this.amount = amount;
        this.status = status;
    }
    
    public String getId() {
		return RefId;
	}
	public void setId(String RefId) {
		this.RefId = RefId;
	}
	public String getPayerName() {
		return payerName;
	}
	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}
	public String getPayerAccount() {
		return payerAccount;
	}
	public void setPayerAccount(String payerAccount) {
		this.payerAccount = payerAccount;
	}
	public String getPayeeAccount() {
		return payeeAccount;
	}
	public void setPayeeAccount(String payeeAccount) {
		this.payeeAccount = payeeAccount;
	}
	public String getPayeeName() {
		return payeeName;
	}
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}




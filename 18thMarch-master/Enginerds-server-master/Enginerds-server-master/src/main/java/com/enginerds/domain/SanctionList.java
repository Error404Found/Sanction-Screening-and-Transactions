package com.enginerds.domain;

import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;

@Entity
@Table(name="SanctionList")
public class SanctionList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(notes = "The database generated person ID")
    private int id;

    @Column(name="Name")
    @ApiModelProperty(notes = "The name of  person", required = true)
    private String name;

    public  SanctionList(){
    }
    public SanctionList(String name){
        this.name = name;
    }
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}




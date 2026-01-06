package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class ChargeCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chargecodeId;
    
    @Column(unique = true, nullable = false)
    private String chargeCode; // INDIA, EMEA, APAC, USA


    private String chargeCodeName;
    @ManyToOne
    private Users chargeCodeApprover;
	public Long getChargecodeId() {
		return chargecodeId;
	}
	public void setChargecodeId(Long chargecodeId) {
		this.chargecodeId = chargecodeId;
	}
	public String getChargeCode() {
		return chargeCode;
	}
	public void setChargeCode(String chargeCode) {
		this.chargeCode = chargeCode;
	}
	public String getChargeCodeName() {
		return chargeCodeName;
	}
	public void setChargeCodeName(String chargeCodeName) {
		this.chargeCodeName = chargeCodeName;
	}
	public Users getChargeCodeApprover() {
		return chargeCodeApprover;
	}
	public void setChargeCodeApprover(Users chargeCodeApprover) {
		this.chargeCodeApprover = chargeCodeApprover;
	}
    
    
}

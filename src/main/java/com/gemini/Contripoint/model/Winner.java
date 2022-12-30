package com.gemini.Contripoint.model;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Table(name = "winner")
public class Winner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Integer amount;
    private String empId;
    private String rewardType;
    private String voucher;
    private String rewardDate;

    public Winner(Integer amount, String empId, String rewardType, String voucher, String rewardDate) {
        this.amount = amount;
        this.empId = empId;
        this.rewardType = rewardType;
        this.voucher = voucher;
        this.rewardDate = rewardDate;
    }

    public Winner(String empId) {
        this.empId = empId;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getRewardType() {
        return rewardType;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType;
    }

    public String getRewardDate() {
        return rewardDate;
    }

    public void setRewardDate(String rewardDate) {
        this.rewardDate = rewardDate;
    }
}

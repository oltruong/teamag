package com.oltruong.teamag.model;

import javax.persistence.Column;



public abstract class Delegable implements IModel {


    @Column(nullable = false)
    private Boolean delegated = Boolean.FALSE;

    private String comment;

    private Double amount;

    public Boolean getDelegated() {
        return delegated;
    }

    public void setDelegated(Boolean delegated) {
        this.delegated = delegated;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}

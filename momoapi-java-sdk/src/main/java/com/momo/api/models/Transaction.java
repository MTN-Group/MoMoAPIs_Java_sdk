package com.momo.api.models;

import java.io.Serializable;

/**
 *
 * Class Transaction
 */
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    private String amount;
    private String currency;
    private String externalId;
    private String payerMessage;
    private String payeeNote;

    /**
     * 
     * @return 
     */
    public String getAmount() {
        return amount;
    }

    /**
     * 
     * @param amount 
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }

    /**
     * 
     * @return 
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * 
     * @param currency 
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * 
     * @return 
     */
    public String getExternalId() {
        return externalId;
    }

    /**
     * 
     * @param externalId 
     */
    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    /**
     * 
     * @return 
     */
    public String getPayerMessage() {
        return payerMessage;
    }

    /**
     * 
     * @param payerMessage 
     */
    public void setPayerMessage(String payerMessage) {
        this.payerMessage = payerMessage;
    }

    /**
     * 
     * @return 
     */
    public String getPayeeNote() {
        return payeeNote;
    }

    /**
     * 
     * @param payeeNote 
     */
    public void setPayeeNote(String payeeNote) {
        this.payeeNote = payeeNote;
    }
}

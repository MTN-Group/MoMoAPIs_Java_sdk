package com.momo.api.models;

import java.io.Serializable;

/**
 *
 * Class AccountBalance
 */
public class AccountBalance implements Serializable {

    private static final long serialVersionUID = 1L;

    private String availableBalance;
    private String currency;

    /**
     * 
     * @return 
     */
    public String getAvailableBalance() {
        return availableBalance;
    }

    /**
     * 
     * @param availableBalance 
     */
    public void setAvailableBalance(String availableBalance) {
        this.availableBalance = availableBalance;
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
}

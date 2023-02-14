package com.momo.api.models;

/**
 *
 * Class Transfer
 */
public class Transfer extends Transaction {

    private static final long serialVersionUID = 1L;

    private Payee payee;

    /**
     *
     * @return
     */
    public Payee getPayee() {
        return payee;
    }

    /**
     *
     * @param payee
     */
    public void setPayee(Payee payee) {
        this.payee = payee;
    }
}

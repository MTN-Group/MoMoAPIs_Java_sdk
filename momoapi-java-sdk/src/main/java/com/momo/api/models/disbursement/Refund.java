package com.momo.api.models.disbursement;

import com.momo.api.models.Transaction;

/**
 *
 * Class Refund
 */
public class Refund extends Transaction {

    private static final long serialVersionUID = 1L;

    private String referenceIdToRefund;

    /**
     * 
     * @return 
     */
    public String getReferenceIdToRefund() {
        return referenceIdToRefund;
    }

    /**
     * This referenceId is received from 'requestToPay' method, made during collection request
     * 
     * @param referenceIdToRefund 
     */
    public void setReferenceIdToRefund(String referenceIdToRefund) {
        this.referenceIdToRefund = referenceIdToRefund;
    }
}

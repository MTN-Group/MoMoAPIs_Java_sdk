package com.momo.api.models.disbursement;

import com.momo.api.models.Payee;
import com.momo.api.models.TransactionStatus;

/**
 *
 * Class RefundStatus
 */
public class RefundStatus extends TransactionStatus {

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

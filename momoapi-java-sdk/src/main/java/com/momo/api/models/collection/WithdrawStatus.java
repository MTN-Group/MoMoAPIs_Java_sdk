package com.momo.api.models.collection;

import com.momo.api.models.Payer;
import com.momo.api.models.TransactionStatus;

/**
 *
 * Class PayStatus
 */
public class WithdrawStatus extends TransactionStatus {

    private static final long serialVersionUID = 1L;

    private Payer payer;

    /**
     * 
     * @return 
     */
    public Payer getPayer() {
        return payer;
    }

    /**
     * 
     * @param payer 
     */
    public void setPayer(Payer payer) {
        this.payer = payer;
    }
}

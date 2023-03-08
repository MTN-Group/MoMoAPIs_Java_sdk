package com.momo.api.models;

/**
 *
 * Class TransactionStatus
 */
public class TransactionStatus extends Transaction {

    private static final long serialVersionUID = 1L;

    private String financialTransactionId;
    private String status;
    private String reason;
//    private Reason reason;

    /**
     * 
     * @return 
     */
    public String getFinancialTransactionId() {
        return financialTransactionId;
    }

    /**
     * 
     * @param financialTransactionId 
     */
    public void setFinancialTransactionId(String financialTransactionId) {
        this.financialTransactionId = financialTransactionId;
    }

    /**
     * 
     * @return 
     */
    public String getStatus() {
        return status;
    }

    /**
     * 
     * @param status 
     */
    public void setStatus(String status) {
        this.status = status;
    }

//    public Reason getReason() {
//        return reason;
//    }
//
//    public void setReason(Reason reason) {
//        this.reason = reason;
//    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

package com.momo.api.models;

import java.io.Serializable;

/**
 * *
 * Class AccountHolder
 */
public class AccountHolder implements Serializable {

    private static final long serialVersionUID = -6178355438770692536L;

    //TODO do we need to validate if it is all in small cases and not capital
    // Provides the account holder type
    private String accountHolderIdType;

    // Provides the account holder type value
    private String accountHolderId;

    /**
     * AccountHolder Constructor with arguments
     *
     * @param accountHolderIdType
     * @param accountHolderId
     */
    public AccountHolder(final String accountHolderIdType, final String accountHolderId) {
        this.accountHolderIdType = accountHolderIdType;
        this.accountHolderId = accountHolderId;
    }

    /**
     * @return
     */
    public String getAccountHolderIdType() {
        return this.accountHolderIdType;
    }

    /**
     *
     * @return
     */
    public String getAccountHolderId() {
        return this.accountHolderId;
    }
}

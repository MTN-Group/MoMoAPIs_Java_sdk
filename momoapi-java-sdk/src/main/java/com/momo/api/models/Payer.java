package com.momo.api.models;

import java.io.Serializable;

/**
 *
 * Class Payer
 */
public class Payer implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String partyIdType;
    private String partyId;

    /**
     * 
     * @return 
     */
    public String getPartyIdType() {
        return partyIdType;
    }

    /**
     * 
     * @param partyIdType 
     */
    public void setPartyIdType(String partyIdType) {
        this.partyIdType = partyIdType;
    }

    /**
     * 
     * @return 
     */
    public String getPartyId() {
        return partyId;
    }

    /**
     * 
     * @param partyId 
     */
    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

}

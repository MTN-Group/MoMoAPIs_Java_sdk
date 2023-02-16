package com.momo.api.base.constants;

/**
 *
 * Enum IdType
 */
public enum IdType {
    //TODO do we need to validate each type accordingly?
    MSISDN("MSISDN"),//Mobile Number validated according to ITU-T E.164
    EMAIL("EMAIL"),//Validated to be a valid e-mail format
    PARTY_CODE("PARTY_CODE");//UUID of the party

    private final String idType;

    /**
     *
     * @param idType
     */
    IdType(final String idType) {
        this.idType = idType;
    }

    /**
     * 
     * @return 
     */
    public String getValue() {
        return this.idType;
    }
    
    /**
     * 
     * @return 
     */
    public String getValueInLowerCase() {
        return this.idType.toLowerCase();
    }
}

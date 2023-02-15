package com.momo.api.base.constants;

/**
 *
 * Enum IdType
 */
public enum IdType {
    MSISDN("MSISDN"),
    EMAIL("EMAIL"),
    PARTY_CODE("PARTY_CODE");

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
    public String getIdType() {
        return this.idType;
    }
    
    /**
     * 
     * @return 
     */
    public String getIdTypeLowerCase() {
        return this.idType.toLowerCase();
    }
}

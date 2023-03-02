package com.momo.api.base.constants;

/**
 *
 * Enum IdType
 */
public enum IdType {
    //TODO do we need to validate each type accordingly?
    //Mobile Number can be validated according to ITU-T E.164
    MSISDN("MSISDN"),
    /**
     * lower case IdType is needed when passing IdType value in URL as a path
     * parameter. Eg:- In "validateAccountHolderStatus"
     */
    msisdn("msisdn"),
    //Can be validated to a valid e-mail format
    EMAIL("EMAIL"),
    /**
     * lower case IdType is needed when passing IdType value in URL as a path
     * parameter. Eg:- In "validateAccountHolderStatus"
     */
    email("email"),
    //Can be validated as a UUID of the party
    PARTY_CODE("PARTY_CODE"),
    /**
     * lower case IdType is needed when passing IdType value in URL as a path
     * parameter. Eg:- In "validateAccountHolderStatus"
     */
    party_code("party_code");

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
}

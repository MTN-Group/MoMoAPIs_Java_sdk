package com.momo.api.base.constants;

/**
 *
 * Enum IdType
 */
public enum IdType {
    //TODO do we need to validate each type accordingly?
    MSISDN("MSISDN"),//Mobile Number validated according to ITU-T E.164
    msisdn("msisdn"),
    EMAIL("EMAIL"),//Validated to be a valid e-mail format
    email("email"),
    PARTY_CODE("PARTY_CODE"),//UUID of the party
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

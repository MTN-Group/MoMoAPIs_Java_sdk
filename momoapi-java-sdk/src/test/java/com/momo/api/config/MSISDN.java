package com.momo.api.config;

import java.util.Random;

//TODO do we need to validate MSISDN length
/**
 *
 * ENUM MSISDN
 */
public enum MSISDN {
    /**
     * A predefined MSISDN number from the MoMo documentation, that may return a
     * "FAILED status due to the reason INTERNAL_PROCESSING_ERROR" or "404
     * status due to the reason RESOURCE_NOT_FOUND", depending on the API
     * request made
     */
    MSISDN_46733123450("46733123450"),
    /**
     * A predefined MSISDN number from the MoMo documentation, that may return a
     * "FAILED status due to the reason APPROVAL_REJECTED" or "500 status due to
     * the reason INTERNAL_PROCESSING_ERROR", depending on the API request made
     */
    MSISDN_46733123451("46733123451"),
    /**
     * A predefined MSISDN number from the MoMo documentation, that may return a
     * "FAILED status due to the reason EXPIRED" or "500 status due to the
     * reason NOT_ALLOWED", depending on the API request made
     */
    MSISDN_46733123452("46733123452"),
    /**
     * A predefined MSISDN number from the MoMo documentation, that may return a
     * "PENDING status" or "500 status due to the reason
     * NOT_ALLOWED_TARGET_ENVIRONMENT", depending on the API request made
     */
    MSISDN_46733123453("46733123453"),
    /**
     * A predefined MSISDN number from the MoMo documentation, that may return a
     * "PENDING status" or "500 status due to the reason
     * INTERNAL_PROCESSING_ERROR", depending on the API request made
     */
    MSISDN_46733123454("46733123454"),
    /**
     * An MSISDN number that may return "FAILED status due to the reasons
     * PAYER_NOT_FOUND/NOT_ENOUGH_FUNDS" or "503 status due to the reason
     * SERVICE_UNAVAILABLE", depending on the API request made
     */
    MSISDN_46733123455("46733123455"),
    /**
     * An MSISDN number that may return "FAILED status due to the reasons
     * PAYEE_NOT_ALLOWED_TO_RECEIVE/PAYER_LIMIT_REACHED", depending on the API
     * request made
     */
    MSISDN_46733123456("46733123456"),
    /**
     * An MSISDN number that may return "FAILED status due to the reason
     * NOT_ALLOWED/PAYEE_NOT_FOUND", depending on the API request made
     */
    MSISDN_46733123457("46733123457"),
    /**
     * An MSISDN number that may return "FAILED status due to the reason
     * NOT_ALLOWED_TARGET_ENVIRONMENT/NOT_ALLOWED", depending on the API request
     * made
     */
    MSISDN_46733123458("46733123458"),
    /**
     * An MSISDN number that may return "FAILED status due to the reason
     * INVALID_CALLBACK_URL_HOST/NOT_ALLOWED_TARGET_ENVIRONMENT", depending on
     * the API request made
     */
    MSISDN_46733123459("46733123459"),
    /**
     * An MSISDN number that may return "FAILED status due to the reason
     * INVALID_CURRENCY/INVALID_CALLBACK_URL_HOST", depending on the API request
     * made
     */
    MSISDN_46733123460("46733123460"),
    /**
     * An MSISDN number that may return "FAILED status due to the reason
     * INTERNAL_PROCESSING_ERROR/INVALID_CURRENCY", depending on the API request
     * made
     */
    MSISDN_46733123461("46733123461"),
    /**
     * An MSISDN number that may return "FAILED status due to the reason
     * SERVICE_UNAVAILABLE/INTERNAL_PROCESSING_ERROR", depending on the API
     * request made
     */
    MSISDN_46733123462("46733123462"),
    /**
     * An MSISDN number that may return "FAILED status due to the reason
     * COULD_NOT_PERFORM_TRANSACTION/SERVICE_UNAVAILABLE", depending on the API
     * request made
     */
    MSISDN_46733123463("46733123463"),
    /**
     * A random MSISDN value that returns a successful response for a valid API
     * request made
     */
    SUCCESSFUL("23423423450");//SUCCESSFUL

    private final String msisdn;

    private static final Random RANDOM = new Random();

    /**
     * Constructor
     *
     * @param msisdn
     */
    MSISDN(String msisdn) {
        this.msisdn = msisdn;
    }

    /**
     * Returns the MSISDN number as String
     *
     * @return
     */
    public String getValue() {
        return this.msisdn;
    }

    /**
     * Returns all the MSISDN numbers as an array of MSISDN object
     *
     * @return
     */
    public static MSISDN[] getValues() {
        return values();
    }

    /**
     * Returns the MSISDN object when the MSISDN number is passed in
     *
     * @param msisdn
     * @return
     */
    public static MSISDN getMSISDN(String msisdn) {
        for (MSISDN status : values()) {
            if (status.msisdn.equals(msisdn)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid MSISDN: " + msisdn);
    }

    /**
     * Returns a random MSISDN object from the available list of ENUM variables
     *
     * @return
     */
    public static MSISDN getRandomMSISDN() {
        return values()[RANDOM.nextInt(values().length)];
    }
}

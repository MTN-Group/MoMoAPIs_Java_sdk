package com.momo.api.base.constants;

/**
 *
 * Enum AccessType
 */
public enum AccessType {
    ONLINE("online"),
    OFFLINE("offline");

    private final String accessType;

    /**
     *
     * @param accessType
     */
    AccessType(final String accessType) {
        this.accessType = accessType;
    }

    /**
     * 
     * @return 
     */
    public String getValue() {
        return this.accessType;
    }
}

package com.momo.api.base.constants;

/**
 *
 * Enum XTargetEnvironment
 */
public enum XTargetEnvironment {
    MTN_UGANDA("mtnuganda"),
    MTN_GHANA("mtnghana"),
    MTN_IVORY_COAST("mtnivorycoast"),
    MTN_ZAMBIA("mtnzambia"),
    MTN_CAMEROON("mtncameroon"),
    MTN_BENIN("mtnbenin"),
    MTN_CONGO("mtncongo"),
    MTN_SWAZILAND("mtnswaziland"),
    MTN_GUINEA_CONAKRY("mtnguineaconakry"),
    MTN_SOUTHA_FRICA("mtnsouthafrica"),
    MTN_LIBERIA("mtnliberia"),
    SANDBOX("sandbox");

    private final String xTargetEnvironment;

    /**
     *
     * @param xTargetEnvironment
     */
    XTargetEnvironment(final String xTargetEnvironment) {
        this.xTargetEnvironment = xTargetEnvironment;
    }

    /**
     *
     * @return
     */
    public String getXTargetEnvironment() {
        return this.xTargetEnvironment;
    }
}

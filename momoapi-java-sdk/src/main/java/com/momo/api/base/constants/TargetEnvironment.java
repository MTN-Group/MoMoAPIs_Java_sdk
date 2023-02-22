package com.momo.api.base.constants;

/**
 *
 * Enum TargetEnvironment
 */
public enum TargetEnvironment {
    mtnuganda("mtnuganda"),
    mtnghana("mtnghana"),
    mtnivorycoast("mtnivorycoast"),
    mtnzambia("mtnzambia"),
    mtncameroon("mtncameroon"),
    mtnbenin("mtnbenin"),
    mtncongo("mtncongo"),
    mtnswaziland("mtnswaziland"),
    mtnguineaconakry("mtnguineaconakry"),
    mtnsouthafrica("mtnsouthafrica"),
    mtnliberia("mtnliberia"),
    sandbox("sandbox");

    private final String targetEnvironment;

    /**
     *
     * @param TargetEnvironment
     */
    TargetEnvironment(final String targetEnvironment) {
        this.targetEnvironment = targetEnvironment;
    }

    /**
     *
     * @return
     */
    public String getValue() {
        return this.targetEnvironment;
    }
}

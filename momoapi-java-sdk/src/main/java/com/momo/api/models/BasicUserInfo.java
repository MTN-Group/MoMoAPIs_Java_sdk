package com.momo.api.models;

import java.io.Serializable;

/**
 *
 * Class BasicUserInfo
 */
public class BasicUserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String given_name;
    private String family_name;
    private String birthdate;
    private String locale;
    private String gender;
    private String status;

    /**
     * 
     * @return 
     */
    public String getGiven_name() {
        return given_name;
    }

    /**
     * 
     * @param given_name 
     */
    public void setGiven_name(String given_name) {
        this.given_name = given_name;
    }

    /**
     * 
     * @return 
     */
    public String getFamily_name() {
        return family_name;
    }

    /**
     * 
     * @param family_name 
     */
    public void setFamily_name(String family_name) {
        this.family_name = family_name;
    }

    /**
     * 
     * @return 
     */
    public String getBirthdate() {
        return birthdate;
    }

    /**
     * 
     * @param birthdate 
     */
    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    /**
     * 
     * @return 
     */
    public String getLocale() {
        return locale;
    }

    /**
     * 
     * @param locale 
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
     * 
     * @return 
     */
    public String getGender() {
        return gender;
    }

    /**
     * 
     * @param gender 
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * 
     * @return 
     */
    public String getStatus() {
        return status;
    }

    /**
     * 
     * @param status 
     */
    public void setStatus(String status) {
        this.status = status;
    }
}

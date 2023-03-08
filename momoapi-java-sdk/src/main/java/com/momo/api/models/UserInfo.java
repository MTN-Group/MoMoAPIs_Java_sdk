package com.momo.api.models;

/**
 *
 * Class UserInfo
 */
public class UserInfo extends BasicUserInfo {

    private static final long serialVersionUID = 1L;

    private String sub;
    private String name;
    private String middle_name;
    private String email;
    private Boolean email_verified;
    private String phone_number;
    private Boolean phone_number_verified;
    private String address;
    private Long updated_at;
    private String credit_score;
    private Boolean active;
    private String country_of_birth;
    private String region_of_birth;
    private String city_of_birth;
    private String occupation;
    private String employer_name;
    private String identification_type;
    private String identification_value;

    /**
     * 
     * @return 
     */
    public String getSub() {
        return sub;
    }

    /**
     * 
     * @param sub 
     */
    public void setSub(String sub) {
        this.sub = sub;
    }

    /**
     * 
     * @return 
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name 
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return 
     */
    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    /**
     * 
     * @return 
     */
    public String getEmail() {
        return email;
    }

    /**
     * 
     * @param email 
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 
     * @return 
     */
    public Boolean getEmail_verified() {
        return email_verified;
    }

    /**
     * 
     * @param email_verified 
     */
    public void setEmail_verified(Boolean email_verified) {
        this.email_verified = email_verified;
    }

    /**
     * 
     * @return 
     */
    public String getPhone_number() {
        return phone_number;
    }

    /**
     * 
     * @param phone_number 
     */
    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    /**
     * 
     * @return 
     */
    public Boolean getPhone_number_verified() {
        return phone_number_verified;
    }

    /**
     * 
     * @param phone_number_verified 
     */
    public void setPhone_number_verified(Boolean phone_number_verified) {
        this.phone_number_verified = phone_number_verified;
    }

    /**
     * 
     * @return 
     */
    public String getAddress() {
        return address;
    }

    /**
     * 
     * @param address 
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 
     * @return 
     */
    public Long getUpdated_at() {
        return updated_at;
    }

    /**
     * 
     * @param updated_at 
     */
    public void setUpdated_at(Long updated_at) {
        this.updated_at = updated_at;
    }

    /**
     * 
     * @return 
     */
    public String getCredit_score() {
        return credit_score;
    }

    /**
     * 
     * @param credit_score 
     */
    public void setCredit_score(String credit_score) {
        this.credit_score = credit_score;
    }

    /**
     * 
     * @return 
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * 
     * @param active 
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * 
     * @return 
     */
    public String getCountry_of_birth() {
        return country_of_birth;
    }

    /**
     * 
     * @param country_of_birth 
     */
    public void setCountry_of_birth(String country_of_birth) {
        this.country_of_birth = country_of_birth;
    }

    /**
     * 
     * @return 
     */
    public String getRegion_of_birth() {
        return region_of_birth;
    }

    /**
     * 
     * @param region_of_birth 
     */
    public void setRegion_of_birth(String region_of_birth) {
        this.region_of_birth = region_of_birth;
    }

    /**
     * 
     * @return 
     */
    public String getCity_of_birth() {
        return city_of_birth;
    }

    /**
     * 
     * @param city_of_birth 
     */
    public void setCity_of_birth(String city_of_birth) {
        this.city_of_birth = city_of_birth;
    }

    /**
     * 
     * @return 
     */
    public String getOccupation() {
        return occupation;
    }

    /**
     * 
     * @param occupation 
     */
    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    /**
     * 
     * @return 
     */
    public String getEmployer_name() {
        return employer_name;
    }

    /**
     * 
     * @param employer_name 
     */
    public void setEmployer_name(String employer_name) {
        this.employer_name = employer_name;
    }

    /**
     * 
     * @return 
     */
    public String getIdentification_type() {
        return identification_type;
    }

    /**
     * 
     * @param identification_type 
     */
    public void setIdentification_type(String identification_type) {
        this.identification_type = identification_type;
    }

    /**
     * 
     * @return 
     */
    public String getIdentification_value() {
        return identification_value;
    }

    /**
     * 
     * @param identification_value 
     */
    public void setIdentification_value(String identification_value) {
        this.identification_value = identification_value;
    }
}

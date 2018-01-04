package com.ElyAdam.AELYLab13_1;

import java.io.Serializable;

/**
 * Created by Owner on 11/28/2015.
 */
public class Contact implements Serializable {

    private String mFirstName;
    private String mLastName;
    private String mEmail;
    private String mPhoneNumber;

    public Contact(String firstName, String lastName, String email, String phoneNumber){
        mFirstName = firstName;
        mLastName = lastName;
        mEmail = email;
        mPhoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public String display() {

        String returnVariables = mFirstName + ", " + mLastName + ": " + mEmail + " " +
                mPhoneNumber + "\n";
        return returnVariables;
    }
}

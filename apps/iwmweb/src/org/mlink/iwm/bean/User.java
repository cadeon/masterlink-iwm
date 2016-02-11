package org.mlink.iwm.bean;

/**
 * User: andrei
 * Date: Jan 15, 2007
 */
public class User {
	private java.lang.String userId;
	private java.lang.String username;
	private java.lang.String password;
	private java.lang.String oldPassword;
	private java.lang.String personId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }
}

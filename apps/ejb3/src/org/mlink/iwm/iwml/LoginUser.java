package org.mlink.iwm.iwml;


/**
 * User: andreipovodyrev
 * Date: Nov 21, 2007
 */
public class LoginUser {
    
    private String j_username;
    private String j_password;
    private String authenticated;
	
    public String getJ_username() {
		return j_username;
	}
	public void setJ_username(String j_username) {
		this.j_username = j_username;
	}
	public String getJ_password() {
		return j_password;
	}
	public void setJ_password(String j_password) {
		this.j_password = j_password;
	}
	public String getAuthenticated() {
		return authenticated;
	}
	public void setAuthenticated(String authenticated) {
		this.authenticated = authenticated;
	}
	
	public String toString(){
		return getJ_username()+" is authenticated: "+getAuthenticated();
	}
}

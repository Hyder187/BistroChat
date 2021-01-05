package com.mustafa.i170253_i170009;

public class Contact {

    private String name,phno,email,dp;

    public Contact(String name, String phno, String email,String dp) {
        this.name = name;
        this.phno = phno;
        this.email = email;
        this.dp=dp;
    }
    public Contact(){

    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

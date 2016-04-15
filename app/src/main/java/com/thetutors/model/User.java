package com.thetutors.model;

/**
 * Created by billysusanto on 3/21/2016.
 */
public class User {
    String username;
    String password;

    public User(){}

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return  this.password;
    }
}

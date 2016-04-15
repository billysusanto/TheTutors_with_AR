package com.thetutors.model;

/**
 * Created by billysusanto on 1/23/2016.
 */
public class Tutorial {
    String title;
    String content;

    public Tutorial(){}

    public Tutorial (String title, String content){
        this.title=title;
        this.content = content;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return this.title;
    }

    public void setContent(String content){
        this.content = content;
    }

    public String getContent(){
        return this.content;
    }
}

package com.thetutors.model;

import android.app.Application;

/**
 * Created by billysusanto on 3/10/2016.
 */
public class Init extends Application {
    int page;
    String contentType;
    String title;
    int resource;

    public Init(){}

    public Init(int page, String contentType, String title, int resource) {
        this.page = page;
        this.contentType = contentType;
        this.title = title;
        this.resource = resource;
    }

    public int getPage() {
        return this.page;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setPage(int page) {
        this.page=page;
    }

    public void setContentType(String contentType){
        this.contentType = contentType;
    }

    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public int getResource(){
        return this.resource;
    }

    public void setResource(int resource){
        this.resource = resource;
    }

}

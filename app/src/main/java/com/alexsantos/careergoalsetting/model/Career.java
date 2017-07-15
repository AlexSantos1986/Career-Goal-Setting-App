package com.alexsantos.careergoalsetting.model;

import android.view.View;

import java.util.Date;

/**
 * Created by Alex on 05/06/2017.
 */

public class Career {

    private String description;
    private String date;
    private String title;
    private boolean checked = false;

    public Career(){

    }

    public boolean isChecked() {
        return checked;
    }


    public void setDescription(String description){
        this.description = description;
    }
    public void setDate(String date){
        this.date = date;
    }

    public String getDescription(){
        return description;
    }



    public String getDate(){

        return date;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){

        return title;
    }




}

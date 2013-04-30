/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamglokk.simplenews;

/**
 *
 * @author shieldsr
 */
public class NewsStory {
    
    private int number;
    private String headline;
    private String story; 
    
    public NewsStory (int number, String headline, String story) {
        this.number = number;
        this.headline = headline;
        this.story    = story; 
    }
    
    public int getNumber(){
        return this.number;
    }
    
    public String getHeadline (){
        return this.headline;
    }
    
    public String getStory() {
        return this.story;
    }
    
    public void setNumber (int n){
        this.number = n;
    }
    
    public void setHeadline (String h ) {
        this.headline = h; 
    }
    
    public void setStory (String s) {
        this.story = s; 
    }
    
}

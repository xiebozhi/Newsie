/* 
 * SimpleNews 
 * Copyright (C) 2013 bobbshields <https://github.com/xiebozhi/SimpleNews> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 * Binary releases are available freely at <http://dev.bukkit.org/server-mods/simplenews/>.
*/
package com.teamglokk.newsie;

import java.util.Date;
import org.bukkit.command.CommandSender;

/**
 *
 * @author shieldsr
 */
public class NewsStory {
    
    private int number;
    private long timestamp;
    private String headline;
    private String body1 = ""; 
    private String body2 = ""; 
    private String body3 = ""; 
    private String body4 = ""; 
    
    public NewsStory () {
        this.timestamp = System.currentTimeMillis();
    }
    public NewsStory (NewsStory n){
        this.number = n.getNumber();
        this.timestamp = n.getTimestamp();
        this.headline = n.getHeadline();
        this.body1    = n.getBody1(); 
        this.body2    = n.getBody2(); 
        this.body3    = n.getBody3();
        this.body4    = n.getBody4();
    }
    public NewsStory (int number, String headline, String body1 ) {
        this.number = number;
        this.timestamp = System.currentTimeMillis();
        this.headline = headline;
        this.body1    = body1; 
    }
    public NewsStory (int number, String headline, String body1, String body2 ) {
        this.number = number;
        this.timestamp = System.currentTimeMillis();
        this.headline = headline;
        this.body1    = body1; 
        this.body2    = body2; 
    }
    public NewsStory (int number, String headline, String body1, String body2, String body3) {
        this.number = number;
        this.timestamp = System.currentTimeMillis();
        this.headline = headline;
        this.body1    = body1; 
        this.body2    = body2; 
        this.body3    = body3; 
    }
    public NewsStory (int number, String headline, String body1, String body2, String body3, String body4 ) {
        this.number = number;
        this.timestamp = System.currentTimeMillis();
        this.headline = headline;
        this.body1    = body1; 
        this.body2    = body2; 
        this.body3    = body3; 
        this.body4    = body4; 
    }
    public NewsStory (int number, long timestamp, String headline, String body1, String body2, String body3, String body4 ) {
        this.number = number;
        this.timestamp = timestamp;
        this.headline = headline;
        this.body1    = (body1 == null ? "" : body1); 
        this.body2    = (body2 == null ? "" : body2); 
        this.body3    = (body3 == null ? "" : body3); 
        this.body4    = (body4 == null ? "" : body4); 
    }
    
    /**
     * Gets the valid database column names
     * @return 
     */
    public String toDB_Cols(){
        return "id,timestamp,headline,body1,body2,body3,body4";
    }
    
    /**
     * Gets the database values in the order of db_Cols()
     * @return 
     */
    public String toDB_Vals(){
        return "'"+getNumber()+"', '"+getTimestamp()+"', '"+getHeadline()+"', '"+
                getBody1()+"', '"+getBody2()+"', '"+getBody3()+"', '"+getBody4()+"'";
    }
    
        /**
     * Gives special string to allow a database update to the whole row in the wrapper
     * @return 
     */ 
    public String toDB_UpdateRowVals(){
        return "id='"+getNumber()+"', timestamp='"+getTimestamp()+
                "', headline='"+getHeadline()+
                "', body1='"+getBody1()+"', body2='"+getBody2()+
                "', body3='"+getBody3()+"', body4='"+ getBody4() +"'";
    }
    
    public boolean isValid(){
        if (this.headline == null || this.headline.equals("")){
            return false;
        }
        if ( ( this.body1 == null || this.body1.equals("") ) && (this.body2 != null || !this.body2.equals("")) ){
            return false; 
        }
        if ( ( this.body2 == null || this.body2.equals("") ) && (this.body3 != null || !this.body3.equals("")) ){
            return false; 
        }
        if ( ( this.body3 == null || this.body3.equals("") ) && (this.body4 != null || !this.body4.equals("")) ){
            return false; 
        }
        return true;
    }
    
    public boolean isValid(CommandSender sender){
        if (this.headline == null || this.headline.equals("")){
            sender.sendMessage("You must have a headline");
            return false;
        }/*
        if ( ( this.body1 == null || this.body1.equals("") ) && !(this.body2 == null || this.body2.equals("")) ){
            sender.sendMessage("You need to have something in line 1 if you want to use line 2");
            return false; 
        }
        if ( ( this.body2 == null || this.body2.equals("") ) && !(this.body3 == null || this.body3.equals("")) ){
            sender.sendMessage("You need to have something in line 2 if you want to use line 3");
            return false; 
        }
        if ( ( this.body3 == null || this.body3.equals("") ) && !(this.body4 == null || this.body4.equals("")) ){
            sender.sendMessage("You need to have something in line 3 if you want to use line 4");
            return false; 
        } */
        return true;
    }
    
    public void displayStory(CommandSender s){
        
        s.sendMessage("This is article number "+getNumber()+
                " and it was created at " +new Date(this.timestamp));
        s.sendMessage(getHeadline());
        if (getBody1() != null && getBody1() != "" ){
            s.sendMessage( getBody1() );
        }
        if (getBody2() != null && getBody2() != "" ){
            s.sendMessage( getBody2() );
        }
        if (getBody3() != null && getBody3() != "" ){
            s.sendMessage( getBody3() );
        }
        if (getBody4() != null && getBody4() != "" ){
            s.sendMessage( getBody4() );
        }
        
    }
    
    public int getNumber(){
        return this.number;
    }
    
    public long getTimestamp(){
        return this.timestamp;
    }
    
    public String getHeadline (){
        return this.headline;
    }
    
    public String getBody1() {
        return this.body1;
    }
    public String getBody2() {
        return this.body2;
    }
    public String getBody3() {
        return this.body3;
    }
    public String getBody4() {
        return this.body4;
    }
    
    public void setNumber (int n){
        this.number = n;
    }
    
    public boolean setHeadline (String h ) {
        if (h.length()< 95){
            this.headline = h; 
            return true;
        }
        return false;
    }
    
    public boolean setBody1 (String s) {
        if (s.length()< 95){
            this.body1 = s; 
            return true;
        }
        return false;
    }
    public boolean setBody2 (String s) {
        if (s.length()< 95){
            this.body2 = s; 
            return true;
        }
        return false;
    }
    public boolean setBody3 (String s) {
        if (s.length()< 95){
            this.body3 = s; 
            return true;
        }
        return false;
    }
    public boolean setBody4 (String s) {
        if (s.length()< 95){
            this.body4 = s; 
            return true;
        }
        return false;
    }
    
}

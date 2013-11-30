/* 
 * Newsie 
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//import com.teamglokk.muni;
/**
 * Wraps the database functions to provide simple functions
 * @author BobbShields
 */
public class dbWrapper extends Newsie {

    private Newsie plugin;
    
    private Connection conn = null;
    private Statement stmt = null;
    public ResultSet rs = null;
    
    public dbWrapper( Newsie instance ){
        plugin = instance;
    }
    
    /**
     * Opens a connection to the database
     * @throws SQLException 
     */
    public void db_open() throws SQLException {
        if(plugin.isSQLdebug()){
            String temp = plugin.useMysql() ? "Opening DB (mysql)":"Opening DB (sqlite)" ;
            plugin.getLogger().info(temp);
        }
        String driver = plugin.useMysql() ?"com.mysql.jdbc.Driver" :"org.sqlite.JDBC" ;
        try {
                Class.forName(driver).newInstance();
            
            } catch (Exception ex){
                plugin.getLogger().severe("db_open: driver " +driver+ " not found");
                plugin.getLogger().severe("db_open: "+ex.getMessage() );
            }
        conn = DriverManager.getConnection(plugin.getDB_URL(),plugin.getDB_user(),plugin.getDB_pass());
        
        stmt = conn.createStatement();
    }
    
    /**
     * Closes the connection to the database
     * @throws SQLException 
     */
    public void db_close() throws SQLException {
        if ( rs != null) { rs.close(); }
        if ( stmt != null) { stmt.close(); }
        if ( conn != null ) { conn.close();}
        if(plugin.isSQLdebug()){plugin.getLogger().info("Closed DB");}
    }
    
    /**
     * Returns a single story, queried by the primary key (integer called id)
     * @param storyID
     * @return 
     */
    public NewsStory getStory ( int storyID ){
        NewsStory rtn = new NewsStory();
        NewsStory blankNS = new NewsStory();
        String SQL = "SELECT "+ blankNS.toDB_Cols() +" FROM "+plugin.getDB_prefix()+"stories"+
                    " WHERE id='"+storyID +"';";
        try {
            
            db_open();
            if(plugin.isSQLdebug() ){plugin.getLogger().info(SQL);}
            rs = stmt.executeQuery(SQL); 
            rs.next();
            rtn = new NewsStory(rs.getInt("id"),rs.getLong("timestamp"),
                    rs.getString("headline"), rs.getString("body1"), rs.getString("body2"), 
                    rs.getString("body3"), rs.getString("body4") ) ;
        } catch (SQLException ex){
            if(this.isSQLdebug() ){plugin.getLogger().info( "getStory error: "+ex.getMessage() ); }
        } finally {
            try { db_close();
            } catch (SQLException ex) {
                plugin.getLogger().warning( "checkExistence: "+ex.getMessage() ); 
            } finally{}
        }
        return rtn;
    }
         /**
     * Returns a single story, queried by the primary key (integer called id)
     * @param storyID
     * @return 
     */
    public List<NewsStory> getAllStories ( ){
        List<NewsStory> rtn = new ArrayList<NewsStory>();
        NewsStory blankNS = new NewsStory();
        String SQL = "SELECT "+ blankNS.toDB_Cols() +" FROM "+plugin.getDB_prefix()+"stories;";
        try {
            
            db_open();
            if(plugin.isSQLdebug() ){plugin.getLogger().info(SQL);}
            rs = stmt.executeQuery(SQL); 
            while (rs.next() ) {
                rtn.add( new NewsStory(rs.getInt("id"),rs.getLong("timestamp"),
                        rs.getString("headline"), rs.getString("body1"), rs.getString("body2"), 
                        rs.getString("body3"), rs.getString("body4") ) );
            }
        } catch (SQLException ex){
            if(plugin.isSQLdebug() ){plugin.getLogger().info( "getStory error: "+ex.getMessage() ); }
        } finally {
            try { db_close();
            } catch (SQLException ex) {
                plugin.getLogger().warning( "checkExistence: "+ex.getMessage() ); 
            } finally{}
        }
        return rtn;
    }
    
    /**
     * Gets a list of all the citizens in the specified town in the specified role
     * @param townName
     * @return      array list of all the citizens in the town
     */
    public boolean deleteStory ( int storyID ){
        boolean rtn = false;
        
        String SQL = "DELETE FROM  "+plugin.getDB_prefix()+
                "stories WHERE id='"+storyID +"';";
        try {
            db_open();
            if(plugin.isSQLdebug() ){plugin.getLogger().warning(SQL);}
            int resultCount = stmt.executeUpdate(SQL); 
            if (resultCount == 1){
                rtn = true;
            } else { rtn =  false; }
        } catch (SQLException ex){
            plugin.getLogger().severe( "deleteStory: "+ex.getMessage() ); 
            rtn = false;
        } finally {
            try { db_close();
            } catch (SQLException ex) {
                plugin.getLogger().warning( "deleteStory: "+ex.getMessage() ); 
                rtn = false;
            } finally{}
        }
        return rtn;
    }
    
    
    
    public boolean saveStories(Collection<NewsStory> stories){
        boolean rtn = false; 
        List<String> updates = new ArrayList<String>();
        List<String> inserts = new ArrayList<String>();
        
        for (NewsStory s : stories){
            if (checkExistence(s.getNumber() ) ){
                updates.add("UPDATE "+plugin.getDB_prefix()+"stories SET "+
                    s.toDB_UpdateRowVals()+" WHERE id='"+s.getNumber()+"';");
            } else {
                inserts.add("INSERT INTO "+plugin.getDB_prefix()+"stories ("+
                        s.toDB_Cols()+") VALUES ("+s.toDB_Vals()+");");
            }
        }
        
        try {
            db_open();
            if(plugin.isSQLdebug() ){plugin.getLogger().info("DB: Saving all towns");}
            if (!updates.isEmpty() ){
                for (String SQL : updates ) {
                    stmt.executeUpdate(SQL); 
                    if(plugin.isSQLdebug() ){plugin.getLogger().info(SQL);}
                }
            } 
            if (!inserts.isEmpty() ){
                for (String SQL : inserts){
                    stmt.executeUpdate(SQL);
                    if(plugin.isSQLdebug() ){plugin.getLogger().info(SQL);}
                }
            }
        } catch (SQLException ex){
            plugin.getLogger().severe("db_saveTowns: "+ ex.getMessage() ); 
            rtn = false;
        } finally {
            try { db_close();
            } catch (SQLException ex) {
                plugin.getLogger().warning("db_saveTowns: "+ ex.getMessage() ); 
                rtn = false;
            } 
        }
        return rtn; 
    }
    public boolean saveStory( NewsStory ns){
        boolean rtn = false; 
        List<String> updates = new ArrayList<String>();
        List<String> inserts = new ArrayList<String>();
        
        if (checkExistence(ns.getNumber() ) ){
            updates.add("UPDATE "+plugin.getDB_prefix()+"stories SET "+
                ns.toDB_UpdateRowVals()+" WHERE id='"+ns.getNumber()+"';");
        } else {
            inserts.add("INSERT INTO "+plugin.getDB_prefix()+"stories ("+
                    ns.toDB_Cols()+") VALUES ("+ns.toDB_Vals()+");");
        }
        
        
        try {
            db_open();
            if(plugin.isSQLdebug() ){plugin.getLogger().info("DB: Saving all towns");}
            if (!updates.isEmpty() ){
                for (String SQL : updates ) {
                    stmt.executeUpdate(SQL); 
                    if(plugin.isSQLdebug() ){plugin.getLogger().info(SQL);}
                }
            } 
            if (!inserts.isEmpty() ){
                for (String SQL : inserts){
                    stmt.executeUpdate(SQL);
                    if(plugin.isSQLdebug() ){plugin.getLogger().info(SQL);}
                }
            }
        } catch (SQLException ex){
            plugin.getLogger().severe("db_saveTowns: "+ ex.getMessage() ); 
            rtn = false;
        } finally {
            try { db_close();
            } catch (SQLException ex) {
                plugin.getLogger().warning("db_saveTowns: "+ ex.getMessage() ); 
                rtn = false;
            } 
        }
        return rtn; 
    }
    
    public boolean checkExistence (int storyID){
         boolean rtn = false;
        String SQL = "SELECT id FROM "+plugin.getDB_prefix()+"stories"+
                    " WHERE id='"+storyID +"';";
        try {
            
            db_open();
            if(plugin.isSQLdebug() ){plugin.getLogger().info(SQL);}
            rs = stmt.executeQuery(SQL); 
            rs.next();
            int temp = rs.getInt("id");
            
            if (temp >= 0 ){ 
                rtn = true ;
                if(this.isSQLdebug() ){plugin.getLogger().info("checkExistence: value = "+temp);}
            } 
        } catch (SQLException ex){
            if(this.isSQLdebug() ){plugin.getLogger().info( "checkExistence: Value not found: " +ex.getMessage() ); }
            rtn = false;
        } finally {
            try { db_close();
            } catch (SQLException ex) {
                plugin.getLogger().warning( "checkExistence: "+ex.getMessage() ); 
                rtn = false;
            } finally{}
        }
        return rtn;
    }
    
    
    
    /**
     * Creates the database specifically for Muni 
     * @param drops true means the tables will be dropped before creating them again
     * @return      false if there was a problem
     */
    public boolean createDB (boolean drops) { 
        boolean rtn = false;
        
        String prefix = plugin.getDB_prefix();
        String DROP1 = "DROP TABLE IF EXISTS "+prefix+"stories;";
        String SQL0 = "CREATE DATABASE IF NOT EXISTS minecraft;";
        String SQL1 = "CREATE TABLE IF NOT EXISTS "+prefix+"stories ( " + 
            "id INTEGER PRIMARY KEY, timestamp BIGINT NOT NULL, headline VARCHAR(95) NOT NULL,"+
                "body1 VARCHAR(95) NULL, body2 VARCHAR(95) NULL," +
                " body3 VARCHAR(95) NULL, body4 VARCHAR(95) NULL );";
        
        try {
            db_open();
            if (drops){ 
                plugin.getLogger().warning("Dropping all tables");
                stmt.executeUpdate(DROP1);
                
            } // could do an else: check existence here
            if (plugin.useMysql() ){ 
                stmt.executeUpdate(SQL0); 
                if(plugin.isSQLdebug()){plugin.getLogger().info("Made the DB (mysql)");}
            }
            if(plugin.isSQLdebug()){plugin.getLogger().info("Making stories table if doesn't exist. ");}
            stmt.executeUpdate(SQL1);
            
            rtn = true;
            
        } catch (SQLException ex){
            plugin.getLogger().severe( "createDB: "+ex.getMessage() );
            rtn = false;
        } finally {
            try { db_close();
            } catch (SQLException ex) {
                plugin.getLogger().warning( "createDB: "+ex.getMessage() );
                rtn = false;
            } finally{}
        }
        return rtn;
    }
}

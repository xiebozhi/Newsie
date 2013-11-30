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

import com.teamglokk.newsie.commands.AddNewsCommand;
import com.teamglokk.newsie.commands.NewsCommand;
import com.teamglokk.newsie.commands.ReadNewsCommand;
import com.teamglokk.newsie.listeners.SimpleNewsLoginEvent;
import java.util.TreeMap;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author bobbshields
 */
public class Newsie extends JavaPlugin{

    private double CONFIG_VERSION = .01;
    private boolean USE_METRICS;
    private int numStories = 1; 
    
    public TreeMap<Integer,NewsStory> stories = new TreeMap<Integer,NewsStory>();
    public TreeMap<String,NewsStory> editingStories = new TreeMap<String,NewsStory>();
    
    private static boolean DEBUG = true;
    private static boolean SQL_DEBUG = true;
    
    public dbWrapper dbwrapper;
    protected static boolean useMYSQL = false;
    private static String db_host = "jdbc:sqlite://localhost:3306/defaultdb";
    private static String db_database = "defaultdatabase";
    protected static String db_user = "defaultuser";
    protected static String db_pass = "defaultpass"; 
    protected static String db_prefix = "defaultpass"; 
    protected static String db_URL = null;
    public static String getDB_dbName() { return db_database;}
    public static String getDB_host() { return db_host;}
    public static String getDB_URL() { return db_URL;}
    public static String getDB_user() {return db_user;}
    public static String getDB_pass() { return db_pass;}
    public static String getDB_prefix() {return db_prefix; }
    
     /**
     * Shut down sequence
     */
    @Override
    public void onDisable() {
        getLogger().info("Shutting Down");
               
        // Save the news items
        this.saveConfig();
        this.dbwrapper.saveStories(stories.values());
        
        getLogger().info("Shut Down sequence complete");
    }

    /**
     * Start up sequence
     */
    @Override
    public void onEnable() {
        getLogger().info("Starting Up");
                
        // Load the configuration file
        this.saveDefaultConfig(); // saves plugins/Muni/config.yml if !exists
        loadConfigSettings(); // parses the settings and saves them into memory
        
        // Register Muni listener(s)
        getServer().getPluginManager().registerEvents(new SimpleNewsLoginEvent(this),this );
        
        // Register Newsie commands
        getCommand("news"    ).setExecutor(new NewsCommand    (this) );
        getCommand("readnews").setExecutor(new ReadNewsCommand    (this) );
        getCommand("addnews" ).setExecutor(new AddNewsCommand (this) );
        
        // Database wrapper initialization
        dbwrapper = new dbWrapper(this);
        
        if ( isDebug() ) { getLogger().info( "Dependancies Hooked"); }
        
        // 
        this.dbwrapper.createDB(false);
        loadStories();
        
        if ( isDebug() ) { getLogger().info( "Stories loaded from database"); }
        
        /*
        // Start Metrics if allowed by server owner
        if (USE_METRICS){
            if ( isDebug() ) {getLogger().info("Loading Metrics") ; }
            try {
                Metrics metrics = new Metrics(this);
                
                metrics.start();
                if ( isDebug() ) {getLogger().info("Metrics data has been sent") ; }
                
            } catch (IOException e) {
                // Failed to submit the stats :-(
                getLogger().warning("There was an error loading Metrics");
            }
        }
        */
        
        this.getLogger().info ("Loaded and Ready to serve the daily news" );
    } 
    
    public boolean loadStories() {
        int i = 1;
        for (NewsStory s : this.dbwrapper.getAllStories()) {
            stories.put(i, s); //Load the stories into memory
            stories.get(i).setNumber(i++); //Remap the story ids, in case something's been deleted
            
        }
        return true;
    }
    
    /**
     * Checks the player's permission
     * @param player
     * @param perm
     * @return 
     */
    public boolean hasPerm (Player player, String perm){
        if ( player.hasPermission(perm) ){
            return true;
        } else if ( player.isOp() ) {
            return true;
        }
        else { return false; }
    }
    
    /**
     * Deletes empty/null elements and trims the elements of a string array
     * @param split the array to be parsed
     * @return resized array of strings
     */
    public String [] trimSplit (String [] split ) {
        if (split.length == 0 ){
            return new String [0];
        } 
        String [] temp = new String[split.length];
        int i = 0;
        for (String entry: split) {
            if (entry.equalsIgnoreCase(" ") || entry.isEmpty() ){
                // do nothing (delete the empty space entries)
            } else {
                temp[i] = entry.trim();
                i++;
            }
        }
        String [] rtn = new String[i];
        int j = 0;
        for (j=0; j<i; j++){
            rtn[j] = temp[j];
        }
        return rtn;
    }
    
    /**
     * Loads the config settings from config.yml in plugins/muni/
     */
    protected void loadConfigSettings(){
        if (CONFIG_VERSION != this.getConfig().getDouble("config_version") ){
            getLogger().warning("Config version does not match software requirements.");
        }
        USE_METRICS = this.getConfig().getBoolean("use_metrics");
        
        DEBUG = this.getConfig().getBoolean("debug");
        SQL_DEBUG = this.getConfig().getBoolean("sql_debug");
        
        // Get database parameters
        useMYSQL = this.getConfig().getBoolean("database.use-mysql");
        db_host = this.getConfig().getString("database.host");
        db_database = this.getConfig().getString("database.database");
        db_user = this.getConfig().getString("database.user");
        db_pass = this.getConfig().getString("database.password");
        db_prefix = this.getConfig().getString("database.prefix");
        
        // Format the URL from the private variables
        db_URL = useMysql() ? "jdbc:mysql"+"://"+ db_host +":3306/"+db_database+
                "?user="+db_user+"&password="+db_pass 
                : "jdbc:sqlite:plugins/SimpleNews/"+db_database+".db";
                
        
   }
    
    public boolean saveStory (CommandSender sender) { 
        int id = stories.size() + 1;
        
        if ( !editingStories.containsKey(sender.getName() ) ) {
            return false;
        }
        
        NewsStory temp = editingStories.get(sender.getName() ) ;
        temp.setNumber(id);
        stories.put( id,  temp ) ;
        this.dbwrapper.saveStory(temp);
        clearEditing(sender);
        return true;
    }
    
    public boolean beginEditing (CommandSender sender){
        if ( !editingStories.containsKey( sender.getName() ) ){
            editingStories.put( sender.getName(), new NewsStory() );
            return true; 
        } else {
            sender.sendMessage("You already have something in your clipboard");
            return false; 
        }
    }
    public NewsStory getEditing (CommandSender sender){
        return editingStories.get( sender.getName() ) ;
    }
    public boolean deleteEditing (CommandSender sender ) {
        if (editingStories.containsKey(sender.getName() ) ){
            editingStories.remove( sender.getName() );
            return true; 
        } else { return false; }
    }
    public boolean isEditing(CommandSender sender) {
        if ( editingStories.containsKey(sender.getName() ) ) {
            return true;
        } else {return false; }
    }
    
    public boolean isEditingValid(CommandSender sender) {
        return getEditing(sender).isValid(sender);
    }
    
    public boolean editExisting(CommandSender sender, int storyID) {
        if ( stories.containsKey( storyID ) ) {
            if (!editingStories.containsKey(sender.getName() ) ) {
                editingStories.put(sender.getName(),stories.get(storyID) ) ;
                return true; 
            } else { 
                sender.sendMessage("You are already editing an article.  Do /addnews complete OR /addnews clear");
                return false; 
            }
        } else {
            sender.sendMessage("That article ID does not exist");
            return false; 
        }
    }
    public void clearEditing(CommandSender sender){
        if (editingStories.containsKey(sender.getName() ) ){
            editingStories.remove(sender.getName() );
        }
    }
       
    
    public boolean isDebug() { return DEBUG; }
   
   /**
    * Set the debug value about whether to output verbose to the log
    * @param value 
    */
   public void setDebug(boolean value){ 
       DEBUG = value; 
       this.getLogger().info("Debug changed to: " + String.valueOf(value) );
   }
   
    /**
     * Global config: Whether the plugin should output verbose debugging info to the log
     * @return 
     */
   public boolean isSQLdebug() { return SQL_DEBUG; }
   /**
    * Set the debug value about whether to output verbose to the log
    * @param value 
    */
   public void setSQLDebug(boolean value){ 
       SQL_DEBUG = value; 
       this.getLogger().info("Debug changed to: " + String.valueOf(value) );
   }
  
    /**
     * Global config: DBwrapper uses this to decide where to send DB queries
     * @return 
     */
    public boolean useMysql() { return useMYSQL; } 
    
    /**
     * Real Work 
     * @param player
     * @param msg
     * @param useConsole
     * @param color
     * @return 
     */
    public boolean out (CommandSender sender, String msg, ChatColor color){
        boolean console = false;
        if (!(sender instanceof Player)) {
            console = true;
        }
        if (console ){
            sender.sendMessage(msg);
            return true;
        } else { 
            Player player = (Player) sender;
            player.sendMessage(color+msg);
            return true;
        }
        
    }
    /**
     * Real Work 
     * @param player
     * @param msg
     * @param useConsole
     * @param color
     * @return 
     */
    public boolean out (CommandSender sender, String msg){
        return this.out(sender,msg,ChatColor.WHITE);
    }
}

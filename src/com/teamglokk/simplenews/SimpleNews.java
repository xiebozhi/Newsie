/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamglokk.simplenews;

import com.teamglokk.simplenews.commands.AddNewsCommand;
import com.teamglokk.simplenews.commands.NewsCommand;
import com.teamglokk.simplenews.listeners.SimpleNewsLoginEvent;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author bobbshields
 */
public class SimpleNews extends JavaPlugin{

    private double CONFIG_VERSION;
    private boolean USE_METRICS;
    private int numStories = 1; 
    public List<NewsStory> stories = new ArrayList<NewsStory>();
    
     /**
     * Shut down sequence
     */
    @Override
    public void onDisable() {
        getLogger().info("Shutting Down");
               
        // Save the news items
        this.saveConfig();
        
        getLogger().info("Shut Down sequence complete");
    }

    /**
     * Start up sequence
     */
    @Override
    public void onEnable() {
        getLogger().info("Starting Up");
                
        //Load the configuration file
        this.saveDefaultConfig(); // saves plugins/Muni/config.yml if !exists
        loadConfigSettings(); // parses the settings and loads into memory
        
        // Register Muni listener(s)
        getServer().getPluginManager().registerEvents(new SimpleNewsLoginEvent(this),this );

        
        // Register SimpleNews commands
        getCommand("news"  ).setExecutor(new NewsCommand    (this) );
        getCommand("addnews").setExecutor(new AddNewsCommand (this) );
        
        
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
        
        this.getLogger().info ("Loaded and Ready to server the daily news" );
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
        numStories = this.getConfig().getInt("total_stories");
        
        for ( int i = 1; i <= numStories; i++ ) {
            NewsStory temp = new NewsStory(i,this.getConfig().getString("stories."+i+".headline"), 
                    this.getConfig().getString("stories."+i+".story") );
            stories.add(i-1, temp );
        }
                
        getLogger().info("News items loaded");
        
   }
    
    public void addStory (String headline, String story) { 
        int len = stories.size() + 1;
        stories.add(new NewsStory(len,headline,story) ) ;
        this.getConfig().set("stories."+len+".headline", headline) ;
        this.getConfig().set("stories."+len+".story", story);
        this.saveConfig();
    }
       
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

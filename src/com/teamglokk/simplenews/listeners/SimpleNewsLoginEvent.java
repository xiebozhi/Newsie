/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamglokk.simplenews.listeners;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import com.teamglokk.simplenews.SimpleNews;
import org.bukkit.ChatColor;

/**
 * The custom Muni Login Event
 * @author bobbshields
 */
public class SimpleNewsLoginEvent implements Listener{
    SimpleNews plugin;
    
    public SimpleNewsLoginEvent (SimpleNews instance) {
        plugin = instance; 
    }
    /**
     * Displays relevant town info to players as they log in. 
     * @param event 
     */
    @EventHandler (priority = EventPriority.LOW)
    public void playerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.sendMessage(ChatColor.YELLOW+"Check the news! /news");
    }
}

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
package com.teamglokk.newsie.listeners;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import com.teamglokk.newsie.Newsie;
import org.bukkit.ChatColor;

/**
 * The custom Muni Login Event
 * @author bobbshields
 */
public class SimpleNewsLoginEvent implements Listener{
    Newsie plugin;
    
    public SimpleNewsLoginEvent (Newsie instance) {
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

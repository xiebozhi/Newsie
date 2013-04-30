/* 
 * Muni 
 * Copyright (C) 2013 bobbshields <https://github.com/xiebozhi/Muni> and contributors
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
 * Binary releases are available freely at <http://dev.bukkit.org/server-mods/muni/>.
*/
package com.teamglokk.simplenews.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.bukkit.ChatColor;
import com.teamglokk.simplenews.SimpleNews;
/**
 * Handler for the /town command.
 * @author BobbShields
 */
public class AddNewsCommand implements CommandExecutor {
    private SimpleNews plugin;
    private Player player;
    
    public AddNewsCommand (SimpleNews instance){
            plugin = instance;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        String [] args = plugin.trimSplit(split);
        
        player = (Player) sender;
        if (!plugin.hasPerm(player, "simplenews.reporter") ){
            player.sendMessage("You do not have permission to run /addnews");
            return true; 
        }

        if (args.length == 0){  
            displayHelp(sender);
            return true;
        } 
        
        plugin.addStory("testing", command.toString());
        return true; 
        
    }
    private void displayHelp(CommandSender sender){ 
        plugin.out(sender, "Help for /addnews:",ChatColor.LIGHT_PURPLE);
        plugin.out(sender, "Simply do /addnews Story to report, with no color support yet");
           
    }
}

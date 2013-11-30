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
package com.teamglokk.newsie.commands;

import com.teamglokk.newsie.NewsStory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.bukkit.ChatColor;
import com.teamglokk.newsie.Newsie;
/**
 * Handler for the /town command.
 * @author BobbShields
 */
public class ReadNewsCommand implements CommandExecutor {
    private Newsie plugin;
    private Player player;
    
    public ReadNewsCommand (Newsie instance){
        plugin = instance;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        String [] args = plugin.trimSplit(split);
        
        if ( sender instanceof Player ){
            player = (Player) sender;
            if ( !player.isOp() && !plugin.hasPerm(player, "simplenews.reader") ) {
                player.sendMessage("You do not have permission to run /ews");
                return true; 
            }
        }
        
        if (args.length==0) {
            sender.sendMessage("You must enter an article number to read.  Check /news");
            return true; 
        } else if (args.length < 1 ){
            sender.sendMessage("You entered too many parameters.  Usage: /readnews article#");
            return true; 
        }
        
        readnews(sender,args[0]); 
        return true;
    }
    private boolean readnews(CommandSender s, String a){
        int articleNumber = -1;
        try {
            articleNumber = Integer.parseInt(a.trim());
        } catch (NumberFormatException e){
            s.sendMessage("You did not enter a number for the article, please check /news then do /readnews #");
            return false;
        }
        
        if (plugin.stories.containsKey(articleNumber) ) {
            NewsStory temp = plugin.stories.get( articleNumber );
            s.sendMessage( temp.getHeadline());
            s.sendMessage( temp.getBody1() );
            s.sendMessage( temp.getBody2() );
            s.sendMessage( temp.getBody3() );
            s.sendMessage( temp.getBody4() );
            return true;
        } else {
            s.sendMessage("There is no article by that number ("+articleNumber+").");
            return false; 
        }
        
    }
    
}

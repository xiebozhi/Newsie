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
package com.teamglokk.simplenews.commands;

import com.teamglokk.simplenews.NewsStory;
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
public class NewsCommand implements CommandExecutor {
    private SimpleNews plugin;
    private Player player;
    
    public NewsCommand (SimpleNews instance){
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
        
        if (args.length == 0 ) { readHeadlines(sender,"1"); }
        else if (args.length ==1 ) { readHeadlines(sender,args[0]); }
        else { sender.sendMessage("You entered too many parameters.  Do /news (page# is optional)"); }
        
        return true;
    }
    
    public boolean readHeadlines(CommandSender s, String a){
        int requestedPage = 1;
        try {
            requestedPage = Integer.parseInt(a.trim());
        } catch (NumberFormatException e){
            s.sendMessage("You did not enter a valid number");
        }
        
        final int totalSize = plugin.stories.size();
        final int pageSize = 5;
        final int pages = (int) Math.ceil( totalSize / (float) pageSize );
        s.sendMessage("List of headlines (Page "+requestedPage+" of "+ pages +")" );
        
        s.sendMessage("List size is " +plugin.stories.size() ); //DELETE MEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        if ( plugin.stories.size() > 0 ) {
            for ( int i = 1; i <= 5; i++ ){
                s.sendMessage(i+": "+plugin.stories.get(i).getHeadline() );
            }
        } else{
            s.sendMessage(ChatColor.RED+"There are no news stories!");
        }
        
        return true; 
    }
    
}

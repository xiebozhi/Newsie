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
import java.util.ArrayList;
import java.util.List;
/**
 * Handler for the /town command.
 * @author BobbShields
 */
public class AddNewsCommand implements CommandExecutor {
    private Newsie plugin;
    private Player player;
    
    public AddNewsCommand (Newsie instance){
            plugin = instance;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        String [] args = plugin.trimSplit(split);
        
        if ( sender instanceof Player ){
            player = (Player) sender;
            if ( !player.isOp() && !plugin.hasPerm(player, "simplenews.reporter") ) {
                player.sendMessage("You do not have permission to run /addnews");
                return true; 
            }
        }

        if (args.length == 0){  
            displayHelp(sender);
            return true;
        }
        
        //Parse string array into one flowing string.
        String input;
        StringBuilder sb  = new StringBuilder();
        for (int i = 1; i< args.length; i++){
            sb.append(args[i]); sb.append(" ");
        }
        input = sb.toString();
        
        if (args[0].equalsIgnoreCase("edit") || args[0].equalsIgnoreCase("e") ) {
            int articleNumber = -1;
            try {
                articleNumber = Integer.parseInt(args[1].trim());
            } catch (NumberFormatException e){
                sender.sendMessage("You did not enter a valid number for the article to edit");
                return false;
            }
            plugin.editExisting(sender, articleNumber );
        } else if (args[0].equalsIgnoreCase("clear") || args[0].equalsIgnoreCase("c") ) {
            
            if (args[1].equalsIgnoreCase("confirm") ){
                plugin.clearEditing(sender);
                sender.sendMessage("Your clipboard has been cleared");
            } else{
                sender.sendMessage("To confirm clearing your clipboard, do /addnews clear confirm");
            }
            
        } else if (args[0].equalsIgnoreCase("save") || args[0].equalsIgnoreCase("s") ) {
            if (!plugin.isEditingValid(sender)) { return true; }
            
            if ( plugin.saveStory(sender) ){
                sender.sendMessage("You have saved the news story");
            } else {
                sender.sendMessage("You did not have anything on your clipboard");
            } 
        } else if (args[0].equalsIgnoreCase("headline") || args[0].equalsIgnoreCase("h") ) {
            if ( !plugin.isEditing(sender) ) {
                plugin.beginEditing(sender);
            }
            plugin.getEditing(sender).setHeadline(input);
            sender.sendMessage(ChatColor.GREEN+"Currently Editing:");
            plugin.getEditing(sender).displayStory(sender);
        } else if (args[0].equalsIgnoreCase("1") ) {
            if ( !plugin.isEditing(sender) ) {
                plugin.beginEditing(sender);
            }
            plugin.getEditing(sender).setBody1(input);
            sender.sendMessage(ChatColor.GREEN+"Currently Editing: (to finish, do /addnews save");
            plugin.getEditing(sender).displayStory(sender);
            
        } else if (args[0].equalsIgnoreCase("2") ) {
            if ( !plugin.isEditing(sender) ) {
                plugin.beginEditing(sender);
            }
            plugin.getEditing(sender).setBody2(input);
            sender.sendMessage(ChatColor.GREEN+"Currently Editing:");
            plugin.getEditing(sender).displayStory(sender);
            
        } else if (args[0].equalsIgnoreCase("3") ) {
            if ( !plugin.isEditing(sender) ) {
                plugin.beginEditing(sender);
            }
            plugin.getEditing(sender).setBody3(input);
            sender.sendMessage(ChatColor.GREEN+"Currently Editing:");
            plugin.getEditing(sender).displayStory(sender);
            
        } else if (args[0].equalsIgnoreCase("4") ) {
            if ( !plugin.isEditing(sender) ) {
                plugin.beginEditing(sender);
            }
            plugin.getEditing(sender).setBody4(input);
            sender.sendMessage(ChatColor.GREEN+"Currently Editing:");
            plugin.getEditing(sender).displayStory(sender);
            
        }          
        
        //plugin.addStory(headline,input);
        return true; 
        
    }
    
    
    private void displayHelp(CommandSender sender){ 
        plugin.out(sender, "Help for /addnews (/an):",ChatColor.LIGHT_PURPLE);
        plugin.out(sender, "Clear your clipboard with /addnews clear",ChatColor.LIGHT_PURPLE);
        plugin.out(sender, "Edit an existing article with /addnews edit #",ChatColor.LIGHT_PURPLE);
        plugin.out(sender, "There is currently no color support for articles in SimpleNews",ChatColor.RED);
        plugin.out(sender, "To start an article, do /addnews headline This is how you make your headline");
        plugin.out(sender, "With aliases: /an h You can do a shorter command to make a headline longer");
        plugin.out(sender, "You can add 4 lines of body to the article",ChatColor.YELLOW);
        plugin.out(sender, "You may edit a line by repeating the command",ChatColor.YELLOW);
        plugin.out(sender, "/addnews 1 This is your first line. You can do 2,3, and 4 instead of 1");
        plugin.out(sender, "Again, alias: /an 4 This is your 4th and final line");
        plugin.out(sender, "After you have entered all your info, do /addnews complete",ChatColor.YELLOW);
        plugin.out(sender, "To finish editing, do /addnews save",ChatColor.LIGHT_PURPLE);
           
    }
}

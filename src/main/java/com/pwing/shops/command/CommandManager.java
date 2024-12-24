package com.pwing.shops.command;

import com.pwing.shops.PwingShops;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.pwing.shops.command.NPCCommand;

public class CommandManager implements CommandExecutor, TabCompleter {
    private final Map<String, CommandExecutor> commands = new HashMap<>();
    private final Map<String, TabCompleter> tabCompleters = new HashMap<>();
    
    public CommandManager(PwingShops plugin) {
        registerCommand("convert", new ConvertCommand(plugin));
        registerCommand("reload", new ReloadCommand(plugin));
        registerCommand("npc", new NPCCommand(plugin));
    }
    
    private void registerCommand(String name, CommandExecutor executor) {
        commands.put(name.toLowerCase(), executor);
        if (executor instanceof TabCompleter) {
            tabCompleters.put(name.toLowerCase(), (TabCompleter) executor);
        }
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) return false;
        
        CommandExecutor executor = commands.get(args[0].toLowerCase());
        if (executor != null) {
            return executor.onCommand(sender, command, label, args);
        }
        return false;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length < 1) return null;
        
        TabCompleter completer = tabCompleters.get(args[0].toLowerCase());
        if (completer != null) {
            return completer.onTabComplete(sender, command, alias, args);
        }
        return null;
    }
}


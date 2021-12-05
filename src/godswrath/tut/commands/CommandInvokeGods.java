package godswrath.tut.commands;

import godswrath.tut.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandInvokeGods implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Main.invokeGodsAllPlayers();
        return true;
    }
}

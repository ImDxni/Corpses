package org.waraccademy.warcorpses.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.waraccademy.warcorpses.WarCorpses;
import org.waraccademy.warcorpses.inventory.log.CorpseLogProvider;

import static org.waraccademy.warcorpses.utils.Utils.color;

public class LogCommand implements CommandExecutor {
    private final YamlConfiguration config = WarCorpses.getInstance().getConfig();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        System.out.println("Comando eseguito");
        if(strings.length != 1)
            return true;
        System.out.println("Comando eseguito 2");

        if(!(sender instanceof Player))
            return true;

        System.out.println("Comando eseguito 3");
        Player p = (Player) sender;

        if(!p.hasPermission("warcorpses.log"))
            return true;

        System.out.println("Comando eseguito 4");

        String target = strings[0];

        WarCorpses.getInstance().getManager().getLogs(target).whenComplete((result,error) -> {
            if(error != null){
                error.printStackTrace();

                return;
            }
            System.out.println("Comando eseguito 5");

            if(result.isEmpty()){
                p.sendMessage(color(config.getString("messages.no-logs")));
                return;
            }

            System.out.println("Comando eseguito 6");
            Bukkit.getScheduler().runTask(WarCorpses.getInstance(),() -> CorpseLogProvider.getInventory(result).open(p));
        });

        return true;
    }
}

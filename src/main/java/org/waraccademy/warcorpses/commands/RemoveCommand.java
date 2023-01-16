package org.waraccademy.warcorpses.commands;

import net.citizensnpcs.api.CitizensAPI;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RemoveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(sender instanceof Player))
            return true;

        Player p = (Player) sender;

        if(!p.hasPermission("warcorpses.remove")){
            return true;
        }

        int range = 5;

        if(strings.length == 1 && NumberUtils.isNumber(strings[0])){
            range = Integer.parseInt(strings[0]);
        }

        p.getNearbyEntities(range,range,range).stream()
                .map(CitizensAPI.getNPCRegistry()::getNPC)
                .forEach(npc -> {
                    if(npc != null)
                        if(npc.data().has("inventory")){
                            npc.destroy();
                        }
                });

        return true;
    }
}

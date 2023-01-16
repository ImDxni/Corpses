package org.waraccademy.warcorpses.listener;

import fr.minuskube.inv.SmartInventory;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.waraccademy.warcorpses.WarCorpses;
import org.waraccademy.warcorpses.database.object.CorpseLog;
import org.waraccademy.warcorpses.inventory.CorpseHolder;
import org.waraccademy.warcorpses.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class DeathListener implements Listener {
    private final YamlConfiguration config = WarCorpses.getInstance().getConfig();

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        List<ItemStack> drops = new ArrayList<>(e.getDrops());

        e.getDrops().clear();

        if(e.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.SUFFOCATION)
            return;

        Player p = e.getEntity();

        NPC corpse = Utils.createCorpse(p);

        if(p.getKiller() != null){
            corpse.data().set("locked",true);
            corpse.data().set("killer",p.getKiller().getName());
        }

        CorpseLog log = new CorpseLog(p.getName(), new ArrayList<>());

        SmartInventory inventory = CorpseHolder.getInventory(drops,log);

        corpse.data().set("inventory",inventory);
        corpse.data().set("log",log);

        Bukkit.getScheduler().runTaskLater(WarCorpses.getInstance(), () -> corpse.data().remove("locked"),
                config.getInt("corpse.locked") * 20L);

        corpse.spawn(p.getLocation());
    }
}

package org.waraccademy.warcorpses.traits;

import fr.minuskube.inv.SmartInventory;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.EntityPose;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.waraccademy.warcorpses.WarCorpses;

import static org.waraccademy.warcorpses.utils.Utils.color;

@TraitName("CorpseTrait")
public class CorpseTrait extends Trait {
    private final YamlConfiguration config = WarCorpses.getInstance().getConfig();

    public CorpseTrait() {
        super("CorpseTrait");
     }


     @EventHandler
     public void onClick(NPCRightClickEvent e){
        if(e.getNPC().getId() == getNPC().getId()){
            NPC corpse = e.getNPC();
            Player p = e.getClicker();

            SmartInventory inventory = corpse.data().get("inventory");

            if(corpse.data().has("locked")){
                if(p.getName().equals(corpse.data().get("killer"))){
                    inventory.open(p);
                } else {
                    p.sendMessage(color(config.getString("messages.corpse-locked")));
                }
                return;
            }

            inventory.open(p);
        }
     }

    @Override
    public void onSpawn() {
        EntityPlayer entity = ((CraftPlayer)getNPC().getEntity()).getHandle();

        entity.setPose(EntityPose.SWIMMING);

        Bukkit.getScheduler().runTaskLater(WarCorpses.getInstance(), () -> {
            getNPC().despawn();
            CitizensAPI.getNPCRegistry().deregister(getNPC());
        },config.getInt("corpse.despawn") * 20L);

    }

    @Override
    public void onDespawn() {
        SmartInventory inventory = getNPC().data().get("inventory");
        inventory.getManager().getOpenedPlayers(inventory).forEach(Player::closeInventory);

        WarCorpses.getInstance().getManager().saveLog(getNPC().data().get("log"));
    }
}

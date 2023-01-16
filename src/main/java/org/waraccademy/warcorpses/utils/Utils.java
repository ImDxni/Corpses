package org.waraccademy.warcorpses.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.SkinTrait;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.waraccademy.warcorpses.traits.CorpseTrait;

public class Utils {

    public static String color(String s){
        return ChatColor.translateAlternateColorCodes('&',s);
    }
    public static NPC createCorpse(Player p){
        NPC corpse = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER,p.getName());
        corpse.addTrait(CorpseTrait.class);

        EntityPlayer player = ((CraftPlayer)p).getHandle();
        GameProfile profile = player.getProfile();
        Property property = null;

        for (Property textures : profile.getProperties().get("textures")) {
            property = textures;
        }

        SkinTrait trait = corpse.getOrAddTrait(SkinTrait.class);
        trait.setSkinPersistent(p.getName(),property.getSignature(),property.getValue());
        return corpse;
    }

}

package org.waraccademy.warcorpses.database.object;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.v1_16_R3.MojangsonParser;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bson.Document;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.util.Date;

public class ItemLog implements Log{
    private String target;
    private ItemStack item;
    private Date time = new Date();

    public ItemLog() {
    }

    public ItemLog(String target, ItemStack item) {
        this.target = target;
        this.item = item;
    }

    public Document getDocument(){
        Document document = new Document();
        document.put("target",target);
        document.put("time",time);

        net.minecraft.server.v1_16_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);

        NBTTagCompound compound = new NBTTagCompound();
        nmsItem.save(compound);

        document.put("item",compound.toString());

        return document;
    }

    public static ItemLog of(Document document){
        ItemLog log = new ItemLog();
        log.setTime(document.getDate("time"));
        log.setTarget(document.getString("target"));
        try {
            NBTTagCompound compound = MojangsonParser.parse(document.getString("item"));
            log.setItem(CraftItemStack.asBukkitCopy(net.minecraft.server.v1_16_R3.ItemStack.a(compound)));
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }

        return log;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}

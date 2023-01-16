package org.waraccademy.warcorpses.inventory.log;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.waraccademy.warcorpses.WarCorpses;
import org.waraccademy.warcorpses.database.object.ItemLog;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.waraccademy.warcorpses.utils.Utils.color;

public class ItemLogProvider implements InventoryProvider {
    private final List<ItemLog> items;

    private final YamlConfiguration config = WarCorpses.getInstance().getConfig();

    public ItemLogProvider(List<ItemLog> items) {
        this.items = items;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        SimpleDateFormat format = new SimpleDateFormat("YYYY/MM/dd HH:mm");
        items.stream()
                .map(log -> {
                    ItemStack item = log.getItem();
                    ItemMeta meta = item.getItemMeta();
                    List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();

                    for(String s : config.getStringList("inventory.itemlog.lore")){
                        lore.add(color(s.replace("%target%",log.getTarget())
                                .replace("%date%",format.format(log.getTime()))));
                    }

                    meta.setLore(lore);
                    item.setItemMeta(meta);

                    return ClickableItem.empty(item);
                }).forEach(contents::add);
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    public static SmartInventory getInventory(List<ItemLog> items){
        return SmartInventory.builder()
                .size(5,9)
                .provider(new ItemLogProvider(items))
                .title("Logs")
                .build();
    }
}

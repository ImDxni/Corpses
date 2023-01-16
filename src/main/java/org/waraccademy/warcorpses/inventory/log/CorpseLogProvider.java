package org.waraccademy.warcorpses.inventory.log;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.waraccademy.warcorpses.WarCorpses;
import org.waraccademy.warcorpses.database.object.CorpseLog;

import java.text.SimpleDateFormat;
import java.util.List;

import static org.waraccademy.warcorpses.utils.Utils.color;

public class CorpseLogProvider implements InventoryProvider {

    private final List<CorpseLog> logs;

    private final YamlConfiguration config = WarCorpses.getInstance().getConfig();

    public CorpseLogProvider(List<CorpseLog> logs) {
        this.logs = logs;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination();
        SimpleDateFormat format = new SimpleDateFormat("YYYY/MM/dd HH:mm");
        ClickableItem[] items = new ClickableItem[logs.size()];

        for(int i = 0; i < items.length; i++) {
            ItemStack item = new ItemStack(Material.CHEST);
            ItemMeta meta = item.getItemMeta();
            CorpseLog log = logs.get(i);
            meta.setDisplayName(color(config.getString("inventory.corpselog.chest")
                    .replace("%player%",log.getPlayer())
                    .replace("%date%",format.format(log.getDate()))));

            item.setItemMeta(meta);

            items[i] = ClickableItem.of(item, e -> ItemLogProvider.getInventory(log.getItems()).open((Player) e.getWhoClicked()));
        }

        pagination.setItems(items);
        pagination.setItemsPerPage(7);

        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1));

        contents.set(2, 3, ClickableItem.of(new ItemStack(Material.ARROW),
                e -> getInventory(logs).open(player, pagination.previous().getPage())));
        contents.set(2, 5, ClickableItem.of(new ItemStack(Material.ARROW),
                e -> getInventory(logs).open(player, pagination.next().getPage())));

    }

    @Override
    public void update(Player player, InventoryContents contents) {}

    public static SmartInventory getInventory(List<CorpseLog> log){
        return SmartInventory.builder()
                .size(3,9)
                .title("Logs")
                .provider(new CorpseLogProvider(log))
                .build();
    }
}

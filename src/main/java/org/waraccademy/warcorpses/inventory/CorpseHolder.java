package org.waraccademy.warcorpses.inventory;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.SmartInvsPlugin;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.waraccademy.warcorpses.database.object.CorpseLog;
import org.waraccademy.warcorpses.database.object.ItemLog;

import java.util.List;
import java.util.Optional;

public class CorpseHolder implements InventoryProvider {
    private final List<ItemStack> items;
    private final CorpseLog log;

    public CorpseHolder(List<ItemStack> items, CorpseLog log) {
        this.items = items;
        this.log = log;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        items.stream()
                .map(item -> ClickableItem.of(item,e -> {
                    e.getWhoClicked().getInventory().addItem(item);

                    e.getClickedInventory().setItem(e.getSlot(),null);

                    items.remove(item);

                    Optional<SmartInventory> smartInventory = SmartInvsPlugin.manager().getInventory(player);

                    smartInventory.ifPresent(inventory -> SmartInvsPlugin.manager().getOpenedPlayers(inventory)
                            .forEach(p -> p.getOpenInventory().getTopInventory().setItem(e.getSlot(),null)));

                    log.addItemLog(new ItemLog(e.getWhoClicked().getName(),item));
                }))
                .forEach(contents::add);
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    public static SmartInventory getInventory(List<ItemStack> items, CorpseLog log){
        return SmartInventory.builder()
                .title("Cadavere")
                .size(5,9)
                .provider(new CorpseHolder(items,log))
                .build();
    }

}

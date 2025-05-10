package endcrypt.equinox.menu.horse.submenus;

import de.tr7zw.changeme.nbtapi.NBT;
import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.menu.horse.EquineHorseInventory;
import org.bukkit.Material;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class InventoryHorseMenu implements Listener {
    private final EquinoxEquestrian plugin;
    private final Map<AbstractHorse, Player> editingHorseInventories = new HashMap<>();

    public InventoryHorseMenu(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public Inventory inventoryMenu(Player player, AbstractHorse abstractHorse) {
        return new EquineHorseInventory(abstractHorse).getInventory();
    }

    private ItemStack loadSlot(int slot, AbstractHorse abstractHorse) {
        ItemStack item = NBT.getPersistentData(abstractHorse, nbt -> nbt.getItemStack("EQUINE_INVENTORY_SLOT_" + slot));
        if(item != null) {
            return item;
        } else {

            return new ItemStack(Material.AIR);
        }
    }

    private void saveSlot(int slot, ItemStack item, AbstractHorse abstractHorse) {
        NBT.modifyPersistentData(abstractHorse, nbt -> {
            if (item == null || item.getType() == Material.AIR) {
                nbt.removeKey("EQUINE_INVENTORY_SLOT_" + slot);
            } else {
                nbt.setItemStack("EQUINE_INVENTORY_SLOT_" + slot, item);
            }
        });
    }

    // Check if a horse's inventory is currently being edited
    public boolean isHorseInventoryOpened(AbstractHorse horse) {
        return editingHorseInventories.containsKey(horse);
    }

    @EventHandler
    public void onHorseInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory().getHolder() instanceof EquineHorseInventory equineHorseInventory) {
            AbstractHorse horse = equineHorseInventory.getHorse();
            Player player = (Player) event.getPlayer();
            editingHorseInventories.put(horse, player);
            IntStream.range(0, 25).forEach(i -> event.getInventory().setItem(i, loadSlot(i, horse)));
        }
    }

    @EventHandler
    public void onHorseInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof EquineHorseInventory equineHorseInventory) {
            AbstractHorse horse = equineHorseInventory.getHorse();
            IntStream.range(0, 25).forEach(i -> saveSlot(i, event.getInventory().getItem(i), horse));
            editingHorseInventories.remove(horse);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked().getOpenInventory().getTopInventory().getHolder() instanceof EquineHorseInventory equineHorseInventory))
            return;

        AbstractHorse horse = equineHorseInventory.getHorse();
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();
        ItemStack cursorItem = event.getCursor();
        Inventory clickedInventory = event.getClickedInventory();
        Inventory topInventory = player.getOpenInventory().getTopInventory();
        Inventory bottomInventory = player.getOpenInventory().getBottomInventory();

        boolean isShiftClick = event.getClick().isShiftClick();
        boolean isBottomInventory = clickedInventory == bottomInventory;
        boolean isTopInventory = clickedInventory == topInventory;

        // Shift-click handling
        if (isShiftClick) {
            if (isBottomInventory && hasEmptySlot(topInventory) && isEquippable(clickedItem)) {
                equipItem(horse, clickedItem);
            } else if (isTopInventory && hasEmptySlot(bottomInventory) && isEquippable(clickedItem)) {
                unequipItem(horse, clickedItem);
            }
            return;
        }

        // Normal click handling
        if (isTopInventory) {

            if(isHorseArmor(cursorItem) && isHorseArmor(clickedItem)) {
                unequipItem(horse, clickedItem);
                equipItem(horse, cursorItem);
                return;
            }


            if (isEquippable(cursorItem)) {
                equipItem(horse, cursorItem);
            }
            if (isEquippable(clickedItem)) {
                unequipItem(horse, clickedItem);
            }
        }
    }

    private void equipItem(AbstractHorse horse, ItemStack item) {
        if (isHorseArmor(item) && horse.getInventory().getItem(1) == null) {
            horse.getInventory().setItem(1, item);
            NBT.modify(item, nbt -> {
                nbt.setString("NBT_EQUIPPED", "true");
            });
        } else if (!isHorseArmor(item)) {
            horse.getInventory().setSaddle(item);
            NBT.modify(item, nbt -> {
                nbt.setString("NBT_EQUIPPED", "true");
            });
        }
    }

    private void unequipItem(AbstractHorse horse, ItemStack item) {
        String equippedTag = NBT.get(item, nbt -> (String) nbt.getString("NBT_EQUIPPED"));

        if ("true".equalsIgnoreCase(equippedTag)) {
            NBT.modify(item, nbt -> {
                nbt.setString("NBT_EQUIPPED", "false");
            });

            if (isHorseArmor(item)) {
                horse.getInventory().setItem(1, null);
            } else {
                horse.getInventory().setSaddle(null);
            }
        }
    }


    private boolean isHorseArmor(ItemStack item) {
        return item != null && switch (item.getType()) {
            case DIAMOND_HORSE_ARMOR, IRON_HORSE_ARMOR, GOLDEN_HORSE_ARMOR, LEATHER_HORSE_ARMOR -> true;
            default -> false;
        };
    }

    private boolean isEquippable(ItemStack item) {
        if (item != null) {
            return item.getType() == Material.SADDLE || isHorseArmor(item);
        }

        return false;
    }


    private boolean hasEmptySlot(Inventory inventory) {
        for (ItemStack item : inventory.getContents()) {
            if (item == null || item.getType() == Material.AIR) {
                return true;
            }
        }
        return false;
    }
}

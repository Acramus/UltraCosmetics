package be.isach.ultracosmetics.manager;

import be.isach.ultracosmetics.CustomPlayer;
import be.isach.ultracosmetics.config.SettingsManager;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by Sacha on 16/12/15.
 */
public class PlayerManager {

    private Map<UUID, CustomPlayer> playerCache;

    public PlayerManager() {
        this.playerCache = new HashMap<>();
    }

    public CustomPlayer getCustomPlayer(Player player) {
        CustomPlayer p = playerCache.get(player.getUniqueId());
        if (p == null)
            return create(player);
        return p;
    }

    public CustomPlayer create(Player player) {
        CustomPlayer customPlayer = new CustomPlayer(player.getUniqueId());
        playerCache.put(player.getUniqueId(), customPlayer);
        return customPlayer;
    }

    public boolean remove(Player player) {
        return playerCache.remove(player.getUniqueId()) != null;
    }

    public void clearPlayers() {
        Iterator<CustomPlayer> i = playerCache.values().iterator();
        while (i.hasNext())
            i.next().clear();
    }

    public Collection<CustomPlayer> getPlayers() {
        return playerCache.values();
    }

    public void dispose() {
        Collection<CustomPlayer> set = playerCache.values();
        for (CustomPlayer cp : set) {
            if (cp.currentTreasureChest != null)
                cp.currentTreasureChest.forceOpen(0);
            cp.clear();
            int slot = SettingsManager.getConfig().getInt("Menu-Item.Slot");
            if (cp.getPlayer().getInventory().getItem(slot) != null
                    && cp.getPlayer().getInventory().getItem(slot).hasItemMeta()
                    && cp.getPlayer().getInventory().getItem(slot).getItemMeta().hasDisplayName()
                    && cp.getPlayer().getInventory().getItem(slot).getItemMeta().getDisplayName()
                    .equals(String.valueOf(SettingsManager.getConfig().get("Menu-Item.Displayname")).replace("&", "§")))
                cp.getPlayer().getInventory().setItem(slot, null);
        }

        playerCache.clear();
        playerCache = null;
    }

}

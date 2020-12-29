package ru.ruscalworld.universalskins.skins.updaters;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ru.ruscalworld.universalskins.UniversalSkins;

public abstract class SkinUpdater {

    private final UniversalSkins plugin;

    public SkinUpdater(UniversalSkins plugin) {
        this.plugin = plugin;
    }

    public abstract void updateSkin(Player player);

    public void updateData(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {player.updateInventory();
                player.setExp(player.getExp());
                player.setLevel(player.getLevel());
                player.setHealth(player.getHealth());
                player.setFlying(player.isFlying());
                player.setPlayerListName(player.getPlayerListName());
            }
        }.runTask(plugin);
    }
}

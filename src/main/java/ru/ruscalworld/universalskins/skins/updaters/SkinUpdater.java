package ru.ruscalworld.universalskins.skins.updaters;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ru.ruscalworld.universalskins.UniversalSkins;

/**
 * Абстрактный апдейтер скина
 * Используются подобне алгоритмы для того, чтобы игрок сам мог увидеть свой новый скин без перезахода на сервер
 */
public abstract class SkinUpdater {

    private final UniversalSkins plugin;

    public SkinUpdater(UniversalSkins plugin) {
        this.plugin = plugin;
    }

    public abstract void updateSkin(Player player);

    /**
     * Просто даём клиенту знать о всём самом главном после головокружительных махинаций с пакетами
     * @param player Игрок, которого мы хотим "восстановить"
     */
    public void updateData(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                player.updateInventory();
                player.setExp(player.getExp());
                player.setLevel(player.getLevel());
                player.setHealth(player.getHealth());
                player.setFlying(player.isFlying());
                player.setPlayerListName(player.getPlayerListName());
            }
        }.runTask(plugin);
    }
}

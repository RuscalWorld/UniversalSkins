package ru.ruscalworld.universalskins.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.ruscalworld.universalskins.UniversalSkins;
import ru.ruscalworld.universalskins.skins.storage.Skin;

public class JoinListener implements Listener {

    private final UniversalSkins plugin;

    public JoinListener(UniversalSkins plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        try {
            Player player = event.getPlayer();
            Skin skin = Skin.get(player.getName(), plugin);
            skin.update();
        } catch (Exception ignored) { }
    }
}

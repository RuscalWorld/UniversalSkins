package ru.ruscalworld.universalskins.skins.storage;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import ru.ruscalworld.universalskins.FileManager;
import ru.ruscalworld.universalskins.UniversalSkins;
import ru.ruscalworld.universalskins.systems.SkinSystem;
import ru.ruscalworld.universalskins.systems.mojang.MojangSkinSystem;
import ru.ruscalworld.universalskins.util.ResultHandler;

import javax.annotation.Nullable;
import java.util.Objects;

public class Skin {
    private final UniversalSkins plugin;
    private final String player;
    private SkinSystem system;
    private String name;

    public Skin(UniversalSkins plugin, String player, SkinSystem system, String name) {
        this.plugin = plugin;
        this.player = player;
        this.system = system;
        this.name = name;
    }

    public static Skin get(String player, UniversalSkins plugin) {
        FileManager fileManager = plugin.getFileManager();
        YamlConfiguration data = fileManager.getData();

        ConfigurationSection section = data.getConfigurationSection("skins." + player);
        if (section == null) return new Skin(plugin, player, new MojangSkinSystem(plugin), player);

        String name = Objects.requireNonNull(section.getString("name"));
        SkinSystem system = SkinSystem.fromName(Objects.requireNonNull(section.getString("system")), plugin);

        return new Skin(plugin, player, system, name);
    }

    public void reset() {
        reset(true);
    }

    public void reset(boolean update) {
        try {
            setSkin(new MojangSkinSystem(plugin), player, update);
        } catch (Exception ignored) { }
    }

    public void setSkin(SkinSystem system, String name) {
        setSkin(system, name, true);
    }

    public void setSkin(SkinSystem system, String name, boolean update) {
        this.name = name;
        this.system = system;

        if (update) this.update(new ResultHandler<Boolean>() {
            @Override
            public void handle(Boolean result) {
                if (!result) return;
                FileManager fileManager = plugin.getFileManager();
                YamlConfiguration data = fileManager.getData();

                data.set("skins." + player + ".name", name);
                data.set("skins." + player + ".system", system.getName());
                fileManager.saveData();
            }
        });
    }

    public void update() {
        update(null);
    }

    public void update(@Nullable ResultHandler<Boolean> callback) {
        Player player = Bukkit.getPlayer(this.player);
        if (player == null) return;
        SkinSystem.setSkinForPlayer(player, name, plugin, system, new ResultHandler<Boolean>() {
            @Override
            public void handle(Boolean result) {
                if (callback != null) callback.handle(result);
            }
        });
    }

    public SkinSystem getSystem() {
        return system;
    }

    public String getName() {
        return name;
    }

    public String getPlayer() {
        return player;
    }

    public String getHeadUrl() {
        return getSystem().getHeadUrl(this.name);
    }

    public String getSkinUrl() {
        return getSystem().getSkinUrl(this.name);
    }
}

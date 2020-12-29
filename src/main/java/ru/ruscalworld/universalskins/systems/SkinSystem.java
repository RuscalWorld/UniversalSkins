package ru.ruscalworld.universalskins.systems;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ru.ruscalworld.universalskins.UniversalSkins;
import ru.ruscalworld.universalskins.skins.Textures;
import ru.ruscalworld.universalskins.skins.updaters.PaperSkinUpdater;
import ru.ruscalworld.universalskins.systems.elyby.ElybySkinSystem;
import ru.ruscalworld.universalskins.systems.mojang.MojangSkinSystem;
import ru.ruscalworld.universalskins.util.ResultHandler;

import java.util.UUID;

public abstract class SkinSystem {

    public abstract void getTextures(String username, ResultHandler<Textures> callback);
    public abstract void getTextures(UUID uuid, ResultHandler<Textures> callback);
    public abstract String getSkinUrl(String name);
    public abstract String getHeadUrl(String name);

    public static void setSkin(GameProfile profile, Textures textures) {
        try {
            if (profile.getProperties().containsKey("textures")) profile.getProperties().removeAll("textures");
            profile.getProperties().put("textures", new Property("textures", textures.getValue(), textures.getSignature()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SkinSystem fromName(String name, UniversalSkins plugin) {
        switch (name.toLowerCase()) {
            case "mojang":
                return new MojangSkinSystem(plugin);
            case "elyby":
                return new ElybySkinSystem(plugin);
        }

        return null;
    }

    public abstract String getName();

    public void setTexturesForPlayer(Player player, Textures textures, UniversalSkins plugin) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        if (textures == null) return;
        GameProfile profile = entityPlayer.getProfile();
        setSkin(profile, textures);

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.hidePlayer(plugin, player);
                    p.showPlayer(plugin, player);
                }
            }
        };
        plugin.runInMainThread(runnable);

        new PaperSkinUpdater(plugin).updateSkin(player);
    }

    public static void setSkinForPlayer(Player player, String skinName, UniversalSkins plugin,
                                        SkinSystem skinSystem, ResultHandler<Boolean> callback) {
        skinSystem.getTextures(skinName, new ResultHandler<Textures>() {
            @Override
            public void handle(Textures result) {
                if (result != null) {
                    skinSystem.setTexturesForPlayer(player, result, plugin);
                    callback.handle(true);
                } else callback.handle(false);
            }
        });
    }

    public static void setSkinForPlayer(Player player, UUID uuid, UniversalSkins plugin,
                                        SkinSystem skinSystem, ResultHandler<Boolean> callback) {
        skinSystem.getTextures(uuid, new ResultHandler<Textures>() {
            @Override
            public void handle(Textures result) {
                if (result != null) {
                    skinSystem.setTexturesForPlayer(player, result, plugin);
                    callback.handle(true);
                } else callback.handle(false);
            }
        });
    }
}

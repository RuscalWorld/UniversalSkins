package ru.ruscalworld.universalskins.skins.updaters;

import net.minecraft.server.v1_16_R3.*;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ru.armagidon.poseplugin.api.PosePluginAPI;
import ru.armagidon.poseplugin.api.player.P3Map;
import ru.armagidon.poseplugin.api.player.PosePluginPlayer;
import ru.armagidon.poseplugin.api.poses.EnumPose;
import ru.armagidon.poseplugin.api.poses.PoseBuilder;
import ru.ruscalworld.universalskins.UniversalSkins;

import java.util.Collections;

public class PaperSkinUpdater extends SkinUpdater {

    private final UniversalSkins plugin;

    public PaperSkinUpdater(UniversalSkins plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public void updateSkin(Player player) {
        CraftPlayer cp = (CraftPlayer) player;
        EntityPlayer ep = cp.getHandle();

        PacketPlayOutPlayerInfo removeInfo = new PacketPlayOutPlayerInfo(
                PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ep);
        PacketPlayOutPlayerInfo addInfo = new PacketPlayOutPlayerInfo(
                PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ep);

        WorldServer worldServer = ep.getWorldServer();

        PacketPlayOutRespawn respawn = new PacketPlayOutRespawn(worldServer.getDimensionManager(), worldServer.getDimensionKey(),
                worldServer.getSeed(), ep.playerInteractManager.getGameMode(), ep.playerInteractManager.getGameMode(),
                worldServer.isDebugWorld(), worldServer.isFlatWorld(), true);

        PacketPlayOutPosition position = new PacketPlayOutPosition(
                player.getLocation().getX(),
                player.getLocation().getY(),
                player.getLocation().getZ(),
                player.getLocation().getYaw(),
                player.getLocation().getPitch(),
                Collections.emptySet(),
                0
        );
        PacketPlayOutHeldItemSlot slot = new PacketPlayOutHeldItemSlot(player.getInventory().getHeldItemSlot());

        DataWatcher watcher = ep.getDataWatcher();
        watcher.set(DataWatcherRegistry.a.a(16), (byte) 127);

        PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(ep.getId(), watcher, false);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.getVehicle() != null) player.getVehicle().eject();

                try {
                    PosePluginAPI api = PosePluginAPI.getAPI();
                    P3Map playerMap = api.getPlayerMap();
                    PosePluginPlayer ppPlayer = playerMap.getPosePluginPlayer(ep.getName());
                    ppPlayer.changePose(PoseBuilder.builder(EnumPose.STANDING).build(player));
                } catch (Exception ignored) { }

                ep.playerConnection.sendPacket(removeInfo);
                ep.playerConnection.sendPacket(addInfo);
                ep.playerConnection.sendPacket(metadata);
                ep.playerConnection.sendPacket(respawn);
                ep.playerConnection.sendPacket(position);
                ep.playerConnection.sendPacket(slot);

                ep.updateAbilities();
                updateData(player);
            }
        }.runTask(plugin);
    }
}

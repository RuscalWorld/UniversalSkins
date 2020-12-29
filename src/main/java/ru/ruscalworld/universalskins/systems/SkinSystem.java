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

/**
 * Абстрактная система скинов
 * Описывает, что каждая из "систем скинов" должна уметь делать
 */
public abstract class SkinSystem {
    /**
     * Метод для получения текстур по никнейму
     * @param username Никнейм игрока
     * @param callback Куда передавать текстуры
     */
    public abstract void getTextures(String username, ResultHandler<Textures> callback);

    /**
     * Метод для получения текстур по UUID
     * @param uuid UUID игрока
     * @param callback Куда передавать текстуры
     */
    public abstract void getTextures(UUID uuid, ResultHandler<Textures> callback);

    /**
     * Метод для получения скина в виде картинки
     * @param name Никнейм игрока
     * @return Адрес скина-картинки
     */
    public abstract String getSkinUrl(String name);

    /**
     * Метод для получения изображения головы
     * @param name Никнейм игрока
     * @return Адрес изображения с головой скина
     */
    public abstract String getHeadUrl(String name);

    /**
     * Метод для установки скина в GameProfile
     * @param profile GameProfile, который необходимо модифицировать
     * @param textures Текстуры, которые необходимо поместить в GameProfile
     */
    public static void setSkin(GameProfile profile, Textures textures) {
        try {
            if (profile.getProperties().containsKey("textures")) profile.getProperties().removeAll("textures");
            profile.getProperties().put("textures", new Property("textures", textures.getValue(), textures.getSignature()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Получает экземпляр системы скинов по её "названию"
     * @param name Название системы скинов
     * @param plugin Экземпляр плагина
     * @return Экземпляр системы скинов
     */
    public static SkinSystem fromName(String name, UniversalSkins plugin) {
        switch (name.toLowerCase()) {
            case "mojang":
                return new MojangSkinSystem(plugin);
            case "elyby":
                return new ElybySkinSystem(plugin);
        }

        return null;
    }

    /**
     * @return Название системы скинов
     */
    public abstract String getName();

    /**
     * Устанавливает текстуры игроку
     * @param player Игрок, которому надо установить текстуры
     * @param textures Сами текстуры с подписью
     * @param plugin Экземпляр главного класса
     */
    public void setTexturesForPlayer(Player player, Textures textures, UniversalSkins plugin) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        if (textures == null) return;

        // Получаем профиль и вставляем туда наши текстуры с помощью уже готового метода
        GameProfile profile = entityPlayer.getProfile();
        setSkin(profile, textures);

        // Скрываем игрока для всех, а потом отображаем
        // Этот фокус нужен для того, чтобы у других игроков был виден новый скин игрока, которому мы только что изменили текстуры
        // Всё это должно запускаться в основном потоке из-за ограничений Spigot API, поэтому, сократить до четырёх строк нельзя
        // (надо помнить, что этот метод вызывается внутри ResultHandler, который вызывается в отдельном потоке после выполнения запроса к системе скинов)
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

        // Делаем так, чтобы сам игрок увидел свой новый скин без перезахода
        new PaperSkinUpdater(plugin).updateSkin(player);
    }

    /**
     * Метод, который обобщает всё и позволяет "просто изменить скин"
     * @param player Игрок, которому надо поменять скин
     * @param skinName Название скина (никнейм игрока, у которого надо взять скин)
     * @param plugin Экземпляр плагина
     * @param skinSystem Экземпляр системы скинов
     * @param callback Куда вернуть результат: текстуры найдены (true) или нет (false)
     */
    public static void setSkinForPlayer(Player player, String skinName, UniversalSkins plugin,
                                        SkinSystem skinSystem, ResultHandler<Boolean> callback) {
        // Получаем текстуры и передаём их в обработчик результата
        skinSystem.getTextures(skinName, new ResultHandler<Textures>() {
            @Override
            public void handle(Textures result) {
                if (result != null) {
                    // Текстуры найдены, устанавливаем их игроку
                    skinSystem.setTexturesForPlayer(player, result, plugin);
                    callback.handle(true);
                } else callback.handle(false);
            }
        });
    }

    // То же самое, что и метод выше, только ставит скин по UUID
    // Уже не помню, зачем было нужно, да и не используется он
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

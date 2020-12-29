package ru.ruscalworld.universalskins;

import com.mojang.brigadier.tree.LiteralCommandNode;
import me.lucko.commodore.Commodore;
import me.lucko.commodore.CommodoreProvider;
import me.lucko.commodore.file.CommodoreFileFormat;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import ru.ruscalworld.universalskins.commands.SkinCommand;
import ru.ruscalworld.universalskins.commands.tabcompleters.SkinTabCompleter;
import ru.ruscalworld.universalskins.listeners.JoinListener;

import java.io.InputStream;
import java.util.Objects;

public class UniversalSkins extends JavaPlugin {

    private FileManager fileManager;

    @Override
    public void onEnable() {
        try {
            PluginCommand skinCommand = Objects.requireNonNull(getCommand("skin"));
            skinCommand.setExecutor(new SkinCommand(this));

            // Подсказки для команды /skin, которые нельзя реализовать с помощью Commodore
            skinCommand.setTabCompleter(new SkinTabCompleter());

            // Подсказки для команды /skin, которые можно реализовать с помощью Commodore
            // Просто чтобы было красиво
            if (CommodoreProvider.isSupported()) {
                Commodore commodore = CommodoreProvider.getCommodore(this);
                InputStream commandFile = getResource("skin.commodore");
                if (commandFile != null) {
                    LiteralCommandNode<Object> node = CommodoreFileFormat.parse(commandFile);
                    commodore.register(skinCommand, node);
                }
            }

            // Инициализируем класс, отвечающий за управление файлами
            fileManager = new FileManager(this).init();

            PluginManager pluginManager = Bukkit.getPluginManager();
            pluginManager.registerEvents(new JoinListener(this), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FileManager getFileManager() {
        return this.fileManager;
    }

    public void runInMainThread(BukkitRunnable runnable) {
        runnable.runTask(this);
    }
}

package ru.ruscalworld.universalskins;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileManager {

    private File d = null;
    private final YamlConfiguration data = new YamlConfiguration();

    private final UniversalSkins plugin;

    public FileManager(UniversalSkins plugin) {
        this.plugin = plugin;
    }

    public FileManager init() {
        d = new File(plugin.getDataFolder(), "data.yml");

        mkdir();
        loadYamls();

        return this;
    }

    private void mkdir() {
        if (!d.exists()) plugin.saveResource("data.yml", false);
    }

    private void loadYamls() {
        try {
            data.load(d);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public YamlConfiguration getData() {
        return data;
    }

    public void saveData() {
        try {
            data.save(d);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

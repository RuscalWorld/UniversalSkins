package ru.ruscalworld.universalskins;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Класс для управления файлами (файлом) плагина
 */
public class FileManager {

    private File d = null;
    private final YamlConfiguration data = new YamlConfiguration();

    private final UniversalSkins plugin;

    public FileManager(UniversalSkins plugin) {
        this.plugin = plugin;
    }

    public FileManager init() {
        // Определяем файл с настройками игроков
        d = new File(plugin.getDataFolder(), "data.yml");

        mkdir();
        loadYamls();

        return this;
    }

    private void mkdir() {
        // Создаём файл, если его нет
        if (!d.exists()) plugin.saveResource("data.yml", false);
    }

    private void loadYamls() {
        try {
            // Загружаем данные из файла
            data.load(d);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return Сохранённые настройки игроков в YAML
     */
    public YamlConfiguration getData() {
        return data;
    }

    /**
     * Сохраняет всё в файл
     */
    public void saveData() {
        try {
            data.save(d);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package ru.ruscalworld.universalskins.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;
import ru.ruscalworld.universalskins.UniversalSkins;
import ru.ruscalworld.universalskins.skins.storage.Skin;
import ru.ruscalworld.universalskins.systems.SkinSystem;
import ru.ruscalworld.universalskins.systems.mojang.MojangSkinSystem;
import ru.ruscalworld.universalskins.util.ResultHandler;

public class SkinCommand implements CommandExecutor {

    private final UniversalSkins plugin;

    public SkinCommand(UniversalSkins plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String alias, String[] args) {
        // Просто ненужная проверка, ничего необычного
        if (command.getLabel().equals("skin")) {
            // Если мы ввели просто /skin, то показать информацию о плагине
            if (args.length == 0) {
                PluginDescriptionFile description = plugin.getDescription();
                commandSender.sendMessage("§8§l=============================\n" +
                        " §9" + description.getName() + " " + description.getVersion() + "§f by §9RuscalWorld\n" +
                        "§8§l     ---------------------     \n" +
                        " §9/skin set <название> from <система>§f - установить скин\n" +
                        " §9/skin reset§f - установить скин по умолчанию\n" +
                        " §9/skin update§f - обновить скин\n" +
                        "§8§l     ---------------------     \n" +
                        " §fИсходный код: §9https://github.com/RuscalWorld/UniversalSkins\n" +
                        " §f© RuscalWorld, 2020\n" +
                        "§8§l=============================");
                return true;
            }

            // А дальше можно только игрокам, из консоли нельзя
            if (!(commandSender instanceof Player)) return false;
            Player player = (Player) commandSender;
            Skin skin = Skin.get(player.getName(), plugin);

            switch (args[0].toLowerCase()) {
                // Если мы ввели /skin set
                case "set":
                    String name = null;
                    SkinSystem system = null;

                    switch (args.length) {
                        case 1:
                        case 3:
                            // Неправильное использование: "/skin set *название не указано*", "/skin set <название> from *система скинов не указана*" и т.п.
                            player.sendMessage("§fИспользование: §9/skin set <название> [from <mojang/elyby>]");
                            break;
                        case 2:
                            // Игрок ввёл просто /skin set <название>, ищем нужный скин у Mojang
                            name = args[1];
                            system = new MojangSkinSystem(plugin);
                            break;
                        case 4:
                            // Игрок ввёл /skin set <название> from <система>, получаем систему скинов и название скина
                            name = args[1];
                            system = SkinSystem.fromName(args[3], plugin);

                            if (system == null) {
                                player.sendMessage("§c§l[!] §fНеизвестная система скинов: §7" + args[3] + "§f.");
                                return true;
                            }

                            break;
                    }

                    if (name == null) return false;

                    // Пытаемся поставить скин
                    try {
                        skin.setSkin(system, name);
                    } catch (Exception e) {
                        player.sendMessage("§c§l[!] §fНе удалось установить скин.");
                    }

                    break;
                case "update":
                    // Просто обновляем скин, если мы ввели /skin update
                    skin.update(new ResultHandler<Boolean>() {
                        @Override
                        public void handle(Boolean result) {
                            if (!result) player.sendMessage("§c§l[!] §fНе удалось обновить скин.");
                        }
                    });
                    break;
                case "reset":
                    // Восстанавливаем значения по умолчанию, если мы ввели /skin reset
                    try {
                        if (args.length == 2) skin = Skin.get(args[1], plugin);
                        skin.reset();
                    } catch (Exception e) {
                        player.sendMessage("§c§l[!] §fНе удалось обновить скин.");
                    }
                    break;
                case "reload":
                    // Перезагружаем конфиг, которого нет
                    if (!player.hasPermission("universalskins.reload")) return true;
                    plugin.reloadConfig();
                    player.sendMessage("Конфигурация перезагружена.");
                    break;
            }
        }

        return false;
    }
}

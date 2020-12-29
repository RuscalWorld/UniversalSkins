package ru.ruscalworld.universalskins.commands.tabcompleters;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Подсказки для команды /skin
 */
public class SkinTabCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length <= 1) return new ArrayList<>();
        List<String> suggestions = new ArrayList<>();
        String last = args[args.length - 2];

        switch (last) {
            case "set":
                // Возвращаем null, если игрок пишет название скина, чтобы ему просто вывело список игроков онлайн
                return null;
            case "from":
                // Возвращаем возможные названия систем скинов, если предыдущее слово - from
                suggestions.add("elyby");
                suggestions.add("mojang");
                return suggestions;
            default:
                // Возвращаем пустой список, чтобы Commodore спокойно выполнял свою работу
                return new ArrayList<>();
        }
    }
}

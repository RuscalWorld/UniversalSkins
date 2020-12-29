package ru.ruscalworld.universalskins.commands.tabcompleters;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SkinTabCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length <= 1) return new ArrayList<>();
        List<String> suggestions = new ArrayList<>();
        String last = args[args.length - 2];

        switch (last) {
            case "set":
                return null;
            case "from":
                suggestions.add("elyby");
                suggestions.add("mojang");
                return suggestions;
            default:
                return new ArrayList<>();
        }
    }
}

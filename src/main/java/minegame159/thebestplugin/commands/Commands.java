package minegame159.thebestplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.reflections.Reflections;

public class Commands {
    public static String HELP_TEXT;

    public static void register() {
        StringBuilder sb = new StringBuilder();

        int i = 0;
        for (Class<? extends MyCommand> klass : new Reflections("minegame159.thebestplugin.commands").getSubTypesOf(MyCommand.class)) {
            try {
                MyCommand command = klass.newInstance();
                Bukkit.getCommandMap().register("thebestplugin", command);

                if (i > 0) sb.append("\n");
                sb.append(ChatColor.GOLD).append(command.getName()).append(": ").append(ChatColor.WHITE).append(command.getDescription());

                i++;
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        HELP_TEXT = sb.toString();
    }
}

package minegame159.thebestplugin;

import minegame159.thebestplugin.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TabList {
    public static void update() {
        if (Bukkit.getOnlinePlayers().size() > 0) {
            String header = String.format("%s%sMeteor Pvp\n\n%sPlayers: %s%d", ChatColor.GRAY, ChatColor.BOLD, ChatColor.AQUA, ChatColor.WHITE, Bukkit.getServer().getOnlinePlayers().size());
            String footer = String.format("\n%sTPS: %s%.1f  %s-  %sMemory: %s%dMB / %dMB  %s-  %sUptime: %s%s", ChatColor.AQUA, ChatColor.WHITE, Bukkit.getTPS()[0], ChatColor.GRAY, ChatColor.AQUA, ChatColor.WHITE, Utils.getUsedRamMb(), Utils.getMaxRamMb(), ChatColor.GRAY, ChatColor.AQUA, ChatColor.WHITE, Utils.getUptimeString());

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.setPlayerListHeaderFooter(header, footer);
            }
        }
    }
}

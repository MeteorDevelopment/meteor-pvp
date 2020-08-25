package minegame159.thebestplugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import minegame159.thebestplugin.utils.Reflection;
import minegame159.thebestplugin.utils.Uptime;
import minegame159.thebestplugin.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class TabList {
    public static void update() {
        try {
            ProtocolManager manager = ProtocolLibrary.getProtocolManager();
            WrappedChatComponent header = WrappedChatComponent.fromText(String.format("%s%sMeteor Pvp\n\n%sPlayers: %s%d", ChatColor.GRAY, ChatColor.BOLD, ChatColor.AQUA, ChatColor.WHITE, Bukkit.getServer().getOnlinePlayers().size()));
            WrappedChatComponent footer = WrappedChatComponent.fromText(String.format("\n%sTPS: %s%.1f  %s-  %sMemory: %s%dMB / %dMB  %s-  %sUptime: %s%dd %dh", ChatColor.AQUA, ChatColor.WHITE, Bukkit.getTPS()[0], ChatColor.GRAY, ChatColor.AQUA, ChatColor.WHITE, Utils.getUsedRamMb(), Utils.getMaxRamMb(), ChatColor.GRAY, ChatColor.AQUA, ChatColor.WHITE, Uptime.getDays(), Uptime.getHours()));

            PacketContainer packet = manager.createPacket(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);
            Reflection.setField("header", packet.getHandle(), header.getHandle());
            Reflection.setField("footer", packet.getHandle(), footer.getHandle());

            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                manager.sendServerPacket(player, packet);
            }
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}

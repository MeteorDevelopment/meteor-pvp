package meteordevelopment.meteorpvp.utils;

import meteordevelopment.meteorpvp.kits.MaxKits;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

public class Msgs {
    // General
    public static String playerNotOnline() { return "Player is not online."; }
    public static String cantUseThisInPvp() { return "You can't use this command while in pvp."; }

    // Duels
    public static String dontHaveRequest() { return "You don't have duel request from that player."; }
    public static String cancelledRequest() { return "Cancelled duel request."; }
    public static String didntSendRequest() { return "You didn't send any duel request."; }
    public static String cantDuelYourself() { return "You can't duel yourself."; }
    public static String playerIsInDuel(String otherPlayer) { return "Player is in duel with " + otherPlayer + "."; }
    public static String duelRequestSent() { return "Duel request sent."; }
    public static String alreadySentRequest() { return "You have already sent a duel request, run /cancelduel to cancel it."; }
    public static String duelRequest(String player, String arena) { return ChatColor.YELLOW + "" + ChatColor.BOLD + player + ChatColor.GRAY + " wants to duel you in the " + arena + " arena."; }
    public static String playerDeclinedDuel(String player) { return ChatColor.GRAY + player + ChatColor.WHITE + " declined your duel."; }
    public static String youDeclinedDuel(String player) { return "Declined duel from " + ChatColor.GRAY + player + ChatColor.WHITE + "."; }
    public static String yourDuelExpired() { return "Your duel request expired."; }
    public static String duelExpired(String player) { return "Duel request from " + ChatColor.GRAY + player + ChatColor.WHITE + " expired."; }
    public static String noAvailableArenas() { return "There are currently no available arenas for this mode."; }

    // Kits
    public static String createdKit(String name) { return "Created kit with name " + ChatColor.GRAY + "'" + name + "'" + ChatColor.WHITE + "."; }
    public static String maxKits(MaxKits maxKits) { return "You can only have " + maxKits.count + " kits."; }
    public static String kitAlreadyExists(String name) { return "Kit with name " + ChatColor.GRAY + "'" + name + "' " + ChatColor.WHITE + "already exists."; }
    public static String deletedKit(String name) { return "Kit with name " + ChatColor.GRAY + "'" + name + "' " + ChatColor.WHITE + "has been deleted."; }
    public static String kitDoesntExist(String name) { return "Kit with name " + ChatColor.GRAY + "'" + name + "' " + ChatColor.WHITE + "doesn't exist."; }
    public static String kitCreatorToggled(boolean on) { return "Kit creator is now " + ChatColor.GRAY + (on ? "on" : "off") + ChatColor.WHITE + "."; }
    public static String dontOwnKit() { return "You don't own that kit."; }

    // Ignores
    public static String beingIgnored() { return ChatColor.RED + "That player is ignoring you, you cannot message them!"; }

    // Mutes
    public static String broadcastMute(OfflinePlayer player) { return String.format("%s%s%s has been muted.", ChatColor.RED, player.getName(), ChatColor.WHITE); }
    public static String broadcastUnmute(OfflinePlayer player) { return String.format("%s%s%s has been unmuted.", ChatColor.GREEN, player.getName(), ChatColor.WHITE); }
    public static String alreadyMuted() { return ChatColor.RED + "That player has already been muted."; }
    public static String notMuted() { return ChatColor.RED + "That player is not muted."; }
    public static String cantMuteYourself() { return ChatColor.RED + "You can't mute yourself."; }
    public static String cantMutePlayer() { return ChatColor.RED + "You can't mute that player."; }
}
